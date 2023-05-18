package com.acme.biz.config.repository;

import com.acme.biz.config.domain.Config;
import com.acme.biz.config.domain.ConfigBody;
import com.acme.biz.config.event.ConfigCreateEvent;
import com.acme.biz.config.event.ConfigDeleteEvent;
import com.acme.biz.config.event.ConfigEventPublisher;
import com.acme.biz.config.event.ConfigUpdateEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.Objects;

import static com.acme.biz.config.domain.ConfigContentType.JSON;
import static com.acme.biz.config.domain.ConfigContentType.PROPERTIES;
import static com.acme.biz.config.domain.ConfigContentType.TEXT;
import static com.acme.biz.config.domain.ConfigContentType.XML;
import static com.acme.biz.config.domain.ConfigContentType.YAML;
import static com.acme.biz.config.repository.ConfigRepositoryTest.Constant.CREATE_KEY;
import static com.acme.biz.config.repository.ConfigRepositoryTest.Constant.DELETE_KEY;
import static com.acme.biz.config.repository.ConfigRepositoryTest.Constant.TEST_KEY;
import static com.acme.biz.config.repository.ConfigRepositoryTest.Constant.UPDATE_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ConfigRepositoryTest {

    ConfigEventPublisher publisher;
    ConfigRepository repository;

    ConfigBody test;
    ConfigBody delete;
    ConfigBody update;


    @BeforeEach
    public void setup() {
        publisher = mock(ConfigEventPublisher.class);

        repository = new ConfigRepository(publisher);
        test = new ConfigBody("", TEXT, true);
        delete = new ConfigBody("", TEXT, true);
        update = new ConfigBody("", TEXT, true);
        repository.repository.clear();
        repository.createConfig(TEST_KEY, test);
        repository.createConfig(DELETE_KEY, delete);
        repository.createConfig(UPDATE_KEY, update);
    }

    private ConfigBody updateConfig() {
        var oldValue = (ConfigBody) repository.putConfig(UPDATE_KEY, new ConfigBody("updated", TEXT, true));
        return oldValue;
    }

    private Deque<Config> deleteConfig() {
        var deleted = repository.deleteConfig(DELETE_KEY);
        return deleted;
    }

    private void createConfig() {
        repository.createConfig(CREATE_KEY, new ConfigBody("test_create", TEXT, true));
    }


    @Nested
    class Basic {


        @Test
        public void should_add_config() {
            createConfig();

            assertTrue(repository.repository.containsKey(CREATE_KEY));
        }

        @Test
        public void should_get_config_via_config_key() {
            var config = repository.getConfig(TEST_KEY);
            assertSame(test, config.configBodies().get(0));
        }

        @Test
        public void should_delete_config_via_config_key() {
            Deque<Config> deleted = deleteConfig();
            assertSame(delete, deleted.pop().configBodies().get(0));
        }

        @Test
        public void should_update_config_via_config_key() {
            ConfigBody oldValue = updateConfig();

            var updated = repository.getConfig(UPDATE_KEY);
            assertEquals(update.content(), oldValue.content());
            assertEquals("updated", updated.configBodies().get(0).content());
        }
    }


    @Nested
    class VersionControl {

        @Test
        public void should_return_version_number_when_created_config() {
            createConfig();
            var configDeque = repository.repository.get(CREATE_KEY);
            assertNotNull(configDeque.peek());
            assertEquals(1, configDeque.peek().version());
        }

        @Test
        public void should_store_history_config_when_updated() {
            updateConfig();
            var configDeque = repository.repository.get(UPDATE_KEY);
            assertNotNull(configDeque.peek());
            assertEquals(2, configDeque.peek().version());
            assertTrue(configDeque.stream().anyMatch(config -> Objects.equals(config.configBodies().get(0).content(), update.content())));
            assertTrue(configDeque.stream().anyMatch(config -> Objects.equals(config.configBodies().get(0).content(), "updated")));
        }

    }

    @Nested
    class ConfigEvents {

        @Test
        public void should_publish_ConfigCreateEvent_when_created_config() {
            reset(publisher);
            createConfig();

            verify(publisher, times(1)).publishEvent(isA(ConfigCreateEvent.class));
        }

        @Test
        public void should_publish_ConfigDeletedEvent_when_deleted_config() {
            reset(publisher);
            deleteConfig();

            verify(publisher, times(1)).publishEvent(isA(ConfigDeleteEvent.class));
        }

        @Test
        public void should_publish_ConfigUpdateEvent_when_update_config() {
            reset(publisher);
            updateConfig();

            verify(publisher, times(1)).publishEvent(isA(ConfigUpdateEvent.class));
        }

    }

    @Nested
    class ContentNegotiation {

        String contentJSON = """
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
        String contentYAML = """
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
        String contentXML = """
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
        String contentPROPERTIES = """
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

        @BeforeEach
        public void setup() {
            repository.createConfig("content-negotiation",
                    new ConfigBody(contentJSON, JSON, true));
        }

        @Test
        public void should_get_config_with_json_format() throws JsonProcessingException {
            var config = repository.getConfig("content-negotiation", JSON);
            assertThat(contentJSON).containsIgnoringWhitespaces(config.configBodies().get(0).content());
            assertEquals(JSON, config.configBodies().get(0).contentType());
        }

        @Test
        public void should_get_config_with_yaml_format() throws JsonProcessingException {
            var config = repository.getConfig("content-negotiation", YAML);
            assertThat(contentYAML).containsIgnoringWhitespaces(config.configBodies().get(0).content());
            assertEquals(YAML, config.configBodies().get(0).contentType());
        }

        @Test
        public void should_get_config_with_xml_format() throws JsonProcessingException {
            var config = repository.getConfig("content-negotiation", XML);
            assertThat(contentXML).containsIgnoringWhitespaces(config.configBodies().get(0).content());
            assertEquals(XML, config.configBodies().get(0).contentType());
        }

        @Test
        public void should_get_config_with_properties_format() throws JsonProcessingException {
            var config = repository.getConfig("content-negotiation", PROPERTIES);
            assertThat(contentPROPERTIES).containsIgnoringWhitespaces(config.configBodies().get(0).content());
            assertEquals(PROPERTIES, config.configBodies().get(0).contentType());
        }

    }

    interface Constant {
        String DELETE_KEY = "delete";
        String TEST_KEY = "test";
        String CREATE_KEY = "create";
        String UPDATE_KEY = "update";
    }
}
