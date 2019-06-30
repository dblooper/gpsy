package com.gpsy.externalApis.spotify.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SpotifyConfig {

    @Value("${spotify.app.refreshToken}")
    private String refreshToken;

    @Value("${spotify.app.id}")
    private String clientId;

    @Value("${spotify.app.secret}")
    private String clientSecret;

    @Value("${spotify.app.mail}")
    private String userMail;

    @Value("${spotify.api.userId}")
    private String userId;
}
