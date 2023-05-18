package com.acme.biz.config.event;

public interface ConfigEventPublisher {
    void publishEvent(ConfigChangeEvent event);
}
