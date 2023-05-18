package com.acme.biz.config.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
public class SpringConfigEventPublisher implements ConfigEventPublisher, ApplicationEventPublisherAware {


    @Override
    public void publishEvent(ConfigChangeEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
