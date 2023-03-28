package com.acme.biz.zookeeper.spring.core.env;

import com.acme.biz.zookeeper.distributedconfig.DistributedConfigDataBase;
import org.springframework.core.env.EnumerablePropertySource;

import java.util.LinkedHashMap;
import java.util.Map;

public class DistributedConfigPropertySource extends EnumerablePropertySource<DistributedConfigDataBase> {
    private final Map<String, String> properties = new LinkedHashMap<>();

    public static final String PROPERTY_SOURCE_NAME = "distributedConfigPropertySource";

    public DistributedConfigPropertySource(String name, DistributedConfigDataBase source) {

        super(name, source);
        properties.putAll(source.loadConfig());
    }

    @Override
    public String[] getPropertyNames() {
        return properties.keySet().toArray(new String[0]);
    }

    @Override
    public String getProperty(String name) {
        return properties.get(name);
    }

    public void setProperty(String name, String value) {
        properties.put(name, value);
    }
}
