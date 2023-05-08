package com.acme.middleware.rpc.server.filter;

import com.acme.middleware.rpc.InvocationRequest;

public interface MethodInvokeFilter {
    default void beforeServiceMethodInvoke(InvocationRequest request) {
    }

    default void afterServiceMethodInvoke(Object result) {
    }
}
