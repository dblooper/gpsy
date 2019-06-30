package com.gpsy.externalApis.auddApi.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AuddConfig {

    @Value("${audd.api.endpoint}")
    private String auddApiEndpointRoot;
}
