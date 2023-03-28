package com.acme.biz.zookeeper.distributedconfig.event;

import java.util.Comparator;
import java.util.EventListener;

public interface DistributedConfigEventListener extends EventListener, Comparable<DistributedConfigEventListener> {

    Integer DEFAULT_ORDER = 0;
    default Integer order(){
        return DEFAULT_ORDER;
    }
    @Override
    default int compareTo(DistributedConfigEventListener o) {
        return Comparator.<Integer>naturalOrder().compare(order(), o.order());
    }
    default void onDistributedConfigReceived(DistributedConfigChangedEvent distributedConfigEvent) {
    }
}
