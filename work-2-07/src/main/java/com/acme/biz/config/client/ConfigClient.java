package com.acme.biz.config.client;

import com.acme.biz.config.domain.Config;
import com.acme.biz.config.domain.ConfigContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConfigClient {

    private OkHttpClient client;
    private final String url;

    private final String clientId;

    public ConfigClient(String url) {
        this.client = new OkHttpClient();
        this.url = url;
        this.clientId = UUID.randomUUID().toString();
    }

    public Config getConfig(String configKey, ConfigContentType contentType) {
        Map<String, Object> uriVariables = buildUriVariables(configKey);

        Request request = new Request.Builder()
                .url(UriComponentsBuilder.fromUriString(url)
                        .pathSegment("config", "{configKey}")
                        .queryParam("format", contentType)
                        .uriVariables(uriVariables).build().toUriString())
                .build();
        try (Response response = client.newCall(request).execute()) {
            return new ObjectMapper().readValue(response.body().string(), Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static Map<String, Object> buildUriVariables(String configKey) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("configKey", configKey);
        return uriVariables;
    }

    public String unsubscribe(String configKey) {
        Map<String, Object> uriVariables = buildUriVariables(configKey);

        Request request = new Request.Builder()
                .url(UriComponentsBuilder.fromUriString(url)
                        .pathSegment("config", "unSubscribe", "{configKey}")
                        .queryParam("clientId", clientId)
                        .uriVariables(uriVariables).build().toUriString())
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void subscribe(String configKey) {
        Map<String, Object> uriVariables = buildUriVariables(configKey);
        var factory = EventSources.createFactory(client);
        Request request = new Request.Builder()
                .url(UriComponentsBuilder.fromUriString(url)
                        .pathSegment("config","subscribe","{configKey}")
                        .queryParam("clientId", clientId)
                        .uriVariables(uriVariables).build().toUriString())
                .build();
        var eventSource = factory.newEventSource(request, new EventSourceListener() {
            @Override
            public void onClosed(@NotNull EventSource eventSource) {
                System.out.println("onClosed");
            }

            @Override
            public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
                try {
                    var config = new ObjectMapper().readValue(data, Config.class);
                    System.out.println(config.version());
                    System.out.println(config.configBodies());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                System.out.println("onFailure");
            }

            @Override
            public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
                System.out.println("onOpen");
            }
        });
        eventSource.request();
    }
}
