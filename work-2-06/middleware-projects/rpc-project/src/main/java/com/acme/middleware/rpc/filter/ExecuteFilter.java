package com.acme.middleware.rpc.filter;

import com.acme.middleware.rpc.InvocationRequest;

public interface ExecuteFilter {

    default void processInvocationRequestBeforeExecute(InvocationRequest request) {
    }



    default void executeFail(Throwable cause) {
    }



    default void executeSuccess(Throwable cause) {
    }


}
