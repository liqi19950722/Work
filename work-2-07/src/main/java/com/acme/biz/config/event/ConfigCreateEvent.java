package com.acme.biz.config.event;

import com.acme.biz.config.domain.Config;

public class ConfigCreateEvent extends ConfigChangeEvent {
    final long timestamp;

    public ConfigCreateEvent(Config source, long timestamp) {
        super(source);
        this.timestamp = timestamp;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }
}
