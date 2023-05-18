package com.acme.biz.config.domain;

import java.util.Arrays;
import java.util.Objects;

public enum ConfigContentType {
    TEXT, JSON, YAML, XML, PROPERTIES;

    public static ConfigContentType parse(String format) {
        return Arrays.stream(values())
                .filter(type -> Objects.equals(String.valueOf(type), format.toUpperCase()))
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }
    
}
