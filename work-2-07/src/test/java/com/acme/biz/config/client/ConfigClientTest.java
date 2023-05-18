package com.acme.biz.config.client;

import com.acme.biz.config.domain.ConfigBody;
import com.acme.biz.config.domain.ConfigContentType;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.acme.biz.config.domain.ConfigContentType.JSON;
import static com.acme.biz.config.domain.ConfigContentType.PROPERTIES;
import static com.acme.biz.config.domain.ConfigContentType.XML;
import static com.acme.biz.config.domain.ConfigContentType.YAML;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConfigClientTest {
    @LocalServerPort
    int serverPort;
    @Autowired
    WebTestClient webClient;
    ConfigClient configClient;

    @BeforeEach
    public void setup() throws IOException {
        configClient = new ConfigClient("http://localhost:" + serverPort);
        var config = new ConfigBody(content, ConfigContentType.JSON, true);
        webClient
                .post().uri("/config/test-config")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(config), ConfigBody.class)
                .exchange()
                .expectStatus().isOk();
    }

    @AfterEach
    public void teardown() throws IOException {
        webClient
                .delete().uri("/config/test-config")
                .exchange()
                .expectStatus().isOk();
    }


    @ParameterizedTest(name = "get config via client format with {0}")
    @MethodSource("contentArgs")
    public void should_get_config_use_client(ConfigContentType contentType) {
        var config = configClient.getConfig("test-config", contentType);
        assertNotNull(config);
        assertEquals(1, config.version());
        assertEquals(1, config.configBodies().size());
        assertNotNull(config.configBodies().get(0));
    }

    @Test
    public void should_receive_sse() throws InterruptedException {
        configClient.subscribe("test-config");

        TimeUnit.SECONDS.sleep(1L);
        var updateConfig = new ConfigBody(updateContent, ConfigContentType.JSON, true);
        webClient
                .put().uri("/config/test-config")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateConfig), ConfigBody.class)
                .exchange()
                .expectStatus().isOk();

        TimeUnit.SECONDS.sleep(1L);

    }

    static Stream<Arguments> contentArgs() {
        return Stream.of(
                arguments(JSON),
                arguments(YAML),
                arguments(XML),
                arguments(PROPERTIES)
        );
    }

    String content = """
            {
                    "user": {
                        "name": "张三",
                        "age": 18,
                        "phones": ["iPhone", "Xiaomi", "Huawei"],
                        "cars":[
                            {
                                "name": "五菱宏光",
                                "carId":"京A888888"
                            },
                            {
                                "name": "Model Y",
                                "carId":"沪A888888"
                            }
                        ],
                        "address": {
                            "province": "浙江省",
                            "city": "杭州市",
                            "area": "上城区"
                        }
                    }
                }
            """;

    String updateContent = """
            {
                    "user": {
                        "name": "李四",
                        "age":20,
                        "phones": ["iPhone", "Xiaomi", "Huawei"],
                        "cars":[
                            {
                                "name": "五菱宏光",
                                "carId":"京A888888"
                            },
                            {
                                "name": "Model Y",
                                "carId":"沪A888888"
                            }
                        ],
                        "address": {
                            "province": "浙江省",
                            "city": "杭州市",
                            "area": "上城区"
                        }
                    }
                }
            """;

}
