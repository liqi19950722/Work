package com.acme.biz.zookeeper.distributedconfig;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

public interface DistributedConfigDataBase {
    String DEFAULT_CONFIG_NAMESPACE = "distributed-config";
    String DEFAULT_APPLICATION_NAME = "application";

    String getConfigNamespace();

    String getApplicationName();

    <T extends Serializable> void putConfig(ConfigProfile configProfile, String key, T value);

    <T extends Serializable> void putConfig(String key, T value);

    Map<String, String> loadConfig();

    record ConfigProfile(String profileName, Integer order) implements Comparable<ConfigProfile> {

        @Override
        public int compareTo(ConfigProfile o) {
            return Comparator.<Integer>naturalOrder().compare(this.order(), o.order());
        }
    }
}

