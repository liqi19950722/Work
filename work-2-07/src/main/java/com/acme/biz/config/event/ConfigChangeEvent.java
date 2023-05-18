package com.acme.biz.config.event;

import java.util.EventObject;

public abstract class ConfigChangeEvent extends EventObject {


    public ConfigChangeEvent(Object source) {
        super(source);
    }

    public abstract long getTimestamp();
}
