package com.acme.biz.zookeeper.distributedconfig.zookeeper.event;

import org.apache.curator.framework.recipes.cache.CuratorCacheListener.Type;

public record EventSource(Type type, String propertyKey, String propertyValue) {
}
