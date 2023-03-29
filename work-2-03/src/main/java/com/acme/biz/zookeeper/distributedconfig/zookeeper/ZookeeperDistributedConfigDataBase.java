package com.acme.biz.zookeeper.distributedconfig.zookeeper;

import com.acme.biz.zookeeper.distributedconfig.DistributedConfigDataBase;
import com.acme.biz.zookeeper.distributedconfig.event.DistributedConfigEventListener;
import com.acme.biz.zookeeper.distributedconfig.zookeeper.event.EventSource;
import com.acme.biz.zookeeper.distributedconfig.zookeeper.event.ZookeeperDistributedConfigChangedEvent;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener.Type;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import java.io.Serializable;
import java.util.*;

import static com.acme.biz.zookeeper.distributedconfig.DistributedConfigDataBase.DataBaseType.ZOOKEEPER;

public class ZookeeperDistributedConfigDataBase implements DistributedConfigDataBase {

    private final CuratorFramework curatorFramework;
    private final String configNamespace;
    private final String applicationName;

    @Override
    public String getConfigNamespace() {
        return configNamespace;
    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }


    private final List<ConfigProfile> profiles = Collections.synchronizedList(new ArrayList<>());
    private final List<DistributedConfigEventListener> listeners = Collections.synchronizedList(new ArrayList<>());

    public ZookeeperDistributedConfigDataBase(CuratorFramework curatorFramework) {
        this(curatorFramework, DistributedConfigDataBase.DEFAULT_CONFIG_NAMESPACE, DistributedConfigDataBase.DEFAULT_APPLICATION_NAME);
    }

    public ZookeeperDistributedConfigDataBase(CuratorFramework curatorFramework, String configNamespace, String applicationName) {
        this.curatorFramework = curatorFramework;
        this.configNamespace = configNamespace;
        this.applicationName = applicationName;
        profiles.add(new ConfigProfile("default", 0));


        profiles.forEach(profile -> {
            String baseConfigPath = buildBaseConfigPath(profile.profileName());
            CuratorCache curatorCache = CuratorCache.builder(curatorFramework, baseConfigPath).build();

            curatorCache.listenable().addListener(CuratorCacheListener.builder()
                    .forCreatesAndChanges(
                            (oldNode, node) -> listeners.stream().sorted().forEach(listener -> listener.onDistributedConfigReceived(
                                    new ZookeeperDistributedConfigChangedEvent(buildPropertySourceName(),
                                            getEventSource(baseConfigPath, node, oldNode == null ? Type.NODE_CREATED : Type.NODE_CHANGED)))))
                    .forDeletes(
                            deleteNode -> listeners.stream().sorted().forEach(listener -> listener.onDistributedConfigReceived(
                                    new ZookeeperDistributedConfigChangedEvent(buildPropertySourceName(),
                                            getEventSource(baseConfigPath, deleteNode, Type.NODE_DELETED)))))
                    .build());
            curatorCache.start();
        });
    }

    private EventSource getEventSource(String baseConfigPath, ChildData node, Type type) {
        var data = node.getData();
        if (data != null && data.length > 0) {
            var propertyValue = JsonSerializer.deserialize(data).data();
            return new EventSource(type, getPropertyKey(baseConfigPath, node.getPath()),
                    propertyValue.toString());
        } else {
            return new EventSource(type, getPropertyKey(baseConfigPath, node.getPath()), "");
        }
    }

    @Override
    public <T extends Serializable> void putConfig(ConfigProfile configProfile, String key, T value) {

        var path = buildBaseConfigPath(configProfile.profileName()) + "/" + key.replace('.', '/');
        var configValue = new ConfigValue<>(new ConfigValueMetadata("text", SerializationUtils.serialize(value).length), value);
        try {
            var stat = curatorFramework.checkExists().forPath(path);
            if (stat == null) {
                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, JsonSerializer.serialize(configValue));
            } else {
                curatorFramework.setData().forPath(path, JsonSerializer.serialize(configValue));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (!profiles.contains(configProfile)) {
            profiles.add(configProfile);
        }
    }

    @Override
    public <T extends Serializable> void putConfig(String key, T value) {
        putConfig(new ConfigProfile("default", 0), key, value);
    }

    @Override
    public Map<String, String> loadConfig() {
        Map<String, Map<String, String>> properties = new HashMap<>();
        for (ConfigProfile profile : profiles) {
            String basePath = buildBaseConfigPath(profile.profileName());
            Map<String, String> profileProperties = doLoadConfig(basePath, basePath);
            properties.put(basePath.substring(basePath.lastIndexOf("/") + 1), profileProperties);
        }

        Map<String, String> result = new HashMap<>();
        profiles.stream().sorted().forEach(profile -> {
            Map<String, String> property = properties.get(profile.profileName());
            result.putAll(property);
        });
        return result;
    }

    @Override
    public void registerListener(DistributedConfigEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public String getDataBaseType() {
        return ZOOKEEPER.name().toLowerCase();
    }

    private Map<String, String> doLoadConfig(String childPath, String basePath) {
        var properties = new HashMap<String, String>();

        var children = getChildren(childPath);
        if (children.isEmpty()) {
            return properties;
        }
        for (var child : children) {
            var path = childPath + "/" + child;
            properties.putAll(doLoadConfig(path, basePath));

            Optional.ofNullable(getData(path))
                    .filter(data -> data.length > 0)
                    .map(JsonSerializer::deserialize)
                    .ifPresent(configValue -> properties.put(getPropertyKey(basePath, path), String.valueOf(configValue.data())));

        }
        return properties;
    }

    private static String getPropertyKey(String basePath, String path) {
        var replaced = path.replace(basePath, "").replace('/', '.');
        if (StringUtils.isEmpty(replaced)) {
            return "";
        }
        return replaced.substring(1);

    }


    private byte[] getData(String child) {
        try {
            return curatorFramework.getData().forPath(child);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getChildren(String basePath) {
        try {
            return curatorFramework.getChildren().forPath(basePath);
        } catch (Exception e) {
            if (e instanceof KeeperException keeperException) {
                if (keeperException.code() != KeeperException.Code.NONODE) { // not found
                    throw new RuntimeException(keeperException);
                }
            }
            // exception ignored
        }
        return List.of();
    }

    private String buildBaseConfigPath(String profile) {
        return "/" + configNamespace + "/" + applicationName + "/" + profile;
    }
}
