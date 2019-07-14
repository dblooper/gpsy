package com.gpsy.externalApis.spotify.client;

import com.gpsy.externalApis.spotify.config.SpotifyConfig;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@EnableScheduling
public class SpotifyAuthorizator {

    private final SpotifyApi spotifyApi;
    private static final Logger LOGGER = LoggerFactory.getLogger(SpotifyAuthorizator.class);

    @Autowired
    public SpotifyAuthorizator(SpotifyConfig spotifyConfig) {
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(spotifyConfig.getClientId())
                .setClientSecret(spotifyConfig.getClientSecret())
                .setRefreshToken(spotifyConfig.getRefreshToken())
                .build();
    }

    @Scheduled(fixedRate = 3500000)
    public void authorize() {

        try {
            final AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh().build();
            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
        } catch(IOException | SpotifyWebApiException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public SpotifyApi getSpotifyApi() {
        return spotifyApi;
    }
}
