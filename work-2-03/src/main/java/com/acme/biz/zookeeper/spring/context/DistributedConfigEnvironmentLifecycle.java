package com.acme.biz.zookeeper.spring.context;

import com.acme.biz.zookeeper.distributedconfig.DistributedConfigDataBase;
import com.acme.biz.zookeeper.spring.core.env.DistributedConfigPropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

import java.util.concurrent.atomic.AtomicBoolean;

public class DistributedConfigEnvironmentLifecycle implements SmartLifecycle, EnvironmentAware {
    private ConfigurableEnvironment environment;
    private Boolean isConfigurableEnvironment = false;

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    @Autowired
    private DistributedConfigDataBase distributedConfigDataBase;

    @Override
    public void start() {
        if (isConfigurableEnvironment) {
            environment.getPropertySources().addLast(
                    new DistributedConfigPropertySource(distributedConfigDataBase.buildPropertySourceName(), distributedConfigDataBase));
        }
        this.isRunning.lazySet(true);
    }

    @Override
    public void stop() {
        isRunning.compareAndSet(true, false);
    }

    @Override
    public boolean isRunning() {
        return isRunning.get();
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        if (environment instanceof ConfigurableEnvironment configurableEnvironment) {
            this.environment = configurableEnvironment;
            this.isConfigurableEnvironment = true;
        }
    }
}
