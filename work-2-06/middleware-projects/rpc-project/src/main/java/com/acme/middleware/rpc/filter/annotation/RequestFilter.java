package com.acme.middleware.rpc.filter.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequestFilter {
    String name() default "";
}
