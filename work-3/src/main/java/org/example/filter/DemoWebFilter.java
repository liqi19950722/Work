package org.example.filter;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.web.reactive.filter.OrderedWebFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.*;

public class DemoWebFilter implements OrderedWebFilter, ApplicationListener<ContextRefreshedEvent>, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(DemoWebFilter.class);
    private List<HandlerMapping> handlerMappings;
    private BulkheadConfig config;
    private Map<Method, Bulkhead> methodBulkheadsMapping;

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        return Flux.fromIterable(this.handlerMappings)
                .concatMap(mapping -> mapping.getHandler(exchange))
                .next()
                .switchIfEmpty(chain.filter(exchange))
                .<Object>flatMap(handler -> {
                    log.info("handler:{}", handler);
                    if (handler instanceof HandlerMethod handlerMethod) {
                        Method method = handlerMethod.getMethod();
                        return Mono.just(methodBulkheadsMapping.get(method));
                    }
                    return Mono.empty();
                })
                .switchIfEmpty(chain.filter(exchange))
                .ofType(Bulkhead.class)
                .flatMap(bulkhead -> {
                    log.info("bulkhead:{}", bulkhead);
                    bulkhead.acquirePermission();
                    return chain.filter(exchange).transformDeferred(call -> call.doOnEach(signal -> {
                        Throwable throwable = signal.getThrowable();
                        if (throwable == null) {
                            bulkhead.releasePermission();
                        }
                    }));
                })
                ;
    }


    private <R> Mono<R> createNotFoundError() {
        return Mono.defer(() -> {
            Exception ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "No matching handler");
            return Mono.error(ex);
        });
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {


        ApplicationContext context = event.getApplicationContext();

        Map<String, HandlerMapping> mappingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                context, HandlerMapping.class, true, false);

        ArrayList<HandlerMapping> mappings = new ArrayList<>(mappingBeans.values());
        AnnotationAwareOrderComparator.sort(mappings);
        this.handlerMappings = Collections.unmodifiableList(mappings);
        log.info("{}", handlerMappings);

        this.methodBulkheadsMapping = new HashMap<>();
        handlerMappings.forEach(handlerMapping -> {
            if (handlerMapping instanceof RequestMappingInfoHandlerMapping requestMappingInfoHandlerMapping) {
                Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingInfoHandlerMapping.getHandlerMethods();
                handlerMethods.forEach((requestMappingInfo, handlerMethod) -> {
                    log.info("{}", requestMappingInfo);
                    Method method = handlerMethod.getMethod();
                    String resourceName = requestMappingInfo.toString();
                    methodBulkheadsMapping.put(method, Bulkhead.of(resourceName, config));
                });
            }
        });


    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.config = BulkheadConfig.custom().build();
    }
}
