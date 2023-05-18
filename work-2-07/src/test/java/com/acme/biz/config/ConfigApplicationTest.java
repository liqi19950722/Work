package com.acme.biz.config;

import com.acme.biz.config.domain.ConfigBody;
import com.acme.biz.config.domain.ConfigContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConfigApplicationTest {


    @Test
    public void should_(@Autowired WebTestClient webClient) {

        var config = new ConfigBody(content, ConfigContentType.JSON, true);
        webClient
                .post().uri("/config/test-key")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(config), ConfigBody.class)
                .exchange()
                .expectStatus().isOk();

        System.out.println(webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/config/test-key")
                        .queryParam("format", "json")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).returnResult());
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
}
