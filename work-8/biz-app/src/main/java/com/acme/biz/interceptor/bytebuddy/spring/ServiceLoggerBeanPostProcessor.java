package com.acme.biz.interceptor.bytebuddy.spring;

import com.acme.biz.configuration.ByteBuddyProperties;
import com.acme.biz.interceptor.bytebuddy.ControllerLoggerInterceptor;
import com.acme.biz.interceptor.bytebuddy.ServiceLoggerInterceptor;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Pipe;
import net.bytebuddy.matcher.ElementMatchers;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

public class ServiceLoggerBeanPostProcessor implements BeanPostProcessor, BeanClassLoaderAware, ApplicationContextAware {

    @Autowired
    private ByteBuddyProperties byteBuddyProperties;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        String packageName = bean.getClass().getPackageName();
        if (MergedAnnotations.from(bean.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).isPresent(Service.class)
                && packageName.startsWith(byteBuddyProperties.getLogging().getService())) {
            Class<?> superclass = bean.getClass().getInterfaces()[0]; // 假设只有一个 xxxService
            ByteBuddy byteBuddy = new ByteBuddy();
            Class<?> clazz = byteBuddy.subclass(superclass)
                    .name(superclass.getName() + "ByteBuddy")
                    .method(ElementMatchers.isDeclaredBy(superclass))
                    .intercept(MethodDelegation.withDefaultConfiguration()
                            .withBinders(Pipe.Binder.install(Function.class))
                            .to(new ServiceLoggerInterceptor(bean)))
                    .make()
                    .load(classLoader)
                    .getLoaded();
            try {
                Object proxy = clazz.getDeclaredConstructor().newInstance();
                applicationContext.getAutowireCapableBeanFactory().autowireBean(proxy);
                return proxy;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
            }
        }

        if (MergedAnnotations.from(bean.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).isPresent(Controller.class)
                && packageName.startsWith(byteBuddyProperties.getLogging().getController())) {
            Class<?> beanClass = bean.getClass();
            Class<?> clazz = new ByteBuddy().rebase(beanClass)
                    .name(beanClass.getName() + "ByteBuddy")
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(new ControllerLoggerInterceptor()))
                    .make()
                    .load(classLoader)
                    .getLoaded();
            try {
                Object proxy = clazz.getDeclaredConstructor().newInstance();
                applicationContext.getAutowireCapableBeanFactory().autowireBean(proxy);
                return proxy;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    private ClassLoader classLoader;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
