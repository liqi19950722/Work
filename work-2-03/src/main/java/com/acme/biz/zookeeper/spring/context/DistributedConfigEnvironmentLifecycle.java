package com.acme.biz.zookeeper.spring.context;

import com.acme.biz.zookeeper.distributedconfig.DistributedConfigDataBase;
import com.acme.biz.zookeeper.spring.core.env.DistributedConfigPropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.lang.NonNull;

public class DistributedConfigEnvironmentLifecycle implements SmartLifecycle, EnvironmentAware {
    private ConfigurableEnvironment environment;
    private Boolean isConfigurableEnvironment = false;

    @Autowired
    private DistributedConfigDataBase distributedConfigDataBase;

    @Override
    public void start() {
        if (isConfigurableEnvironment) {
            MutablePropertySources propertySources = environment.getPropertySources();
            propertySources.addLast(
                    new DistributedConfigPropertySource(buildPropertySourceName(), distributedConfigDataBase));
        }
    }

    private String buildPropertySourceName() {
        return DistributedConfigPropertySource.PROPERTY_SOURCE_NAME + "-" + distributedConfigDataBase.getConfigNamespace() + "-" + distributedConfigDataBase.getApplicationName();
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        if (environment instanceof ConfigurableEnvironment configurableEnvironment) {
            this.environment = configurableEnvironment;
            this.isConfigurableEnvironment = true;
        }
    }
}
