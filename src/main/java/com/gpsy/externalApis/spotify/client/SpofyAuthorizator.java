package com.gpsy.externalApis.spotify.client;

import com.gpsy.externalApis.spotify.config.SpotifyConfig;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@EnableScheduling
public class SpofyAuthorizator {

    private final SpotifyApi spotifyApi;

    @Autowired
    public SpofyAuthorizator(SpotifyConfig spotifyConfig) {
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
            System.out.println(authorizationCodeCredentials.getAccessToken());
        } catch(IOException | SpotifyWebApiException e) {
            System.out.println("Spotify exception: " + e.getMessage());
        }
    }

    public SpotifyApi getSpotifyApi() {
        return spotifyApi;
    }
}
