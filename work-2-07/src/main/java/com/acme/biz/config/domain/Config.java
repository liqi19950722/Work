package com.acme.biz.config.domain;

import java.util.List;

public record Config(List<ConfigBody> configBodies, int version, long timestamp){

    public Config copy(ConfigBody configBody) {
        return new Config(List.of(configBody), version, timestamp);
    }
}
