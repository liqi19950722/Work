package com.acme.biz.zookeeper.distributedconfig.event;

import com.acme.biz.zookeeper.distributedconfig.zookeeper.event.ZookeeperDistributedConfigChangedEvent;

public interface EventContext {

    String propertyKey();
    String propertyValue();

    static EventContext getEventContext(DistributedConfigChangedEvent distributedConfigEvent) {
        if (distributedConfigEvent instanceof ZookeeperDistributedConfigChangedEvent zookeeper) {
            return zookeeper.getContext();
        }
        throw new IllegalStateException("还未实现: " + distributedConfigEvent.getClass());
    }
}
