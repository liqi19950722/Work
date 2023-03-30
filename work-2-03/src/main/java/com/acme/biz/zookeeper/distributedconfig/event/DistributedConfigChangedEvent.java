package com.acme.biz.zookeeper.distributedconfig.event;

import java.util.EventObject;
import java.util.Objects;

public abstract class DistributedConfigChangedEvent extends EventObject {
    private final long timestamp;
    protected EventContext context;


    public DistributedConfigChangedEvent(String source, EventContext context) {
        super(source);
        this.timestamp = System.currentTimeMillis();
        Objects.requireNonNull(context, "EventContext is null");
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
