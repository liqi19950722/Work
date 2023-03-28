package com.acme.biz.zookeeper.spring.context.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class DistributedConfigPropertySourceChangedEvent extends ApplicationEvent {
    public DistributedConfigPropertySourceChangedEvent(Object source) {
        super(source);
    }

    public DistributedConfigPropertySourceChangedEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
