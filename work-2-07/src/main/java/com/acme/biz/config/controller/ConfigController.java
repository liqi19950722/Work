package com.acme.biz.config.controller;

import com.acme.biz.config.domain.Config;
import com.acme.biz.config.domain.ConfigBody;
import com.acme.biz.config.domain.ConfigContentType;
import com.acme.biz.config.event.ConfigEventListener;
import com.acme.biz.config.event.ConfigUpdateEvent;
import com.acme.biz.config.event.SpringConfigEventListener;
import com.acme.biz.config.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping(value = "/config")
public class ConfigController {

    @Autowired
    ConfigRepository repository;

    @GetMapping(value = "/{configKey}")
    public Config getConfig(@PathVariable(value = "configKey") String configKey,
                            @RequestParam("format") String format) {
        return repository.getConfig(configKey, ConfigContentType.parse(format));
    }

    @PostMapping(value = "/{configKey}")
    public void createConfig(@PathVariable(value = "configKey") String configKey,
                             @RequestBody ConfigBody configBody) {
        repository.createConfig(configKey, configBody);
    }

    @DeleteMapping(value = "/{configKey}")
    public void deleteConfig(@PathVariable(value = "configKey") String configKey) {
        repository.deleteConfig(configKey);
    }

    @PutMapping(value = "/{configKey}")
    public ConfigBody putConfig(@PathVariable(value = "configKey") String configKey,
                                @RequestBody ConfigBody configBody) {
        return repository.putConfig(configKey, configBody);
    }

    @GetMapping(value = "/subscribe/{configKey}")
    public SseEmitter subscribeConfig(@PathVariable(value = "configKey") String configKey,
                                      @RequestParam("clientId") String clientId) throws IOException {
        var sseEmitter = new SseEmitter();
        applicationEventMulticaster.addApplicationListener(new SpringConfigEventListener(clientId, configKey,
                new ConfigEventListener() {
                    @Override
                    public void onConfigUpdateEvent(ConfigUpdateEvent configUpdateEvent) {
                        try {
                            sseEmitter.send(configUpdateEvent.getSource(), MediaType.APPLICATION_JSON);
                        } catch (IOException e) {
                            sseEmitter.completeWithError(e);
                            throw new RuntimeException(e);
                        }
                    }
                    @Override
                    public void unregister() {
                        sseEmitter.complete();
                    }
                }));
        return sseEmitter;
    }

    @GetMapping(value = "/unSubscribe/{key}")
    public ResponseEntity<?> unSubscribeConfig(@PathVariable(value = "configKey") String configKey,
                                               @RequestParam("clientId") String clientId) throws IOException {
        applicationEventMulticaster.removeApplicationListeners(applicationListener -> {
            if (applicationListener instanceof SpringConfigEventListener springConfigEventListener
                    && springConfigEventListener.isSameClient(clientId, configKey)) {
                springConfigEventListener.unSubscribeConfigListener();
                return true;
            }
            return false;
        });
        return ResponseEntity.ok().build();
    }

    @Autowired
    ApplicationEventMulticaster applicationEventMulticaster;


    @PostMapping(value = "/test")
    public SseEmitter test() throws IOException {
        var sseEmitter = new SseEmitter();
        sseEmitter.send("test");
        sseEmitter.complete();
        return sseEmitter;
    }
}
