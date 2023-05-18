package com.acme.biz.config.domain;

public record ConfigBody(String content, ConfigContentType contentType, boolean isConfigured) {
}
