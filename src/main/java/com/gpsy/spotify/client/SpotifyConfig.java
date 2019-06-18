package com.gpsy.spotify.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
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
