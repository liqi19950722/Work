package com.acme.biz.config.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;

import java.util.Objects;

public class SpringConfigEventListener implements GenericApplicationListener {

    private final String clientId;
    private final String configKey;
    private ConfigEventListener configEventListener;

    public SpringConfigEventListener(String clientId, String configKey, ConfigEventListener configEventListener) {
        this.clientId = clientId;
        this.configKey = configKey;
        this.configEventListener = configEventListener;
    }

    @Override
    public boolean supportsEventType(ResolvableType eventType) {
        Class<?> payloadEventClass = eventType.getRawClass();
        Class<?> eventClass = eventType.getGeneric(0).getRawClass();
        if (Objects.isNull(payloadEventClass) || Objects.isNull(eventClass)) {
            return false;
        }

        return PayloadApplicationEvent.class.isAssignableFrom(payloadEventClass) &&
                ConfigChangeEvent.class.isAssignableFrom(eventClass);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof PayloadApplicationEvent<?> payloadEvent) {
            Object payload = payloadEvent.getPayload();
            if (payload instanceof ConfigUpdateEvent configUpdateEvent) {
                configEventListener.onConfigUpdateEvent(configUpdateEvent);
            }
            if (payload instanceof ConfigCreateEvent configCreateEvent) {
                configEventListener.onConfigCreateEvent(configCreateEvent);
            }
            if (payload instanceof ConfigDeleteEvent configDeleteEvent) {
                configEventListener.onConfigDeleteEvent(configDeleteEvent);
            }
        }
    }

    public String getClientId() {
        return clientId;
    }

    public String getConfigKey() {
        return configKey;
    }

    public boolean isSameClient(String clientId, String configKey) {
        return Objects.equals(this.clientId, clientId) && Objects.equals(this.configKey, configKey);
    }

    public void unSubscribeConfigListener() {
        configEventListener.unregister();
    }
}
