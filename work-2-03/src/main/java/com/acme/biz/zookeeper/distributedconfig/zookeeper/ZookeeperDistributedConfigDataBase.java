package com.acme.biz.zookeeper.distributedconfig.zookeeper;

import com.acme.biz.zookeeper.distributedconfig.DistributedConfigDataBase;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.io.Serializable;

public class ZookeeperDistributedConfigDataBase implements DistributedConfigDataBase {

    private static final String DEFAULT_CONFIG_NAMESPACE = "config";
    private static final String DEFAULT_APPLICATION_NAME = "application";
    private final CuratorFramework curatorFramework;
    private final String configNamespace;
    private final String applicationName;


    public ZookeeperDistributedConfigDataBase(CuratorFramework curatorFramework, String configNamespace, String applicationName) {
        this.curatorFramework = curatorFramework;
        this.configNamespace = configNamespace;
        this.applicationName = applicationName;

    }

    public ZookeeperDistributedConfigDataBase(CuratorFramework curatorFramework) {
        this(curatorFramework, DEFAULT_CONFIG_NAMESPACE, DEFAULT_APPLICATION_NAME);
    }

    @Override
    public <T extends Serializable> void putConfig(String profile, String key, T value) {
        String path = "/" + configNamespace + "/" + applicationName + "/" + profile + "/" + key.replace('.', '/');
        try {
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, SerializationUtils.serialize(value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends Serializable> void putConfig(String key, T value) {
        putConfig("default", key, value);
    }
}
