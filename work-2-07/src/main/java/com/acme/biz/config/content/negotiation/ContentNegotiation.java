package com.acme.biz.config.content.negotiation;

import com.acme.biz.config.domain.ConfigContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.acme.biz.config.domain.ConfigContentType.JSON;
import static com.acme.biz.config.domain.ConfigContentType.PROPERTIES;
import static com.acme.biz.config.domain.ConfigContentType.XML;
import static com.acme.biz.config.domain.ConfigContentType.YAML;

public class ContentNegotiation {


    public Map<String, ?> readString(String content, ConfigContentType configContentType) {
        ObjectMapper objectMapper = DICTIONARY.get(configContentType).get();
        try {
            return objectMapper.readValue(content, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String writeMap(Map<String, ?> content, ConfigContentType configContentType) {
        ObjectMapper objectMapper = DICTIONARY.get(configContentType).get();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(content);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    static Map<ConfigContentType, Supplier<ObjectMapper>> DICTIONARY = new HashMap<>();

    static {
        JsonMapper jsonMapper = new JsonMapper();
        YAMLMapper yamlMapper = new YAMLMapper();
        XmlMapper xmlMapper = new XmlMapper();
        JavaPropsMapper javaPropsMapper = new JavaPropsMapper();

        DICTIONARY.put(JSON, () -> jsonMapper);
        DICTIONARY.put(YAML, () -> yamlMapper);
        DICTIONARY.put(XML, () -> xmlMapper);
        DICTIONARY.put(PROPERTIES, () -> javaPropsMapper);
    }

}
