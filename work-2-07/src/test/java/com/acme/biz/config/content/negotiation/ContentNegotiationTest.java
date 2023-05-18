package com.acme.biz.config.content.negotiation;

import com.acme.biz.config.domain.ConfigContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static com.acme.biz.config.domain.ConfigContentType.JSON;
import static com.acme.biz.config.domain.ConfigContentType.PROPERTIES;
import static com.acme.biz.config.domain.ConfigContentType.XML;
import static com.acme.biz.config.domain.ConfigContentType.YAML;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ContentNegotiationTest {


    ContentNegotiation contentNegotiation;

    @BeforeEach
    public void setup() {
        contentNegotiation = new ContentNegotiation();
    }


    @ParameterizedTest(name = "should read properties by {1}")
    @MethodSource(value = "contentArgs")
    public void should_read_properties(String contentString, ConfigContentType configContentType) {
        Map<String, ?> content = contentNegotiation.readString(contentString, configContentType);
        assertContent(content);
    }

    static Stream<Arguments> contentArgs() {
        return Stream.of(
                arguments(contentJSON, JSON),
                arguments(contentYAML, YAML),
                arguments(contentXML, XML),
                arguments(contentPROPERTIES, PROPERTIES)
        );
    }


    private static void assertContent(Map<String, ?> content) {
        assertNotNull(content);
        Map<String, Object> user = (Map<String, Object>) content.get("user");
        assertNotNull(user);
        assertEquals("张三", user.get("name"));
        assertEquals("18", String.valueOf(user.get("age")));
    }

    static String contentJSON = """
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
    static String contentYAML = """
            ---
            user:
              name: "张三"
              age: 18
              phones:
              - "iPhone"
              - "Xiaomi"
              - "Huawei"
              cars:
              - name: "五菱宏光"
                carId: "京A888888"
              - name: "Model Y"
                carId: "沪A888888"
              address:
                province: "浙江省"
                city: "杭州市"
                area: "上城区"
            """;
    static String contentXML = """
            <LinkedHashMap>
              <user>
                <name>张三</name>
                <age>18</age>
                <phones>iPhone</phones>
                <phones>Xiaomi</phones>
                <phones>Huawei</phones>
                <cars>
                  <name>五菱宏光</name>
                  <carId>京A888888</carId>
                </cars>
                <cars>
                  <name>Model Y</name>
                  <carId>沪A888888</carId>
                </cars>
                <address>
                  <province>浙江省</province>
                  <city>杭州市</city>
                  <area>上城区</area>
                </address>
              </user>
            </LinkedHashMap>
            """;
    static String contentPROPERTIES = """
            user.name=\\u5F20\\u4E09
            user.age=18
            user.phones.1=iPhone
            user.phones.2=Xiaomi
            user.phones.3=Huawei
            user.cars.1.name=\\u4E94\\u83F1\\u5B8F\\u5149
            user.cars.1.carId=\\u4EACA888888
            user.cars.2.name=Model Y
            user.cars.2.carId=\\u6CAAA888888
            user.address.province=\\u6D59\\u6C5F\\u7701
            user.address.city=\\u676D\\u5DDE\\u5E02
            user.address.area=\\u4E0A\\u57CE\\u533A
            """;

}
