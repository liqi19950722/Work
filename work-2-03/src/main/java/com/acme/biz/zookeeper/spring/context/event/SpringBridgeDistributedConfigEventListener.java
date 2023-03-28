package com.acme.biz.zookeeper.spring.context.event;

import com.acme.biz.zookeeper.distributedconfig.event.DistributedConfigChangedEvent;
import com.acme.biz.zookeeper.distributedconfig.event.DistributedConfigEventListener;
import com.acme.biz.zookeeper.distributedconfig.zookeeper.EventContext;
import com.acme.biz.zookeeper.distributedconfig.zookeeper.event.ZookeeperDistributedConfigChangedEvent;
import com.acme.biz.zookeeper.spring.core.env.DistributedConfigPropertySource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

public class SpringBridgeDistributedConfigEventListener implements DistributedConfigEventListener, EnvironmentAware, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;
    private ConfigurableEnvironment configurableEnvironment;
    private Boolean isConfigurableEnvironment = false;

    @Override
    public void onDistributedConfigReceived(DistributedConfigChangedEvent distributedConfigEvent) {
        if (isConfigurableEnvironment) {
            String propertySourceName = distributedConfigEvent.getSource();
            MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
            PropertySource<?> propertySource = propertySources.get(propertySourceName);
            if (propertySource instanceof DistributedConfigPropertySource distributedConfigPropertySource) {
                if (distributedConfigEvent instanceof ZookeeperDistributedConfigChangedEvent zookeeper) {
                    EventContext context = zookeeper.getContext();
                    if (StringUtils.hasText(context.propertyKey())) {
                        distributedConfigPropertySource.setProperty(context.propertyKey(), context.propertyValue());
                    }
                }
            }
            applicationEventPublisher.publishEvent(new DistributedConfigPropertySourceChangedEvent(distributedConfigEvent.getContext()));
        }
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
