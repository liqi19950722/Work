package com.acme.biz.listener;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.core.env.StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME;

public class ApplicationEnvironmentPreparedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        PropertiesPropertySource propertiesPropertySource = (PropertiesPropertySource)propertySources.get(SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME);
        Map<String, Object> source = propertiesPropertySource.getSource();
        propertySources.replace(SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, new MapPropertySource(SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, new HashMap<>(source)));
    }
}
