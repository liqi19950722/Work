package com.acme.biz.config.event;

import com.acme.biz.config.domain.Config;

import java.time.Instant;
import java.util.Deque;

public class ConfigDeleteEvent extends ConfigChangeEvent {
    final long timestamp;
    public ConfigDeleteEvent(Deque<Config> source) {
        super(source);
        this.timestamp = Instant.now().toEpochMilli();
    }


    @Override
    public long getTimestamp() {
        return timestamp;
    }
}
