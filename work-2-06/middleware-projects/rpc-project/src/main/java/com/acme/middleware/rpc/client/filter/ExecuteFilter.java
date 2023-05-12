package com.acme.middleware.rpc.client.filter;

import com.acme.middleware.rpc.InvocationRequest;

public interface ExecuteFilter {

    default void processInvocationRequestBeforeExecute(InvocationRequest request) {
    }



    default void onFail(Throwable cause) {
    }


    default void onSuccess(Object result) {
    }


}
