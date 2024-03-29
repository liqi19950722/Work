package com.acme.biz.zookeeper.distributedconfig.zookeeper.event;

import com.acme.biz.zookeeper.distributedconfig.event.DistributedConfigChangedEvent;
import com.acme.biz.zookeeper.distributedconfig.event.EventContext;

public class ZookeeperDistributedConfigChangedEvent extends DistributedConfigChangedEvent {
    public ZookeeperDistributedConfigChangedEvent(String source, EventSource context) {
        super(source, context);
        super.context = context;
    }

    @Override
    public EventContext getContext() {
        return context;
    }
}

