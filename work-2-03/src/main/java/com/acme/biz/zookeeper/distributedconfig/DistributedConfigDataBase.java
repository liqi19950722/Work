package com.acme.biz.zookeeper.distributedconfig;

import com.acme.biz.zookeeper.distributedconfig.event.DistributedConfigEventListener;
import com.acme.biz.zookeeper.spring.core.env.DistributedConfigPropertySource;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
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

    record ConfigValue<T>(ConfigValueMetadata metadata,
                          @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, defaultImpl = Object.class) T data) implements Serializable {

    }


    record ConfigValueMetadata(@JsonProperty(value = "content-type") String contentType,
                               @JsonProperty(value = "content-length") Integer contentLength) {
    }

    record ConfigProfile(String profileName, Integer order) implements Comparable<ConfigProfile> {

        @Override
        public int compareTo(ConfigProfile o) {
            return Comparator.<Integer>reverseOrder().compare(this.order(), o.order());
        }
    }

    class JsonSerializer {
        private static final ObjectMapper objectMapper = new ObjectMapper();

        public static byte[] serialize(ConfigValue<?> configValue) {
            try {
                return objectMapper.writeValueAsBytes(configValue);
            } catch (JsonProcessingException ignore) {
                return new byte[]{};
            }
        }

        public static <T> ConfigValue<T> deserialize(byte[] bytes) {
            try {
                return objectMapper.readValue(bytes, new TypeReference<>() {
                });
            } catch (IOException ignore) {
                return new ConfigValue<>(new ConfigValueMetadata(null, null), null);
            }
        }
    }

    enum DataBaseType {
        ZOOKEEPER
    }
}

