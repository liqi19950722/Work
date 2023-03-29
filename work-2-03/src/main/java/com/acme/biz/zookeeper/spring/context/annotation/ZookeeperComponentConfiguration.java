package com.acme.biz.zookeeper.spring.context.annotation;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.DefaultTracerDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.TimeUnit;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Configuration(proxyBeanMethods = false)
public class ZookeeperComponentConfiguration {

    @Scope(SCOPE_SINGLETON)
    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curatorFramework(@Value(value = "${zookeeper.connectString}") String connectString,
                                             @Value(value = "${zookeeper.sessionTimeoutMs}") int sessionTimeout,
                                             @Value(value = "${zookeeper.connectionTimeoutMs}") int connectionTimeout,
                                             @Value(value = "${zookeeper.baseSleepTimeMs}") int baseSleepTime,
                                             @Value(value = "${zookeeper.maxRetries}") int maxRetries,
                                             @Value(value = "${zookeeper.maxSleepMs}") int maxSleep,
                                             @Value(value = "${zookeeper.blockUntilConnected}") int blockUntilConnected,
                                             @Value(value = "${zookeeper.blockUntilConnectedTimeUnit}") TimeUnit blockUntilConnectedTimeUnit) throws Exception {
        var curator = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .sessionTimeoutMs(sessionTimeout)
                .connectionTimeoutMs(connectionTimeout)
                .retryPolicy(new ExponentialBackoffRetry(baseSleepTime, maxRetries, maxSleep))
                .build();

        curator.getZookeeperClient().setTracerDriver(new DefaultTracerDriver());
        curator.blockUntilConnected(blockUntilConnected, blockUntilConnectedTimeUnit);
        return curator;
    }
}
