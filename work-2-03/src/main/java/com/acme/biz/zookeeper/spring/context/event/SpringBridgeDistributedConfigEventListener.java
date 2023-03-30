package com.acme.biz.zookeeper.spring.context.event;

import com.acme.biz.zookeeper.distributedconfig.event.DistributedConfigChangedEvent;
import com.acme.biz.zookeeper.distributedconfig.event.DistributedConfigEventListener;
import com.acme.biz.zookeeper.distributedconfig.event.EventContext;
import com.acme.biz.zookeeper.spring.core.env.DistributedConfigPropertySource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class SpringBridgeDistributedConfigEventListener implements DistributedConfigEventListener, EnvironmentAware, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;
    private ConfigurableEnvironment configurableEnvironment;
    private Boolean isConfigurableEnvironment = false;

    @Override
    public void onDistributedConfigReceived(DistributedConfigChangedEvent distributedConfigEvent) {
        if (isConfigurableEnvironment) {
            var propertySources = configurableEnvironment.getPropertySources();
            var propertySource = propertySources.get(distributedConfigEvent.getSource());
            if (propertySource instanceof DistributedConfigPropertySource distributedConfigPropertySource) {
                Optional.of(EventContext.getEventContext(distributedConfigEvent))
                        .filter(eventContext -> StringUtils.hasText(eventContext.propertyKey()))
                        .ifPresent(eventContext -> changePropertySource(eventContext, distributedConfigPropertySource));
            }
        }
    }

    private void changePropertySource(EventContext eventContext, DistributedConfigPropertySource distributedConfigPropertySource) {
        distributedConfigPropertySource.setProperty(eventContext.propertyKey(), eventContext.propertyValue());
        applicationEventPublisher.publishEvent(new DistributedConfigPropertySourceChangedEvent(eventContext));
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        if (environment instanceof ConfigurableEnvironment configurableEnvironment) {
            this.configurableEnvironment = configurableEnvironment;
            this.isConfigurableEnvironment = true;
        }

    }

    @Override
    public Integer order() {
        return DEFAULT_ORDER + 1;
    }
}
