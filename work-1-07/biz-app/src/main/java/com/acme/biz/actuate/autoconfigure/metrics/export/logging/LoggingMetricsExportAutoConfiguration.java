package com.acme.biz.actuate.autoconfigure.metrics.export.logging;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.ConditionalOnEnabledMetricsExport;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(before = CompositeMeterRegistryAutoConfiguration.class, after = MetricsAutoConfiguration.class)
@ConditionalOnBean(Clock.class)
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnMissingBean(MeterRegistry.class)
@ConditionalOnEnabledMetricsExport("logging")
public class LoggingMetricsExportAutoConfiguration {

    private final LoggingProperties properties;

    public LoggingMetricsExportAutoConfiguration(LoggingProperties properties) {
        this.properties = properties;
    }

    @Bean
    public LoggingMeterRegistry loggingMeterRegistry(LoggingRegistryConfig loggingRegistryConfig, Clock clock) {
        return new LoggingMeterRegistry(loggingRegistryConfig, clock);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoggingRegistryConfig loggingRegistryConfig() {
        return new LoggingPropertiesConfigAdapter(this.properties);
    }
}
