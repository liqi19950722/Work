package com.acme.biz.app.loadbalancer;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RoundRobinBasedUptimeRule extends AbstractLoadBalancerRule {
    private static final Logger log = LoggerFactory.getLogger(RoundRobinBasedUptimeRule.class);

    private final ConcurrentMap<String, WeightedRoundRobin> serviceWeightMap = new ConcurrentHashMap<>();

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

        System.out.println(clientConfig);
    }

    @Override
    public Server choose(Object key) {
        List<Server> allServers = getLoadBalancer().getReachableServers();
        AtomicInteger weightCounter = recalculateServiceInstanceWeightAndGetTotalWeight(allServers);
        return serviceWeightMap.entrySet().stream().max(Comparator.comparingLong(entry -> entry.getValue().current.get()))
                .map(it -> {
                    WeightedRoundRobin weightedRoundRobin = it.getValue();
                    log.info("before sel selected {} current {} weight {}", weightedRoundRobin.getServer(), weightedRoundRobin.current.get(), weightedRoundRobin.getWeight());
                    weightedRoundRobin.sel(weightCounter.get());
                    log.info("sel selected {} current {}", weightedRoundRobin.getServer(), weightedRoundRobin.current.get());

                    return weightedRoundRobin.getServer();
                }).orElseGet(() -> allServers.get(0));
        /*
         int totalWeight = 0;
        long flag = Long.MIN_VALUE;
        Server selectedServer = null;
        WeightedRoundRobin selectedWRR = null;
        for (Server server : allServers) {
            DiscoveryEnabledServer discoveryEnabledServer = (DiscoveryEnabledServer) server;

            int actualWeight = getActualServerWeight(discoveryEnabledServer);

            String serviceKey = buildServiceKey(discoveryEnabledServer);
            WeightedRoundRobin weightedRoundRobin = serviceWeightMap.computeIfAbsent(serviceKey, k -> {
                WeightedRoundRobin wrr = new WeightedRoundRobin();
                wrr.setWeight(actualWeight);
                return wrr;
            });

            if (actualWeight != weightedRoundRobin.getWeight()) {
                //weight changed
                weightedRoundRobin.setWeight(actualWeight);
            }
            long cur = weightedRoundRobin.increaseCurrent();
            weightedRoundRobin.setLastUpdate(System.currentTimeMillis());
            if (cur > flag) {
                flag = cur;
                selectedServer = server;
                selectedWRR = weightedRoundRobin;
            }
            totalWeight += actualWeight;
        }

        if (selectedServer != null) {
            log.info("selected {} weight {}", selectedServer, selectedWRR.getWeight());
            selectedWRR.sel(totalWeight);
            return selectedServer;
        }
        return allServers.get(0);*/
    }

    private AtomicInteger recalculateServiceInstanceWeightAndGetTotalWeight(List<Server> allServers) {
        AtomicInteger weightCounter = new AtomicInteger();
        allServers.forEach(server -> {
            DiscoveryEnabledServer discoveryEnabledServer = (DiscoveryEnabledServer) server;
            int actualWeight = getActualServerWeight(discoveryEnabledServer);
            weightCounter.addAndGet(actualWeight);
            serviceWeightMap.compute(buildServiceKey(discoveryEnabledServer), (serviceKey, oldValue) -> {
                if (Objects.isNull(oldValue)) {
                    WeightedRoundRobin wrr = new WeightedRoundRobin();
                    wrr.setWeight(actualWeight);
                    wrr.setServer(discoveryEnabledServer);
                    return wrr;
                } else {
                    if (actualWeight != oldValue.getWeight()) {
                        //weight changed
                        oldValue.setWeight(actualWeight);

                    }
                    oldValue.increaseCurrent();
                    oldValue.setLastUpdate(System.currentTimeMillis());
                    return oldValue;
                }
            });
        });
        return weightCounter;
    }

    private int getActualServerWeight(DiscoveryEnabledServer discoveryEnabledServer) {
        int serverWeight = getServerWeight(discoveryEnabledServer); // 服务器权重
        long registrationTimestamp = discoveryEnabledServer.getInstanceInfo().getLeaseInfo().getRegistrationTimestamp();
        int actualWeight = serverWeight;
        if (serverWeight > 0 && registrationTimestamp > 0) {
            long uptime = System.currentTimeMillis() - registrationTimestamp;
            long serverWarmup = getServerWarmup(discoveryEnabledServer); // 服务器预热时间
            if (serverWarmup > 0 && uptime < serverWarmup) {
                int ww = (int) (uptime / ((float) serverWarmup / serverWeight));
                actualWeight = ww < 1 ? 1 : (Math.min(ww, serverWeight));
            }
        }
        actualWeight = Math.max(actualWeight, 0);
        return actualWeight;
    }

    private String buildServiceKey(DiscoveryEnabledServer server) {
        InstanceInfo instanceInfo = server.getInstanceInfo();
        return instanceInfo.getIPAddr() + ":" + instanceInfo.getPort();
    }

    private long getServerWarmup(DiscoveryEnabledServer server) {

        // TODO 从注册中心中取出服务的预热时间
        long warmup = 10 * 60 * 1000;
        return warmup;
    }

    private int getServerWeight(DiscoveryEnabledServer server) {
        // TODO 从注册中心中取出服务的权重
        return 100;
    }

    class WeightedRoundRobin {

        public Server getServer() {
            return server;
        }

        public void setServer(DiscoveryEnabledServer server) {
            this.server = server;
        }

        private DiscoveryEnabledServer server;

        private int weight;
        private AtomicLong current = new AtomicLong(0);
        private long lastUpdate;

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
            current.set(0);
        }

        public long increaseCurrent() {
            return current.addAndGet(weight);
        }

        public void sel(int total) {
            current.addAndGet(-1 * total);
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }
    }
}
