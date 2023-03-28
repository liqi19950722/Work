package com.acme.biz.zookeeper.distributedconfig.event;

import java.util.EventObject;

public abstract class DistributedConfigChangedEvent<T> extends EventObject {
    private final long timestamp;
    protected T context;


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

    public abstract T getContext();
}
