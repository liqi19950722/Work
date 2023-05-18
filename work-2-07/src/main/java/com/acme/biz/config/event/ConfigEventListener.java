package com.acme.biz.config.event;

import java.util.EventListener;

public interface ConfigEventListener extends EventListener {

    default void onConfigUpdateEvent(ConfigUpdateEvent configUpdateEvent){};

    default void onConfigCreateEvent(ConfigCreateEvent configCreateEvent) {};

    default void onConfigDeleteEvent(ConfigDeleteEvent configDeleteEvent){};

    default void unregister(){};
}
