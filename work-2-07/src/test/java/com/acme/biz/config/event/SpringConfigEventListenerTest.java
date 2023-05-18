package com.acme.biz.config.event;

import com.acme.biz.config.domain.Config;
import org.junit.jupiter.api.Test;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

import java.time.Instant;
import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SpringConfigEventListenerTest {

    @Test
    public void should_listen_ConfigChangeEvent() {
        var listener = spy(new SpringConfigEventListener("clientId", "configKey", mock(ConfigEventListener.class)));
        var applicationEventMulticaster = new SimpleApplicationEventMulticaster();
        applicationEventMulticaster.addApplicationListener(listener);

        var source = mock(Object.class);
        var configChangeEvent = mock(ConfigChangeEvent.class);
        var payloadEvent = new PayloadApplicationEvent<>(source, configChangeEvent, null);

        applicationEventMulticaster.multicastEvent(payloadEvent);

        verify(listener, times(1)).onApplicationEvent(same(payloadEvent));
    }

    @Test
    public void should_do_action_on_ConfigUpdatedEvent() {
        var source = mock(Object.class);
        var configUpdatedEvent = new ConfigUpdateEvent(new Config(List.of(), 1, Instant.now().toEpochMilli()));
        var payloadEvent = new PayloadApplicationEvent<>(source, configUpdatedEvent, null);

        var configEventListener = spy(new ConfigEventListener() {
            @Override
            public void onConfigUpdateEvent(ConfigUpdateEvent event) {
                assertSame(configUpdatedEvent, event);
            }
        });
        var listener = new SpringConfigEventListener("clientId", "configKey", configEventListener);
        listener.onApplicationEvent(payloadEvent);

        verify(configEventListener).onConfigUpdateEvent(same(configUpdatedEvent));
    }

    @Test
    public void should_do_action_on_ConfigCreatedEvent() {
        var source = mock(Object.class);
        var configCreatedEvent = new ConfigCreateEvent(new Config(List.of(), 1, Instant.now().toEpochMilli()), Instant.now().toEpochMilli());
        var payloadEvent = new PayloadApplicationEvent<>(source, configCreatedEvent, null);

        var configEventListener = spy(new ConfigEventListener() {
            @Override
            public void onConfigCreateEvent(ConfigCreateEvent event) {
                assertSame(configCreatedEvent, event);
            }
        });
        var listener = new SpringConfigEventListener("clientId", "configKey", configEventListener);
        listener.onApplicationEvent(payloadEvent);

        verify(configEventListener).onConfigCreateEvent(same(configCreatedEvent));
    }

    @Test
    public void should_do_action_on_ConfigDeleteEvent() {
        var source = mock(Object.class);
        var configDeleteEvent = new ConfigDeleteEvent(mock(Deque.class));
        var payloadEvent = new PayloadApplicationEvent<>(source, configDeleteEvent, null);

        var configEventListener = spy(new ConfigEventListener() {
            @Override
            public void onConfigDeleteEvent(ConfigDeleteEvent event) {
                assertSame(configDeleteEvent, event);
            }
        });
        var listener = new SpringConfigEventListener("clientId", "configKey", configEventListener);
        listener.onApplicationEvent(payloadEvent);

        verify(configEventListener).onConfigDeleteEvent(same(configDeleteEvent));
    }
}
