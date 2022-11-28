package com.acme.biz.actuate.autoconfigure.metrics.export.logging;

import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.StepRegistryProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "management.metrics.export.logging")
public class LoggingProperties extends StepRegistryProperties {
}
