package com.acme.biz.zookeeper.distributedconfig;

import java.io.Serializable;

public interface DistributedConfigDataBase {
    <T extends Serializable> void putConfig(String profile, String key, T value);
    <T extends Serializable> void putConfig(String key, T value);
}

