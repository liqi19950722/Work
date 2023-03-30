package com.acme.biz.zookeeper.distributedconfig.event;

public interface EventContext {
    String propertyKey();
    String propertyValue();
}
