package com.gpsy.externalApis.musiXmatchApi.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class MusiXmatchConfig {

    @Value("${musiXmatch.api.endpoint}")
    private String apiEndpointRoot;

    @Value("${musiXmatch.api.key}")
    private String apiKey;
}
