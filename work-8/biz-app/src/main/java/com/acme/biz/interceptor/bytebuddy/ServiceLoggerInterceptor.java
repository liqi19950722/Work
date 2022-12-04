package com.acme.biz.interceptor.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.Pipe;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;

public class ServiceLoggerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ServiceLoggerInterceptor.class);
    private final Object object;

    public ServiceLoggerInterceptor(Object object) {
        this.object = object;
    }


    @RuntimeType
    public Object log(@Pipe Function<Object, Object> pipe, @AllArguments Object[] args, @Origin Method method)
            throws Exception {
        Arrays.stream(args).forEach(arg -> logger.info("{} 调用参数: {}", method, arg));
        try {
            return pipe.apply(object);
        } finally {
        }
    }
}
