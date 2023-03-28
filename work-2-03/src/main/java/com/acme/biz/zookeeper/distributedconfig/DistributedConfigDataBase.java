package com.acme.biz.zookeeper.distributedconfig;

import com.acme.biz.zookeeper.distributedconfig.event.DistributedConfigEventListener;
import com.acme.biz.zookeeper.spring.core.env.DistributedConfigPropertySource;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

public interface DistributedConfigDataBase {
    String DEFAULT_CONFIG_NAMESPACE = "distributed-config";
    String DEFAULT_APPLICATION_NAME = "application";

    String getConfigNamespace();

    String getApplicationName();

    String getDataBaseType();

    <T extends Serializable> void putConfig(ConfigProfile configProfile, String key, T value);

    <T extends Serializable> void putConfig(String key, T value);

    Map<String, String> loadConfig();

    void registerListener(DistributedConfigEventListener listener);

    default String buildPropertySourceName() {
        return DistributedConfigPropertySource.PROPERTY_SOURCE_NAME
                + "-" + getDataBaseType()
                + "-" + getConfigNamespace()
                + "-" + getApplicationName();
    }



    record ConfigProfile(String profileName, Integer order) implements Comparable<ConfigProfile> {

        @Override
        public int compareTo(ConfigProfile o) {
            return Comparator.<Integer>naturalOrder().compare(this.order(), o.order());
        }
    }
    enum DataBaseType {
        ZOOKEEPER
    }
}

