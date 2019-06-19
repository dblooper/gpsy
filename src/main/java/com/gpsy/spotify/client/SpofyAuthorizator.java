package com.gpsy.spotify.client;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@EnableScheduling
@Getter
@NoArgsConstructor
public class SpofyAuthorizator {

    private AuthorizationCodeCredentials authorizationCodeCredentials;

    public static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId("cabce243b2e7482cbaf0dc2c6f88a78b")
            .setClientSecret("e92ce3f2921f46f0ae3d3c4cb894b2b5")
            .setRefreshToken("AQBIWEuQaX-4RtqBorRwDkkpGfC7M6d5T4SC4yYHQqbiqOifICQ8FixJGhii5BqNtURVF1uTmtMak3FdjbP-O90vCNZ3DtmAWsY_Xli_tH-cnQ7nJ8xvgKus-kyb7iCG9sNFog")
            .build();

    @Scheduled(fixedRate = 3500000)
    public void authorize() {
        try {
            final AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh().build();
            authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            System.out.println(authorizationCodeCredentials.getAccessToken());
        } catch(IOException | SpotifyWebApiException e) {
            System.out.println("Spotify exception: " + e.getMessage());
        }
    }
}
