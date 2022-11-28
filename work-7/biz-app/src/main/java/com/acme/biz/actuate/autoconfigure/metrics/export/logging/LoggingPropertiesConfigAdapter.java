package com.acme.biz.actuate.autoconfigure.metrics.export.logging;

import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.StepRegistryPropertiesConfigAdapter;


class LoggingPropertiesConfigAdapter extends StepRegistryPropertiesConfigAdapter<LoggingProperties>
        implements LoggingRegistryConfig {

    LoggingPropertiesConfigAdapter(LoggingProperties properties) {
        super(properties);
    }

    @Override
    public String prefix() {
        return "management.metrics.export.logging";
    }


}
