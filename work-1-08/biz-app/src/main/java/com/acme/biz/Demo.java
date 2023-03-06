package com.acme.biz;

import com.acme.biz.domain.User;
import com.acme.biz.interceptor.bytebuddy.ServiceLoggerInterceptor;
import com.acme.biz.service.UserService;
import com.acme.biz.service.impl.UserServiceImpl;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Pipe;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

public class Demo {
    static ClassLoader classLoader = Demo.class.getClassLoader();

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        UserService userService = new UserServiceImpl();

        ByteBuddy byteBuddy = new ByteBuddy();
        Class<? extends UserService> clazz = byteBuddy.subclass(UserService.class)
                .name(UserService.class.getName() + "ByteBuddy")
                .method(ElementMatchers.isDeclaredBy(UserService.class))
                .intercept(MethodDelegation.withDefaultConfiguration()
                        .withBinders(Pipe.Binder.install(Function.class))
                        .to(new ServiceLoggerInterceptor(userService)))
                .make()
                .load(classLoader)
                .getLoaded();

        UserService proxy = clazz.getDeclaredConstructor().newInstance();
        User user = new User();
        user.setId(1L);
        user.setUserName("张三");
        proxy.registerUser(user);
    }

}
