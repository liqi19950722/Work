package com.acme.biz.interceptor.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class ControllerLoggerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ServiceLoggerInterceptor.class);

    public ControllerLoggerInterceptor() {
    }

    @RuntimeType
    public Object log(@SuperCall Callable<Object> supercall, @AllArguments Object[] args, @Origin Method method)
            throws Exception {
        Arrays.stream(args).forEach(arg -> logger.info("{} 调用参数: {}", method, arg));
        try {
            return supercall.call();
        } finally {
        }
    }
}
