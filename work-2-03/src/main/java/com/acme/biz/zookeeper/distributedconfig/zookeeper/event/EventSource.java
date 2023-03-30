package com.acme.biz.zookeeper.distributedconfig.zookeeper.event;

import com.acme.biz.zookeeper.distributedconfig.event.EventContext;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener.Type;

public record EventSource(Type type, String propertyKey, String propertyValue)implements EventContext {
}
