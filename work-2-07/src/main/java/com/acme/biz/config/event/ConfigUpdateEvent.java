package com.acme.biz.config.event;

import com.acme.biz.config.domain.Config;

import java.time.Instant;

public class ConfigUpdateEvent extends ConfigChangeEvent {

    final long timestamp;

    public ConfigUpdateEvent(Config source) {
        super(source);
        this.timestamp = Instant.now().toEpochMilli();
    }

    @Override
    public long getTimestamp() {
        return 0;
    }
}
