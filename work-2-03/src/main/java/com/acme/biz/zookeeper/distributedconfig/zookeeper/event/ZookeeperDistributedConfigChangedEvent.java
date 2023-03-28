package com.acme.biz.zookeeper.distributedconfig.zookeeper.event;

import com.acme.biz.zookeeper.distributedconfig.event.DistributedConfigChangedEvent;

public class ZookeeperDistributedConfigChangedEvent extends DistributedConfigChangedEvent<EventSource> {
    public ZookeeperDistributedConfigChangedEvent(String source, EventSource context) {
        super(source);
        super.context = context;
    }

    @Override
    public EventSource getContext() {
        return context;
    }
}

