package com.acme.biz.config.repository;

import com.acme.biz.config.content.negotiation.ContentNegotiation;
import com.acme.biz.config.domain.Config;
import com.acme.biz.config.domain.ConfigBody;
import com.acme.biz.config.domain.ConfigContentType;
import com.acme.biz.config.event.ConfigCreateEvent;
import com.acme.biz.config.event.ConfigDeleteEvent;
import com.acme.biz.config.event.ConfigEventPublisher;
import com.acme.biz.config.event.ConfigUpdateEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

import static com.acme.biz.config.domain.ConfigContentType.TEXT;

@Component
public class ConfigRepository {
    private final ConfigEventPublisher publisher;
    ConcurrentMap<String, Deque<Config>> repository = new ConcurrentHashMap<>();
    ContentNegotiation contentNegotiation = new ContentNegotiation();

    public ConfigRepository(ConfigEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void createConfig(String configKey, ConfigBody configBody) {
        Config config = createConfig(configBody, 1);

        var deque = createDeque();
        deque.push(config);

        var result = repository.putIfAbsent(configKey, deque);
        if (Objects.nonNull(result)) {
            throw new ConcurrentModificationException(configKey + " has been created");
        }
        publisher.publishEvent(new ConfigCreateEvent(config, config.timestamp()));
    }

    private Config createConfig(ConfigBody configBody, int version) {
        var configBodyList = new ArrayList<ConfigBody>();
        configBodyList.add(configBody);
        var configContentType = configBody.contentType();
        if (Objects.equals(configContentType, TEXT)) {
            return new Config(configBodyList, version, Instant.now().toEpochMilli());
        }

        Map<String, ?> configValue = contentNegotiation.readString(configBody.content(), configContentType);
        Arrays.stream(ConfigContentType.values())
                .filter(contentType -> !(Objects.equals(contentType, TEXT) || Objects.equals(contentType, configContentType)))
                .map(contentType -> new ConfigBody(contentNegotiation.writeMap(configValue, contentType), contentType, false))
                .forEach(configBodyList::add);
        return new Config(configBodyList, version, Instant.now().toEpochMilli());
    }

    public Config getConfig(String configKey) {
        var config = getConfig(configKey, TEXT);
        return config;
    }

    public Deque<Config> deleteConfig(String configKey) {

        var deleted = repository.remove(configKey);
        publisher.publishEvent(new ConfigDeleteEvent(deleted));
        return deleted;
    }

    public ConfigBody putConfig(String configKey, ConfigBody newConfigBody) {
        AtomicReference<Config> reference = new AtomicReference<>();
        repository.computeIfPresent(configKey, (key, oldValue) -> {
            int version;
            if (oldValue.isEmpty()) {
                version = 1;
            } else {
                var config = oldValue.peek();
                reference.set(config);
                version = config.version() + 1;
            }

            oldValue.push(createConfig(newConfigBody, version));
            return oldValue;
        });
        publisher.publishEvent(new ConfigUpdateEvent(reference.get()));

        return reference.get().configBodies().stream()
                .filter(ConfigBody::isConfigured)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private static LinkedList<Config> createDeque() {
        return new LinkedList<>();
    }

    public Config getConfig(String configKey, ConfigContentType configContentType) {
        var deque = repository.getOrDefault(configKey, createDeque());
        var latestConfig = deque.peek();
        if (Objects.isNull(latestConfig)) {
            throw new IllegalStateException(configKey + "not found");
        }
        return latestConfig.copy(latestConfig.configBodies().stream()
                .filter(configBody -> Objects.equals(configBody.contentType(), configContentType))
                .findFirst().orElseThrow(() -> new IllegalStateException(configKey + "not found")));
    }

}
