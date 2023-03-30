package com.acme.biz.zookeeper.distributedconfig.event;

import java.util.EventObject;

public abstract class DistributedConfigChangedEvent extends EventObject {
    private final long timestamp;
    protected EventContext context;


    public DistributedConfigChangedEvent(String source) {
        super(source);
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String getSource() {
        return (String) super.getSource();
    }

    public abstract EventContext getContext();
}
