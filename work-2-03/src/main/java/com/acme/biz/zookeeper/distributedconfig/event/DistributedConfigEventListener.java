package com.acme.biz.zookeeper.distributedconfig.event;

import java.util.EventListener;

public interface DistributedConfigEventListener extends EventListener {

    default void onDistributedConfigReceived(DistributedConfigChangedEvent<?> distributedConfigEvent) {
    }
}
