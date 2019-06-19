package com.gpsy.spotify.client;

import com.gpsy.domain.TrackDto;
import com.sun.jndi.toolkit.url.Uri;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.browse.GetListOfCategoriesRequest;
import com.wrapper.spotify.requests.data.personalization.GetUsersTopArtistsAndTracksRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import com.wrapper.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SpotifyClient {

    @Autowired
    SpotifyConfig spotifyConfig;

    @Autowired
    private RestTemplate restTemplate;

//    ---------------------------------------code refresh----------------------------------------

//    -------------------------------------------------------------------

    private boolean isExecuted = false;

//    public URI retrieveUri() {
//        final SpotifyApi spotifyApi = new SpotifyApi.Builder()
//                .setClientId("cabce243b2e7482cbaf0dc2c6f88a78b")
//                .setClientSecret("e92ce3f2921f46f0ae3d3c4cb894b2b5")
//                .setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:8080"))
//                .build();
////
//        final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
//                .scope("user-read-recently-played user-follow-read user-follow-modify playlist-modify-private playlist-modify-public playlist-read-collaborative playlist-read-private streaming app-remote-control user-library-read user-library-modify user-read-email user-read-private user-read-birthdate user-top-read user-read-currently-playing user-read-playback-state user-modify-playback-state")
//                .build();
////
//        return authorizationCodeUriRequest.execute();
//    }

    public List<String> getSpotifyTracks() {
        final List<String> tracks = new ArrayList<>();





        try {
            if(!isExecuted) {

                isExecuted = true;
            }



            final GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = SpofyAuthorizator.spotifyApi.getCurrentUsersProfile().build();
            User user = getCurrentUsersProfileRequest.execute();

            final GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = SpofyAuthorizator.spotifyApi.getListOfCurrentUsersPlaylists().build();
            final Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfCurrentUsersPlaylistsRequest.execute();

            final GetCurrentUsersRecentlyPlayedTracksRequest getCurrentUsersRecentlyPlayedTracksRequest = SpofyAuthorizator.spotifyApi.getCurrentUsersRecentlyPlayedTracks().build();

            final GetListOfCategoriesRequest getListOfCategoriesRequest = SpofyAuthorizator.spotifyApi.getListOfCategories()
                    .limit(50)
                    .build();

            GetUsersTopTracksRequest getUsersTopTracksRequest = SpofyAuthorizator.spotifyApi.getUsersTopTracks()
                    .build();
            final Paging<Track> trackPaging = getUsersTopTracksRequest.execute();
            Track[] tracksArray = trackPaging.getItems();

            for (Track track : tracksArray) {
                Double duration = track.getDurationMs()/1000.0/60.0;
                String duration2 = duration.toString().substring(0,4);
                String artists = "";
                    for(ArtistSimplified artist: track.getArtists()) {
                        artists += artist.getName();
                    }
                tracks.add("TITLE: [" + track.getName() + track.getId() + "], ARTISTS: [" + artists + "], POPULARITY: [" + track.getPopularity() + "] ****************************************************************************************************************************");
            }
                tracks.add("Username: " + user.getDisplayName() + "User ID: " + user.getId());

                for(PlaylistSimplified playlist : playlistSimplifiedPaging.getItems()) {
                    tracks.add(playlist.getName() + " " + playlist.getTracks().getTotal());
                }

                final PagingCursorbased<PlayHistory> playHistoryPagingCursorbased = getCurrentUsersRecentlyPlayedTracksRequest.execute();

                for(PlayHistory playHistory: playHistoryPagingCursorbased.getItems()) {
                    tracks.add(playHistory.getTrack().getName() + ", Played at: " + playHistory.getPlayedAt().toString());
                }

                final Paging<Category> categoryPaging = getListOfCategoriesRequest.execute();
                tracks.add("Categories: ***************************************************************************************************************");
                for(Category category: categoryPaging.getItems()) {
                    tracks.add(category.getName());
                }
            } catch(IOException | SpotifyWebApiException e){
                System.out.println("error: " + e.getMessage());
            }


            return tracks;
//            GetUsersTopArtistsAndTracksRequest getUsersTopArtistsAndTracksRequest = spotifyApi.getUsersTopArtistsAndTracks(ModelObjectType.ARTIST).build();
//            final Paging<Artist> artistPaging = getUsersTopArtistsAndTracksRequest.execute();
//            Artist[] artistArray = artistPaging.getItems();
//
//            for (Artist artist : artistArray) {
//                tracks.add("TITLE: [" + artist.getName() + ", [" + artist.getPopularity() + "] ****************************************************************************************************************************");
//            }
//
//        } catch(IOException | SpotifyWebApiException e){
//            System.out.println("error: " + e.getMessage());
//        }
//
//        return tracks;
        }













//        List<String> tracks = new ArrayList<>();
////
//        final SpotifyApi spotifyApi = new SpotifyApi.Builder()
//                .setClientId(spotifyConfig.getClientId())
//                .setClientSecret(spotifyConfig.getClientSecret())
//
//                .setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:8080"))
//                .build();
//
//        final AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode("AQBmHYA3BiUULloovcdgittf2z7XPRTMwowmbT1SCurN9YzjaSvUh53jAOWfX4spmzta5N8cK6TnjacYuWF9PEd6lkFv-uKd-fvK3lc-ai1mDnr8voMtVMcU5ZlMOLL4LJX4o8yZ5D5P02fZY_3GvSCvQMfzW-P69fv-14d93mQ4MDbP2dTapk6MwXckA-d_CNB5RyRyV7LlxWa6X9JGQTERKsHHifob_uzqsFupI0pEK3aofBTyGNHSg_H_jIAGovaCpLTagP7GPY3r5nPk-pnY96607gKThtb1iE0AIUJDiP3vHVBe-4qMTMovjvd5QZLq4bzTEctlQRt93JN9xC2JbjlfvvTJ2rPDNBBfDHEG-312BVCpT3tQdQasSY3lhgOGqEBbpbvtpbafg3QOtDkGplLBG6ltZ7pA3tbB3RCiBc9uhQxlbnNv1gEBo-_6x1_UYPaxeDAw3ri64tT5OeNxF8xWKDwffZhDJx33YzqWFGiztJPqtRVqXGgC8tpxcWen2vEdJ0IYRojD-6pW2TEVLqgBrrNokKNqOgXPS9Etm7mwvM5J4QzcAOlPveMCv82ZM-TG6ss9aVM1HmTk--o5eVFC1Xuj35eW5bTgmr3uFOJlmKRMIFBVEa-BEYHp2NKSVr8MYnA0oSuxScuRP__z2pazCes5pzLlyN281g6nFk4yPSIYG1enuqA4g2cZaDanssg")
//                .build();
//////
//////
//        try {
//            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
//            System.out.println(authorizationCodeCredentials.getAccessToken());
//            // Set access and refresh token for further "spotifyApi" object usage
//            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
//            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
//
//            System.out.println(spotifyApi.getAccessToken());
//            System.out.println(spotifyApi.getRefreshToken());
//
//        } catch (IOException | SpotifyWebApiException e) {
//            System.out.println("Error: " + e.getMessage());
//        }

//        try {
//            spotifyApi.setAccessToken("BQC27M6SdVkg4d69YKi2Tp0qXFsVQWUEsPRj9hXQcXTDeTjNBBrVUSiQ_vuNbgznLjme2atm7zX-b91VAb7vRtZoJbzQIbKSfXPT7wWj5oN8yKNVwCqJTs0nUVs-jHbbrLx92Rv3V7zbGod7L3eMmAeacJM1TYAkjQdtObUcZZMAR7oupU9vlwOoK1rGMpILwB2ey0br_bJGMaGI7J0_k1EiP5DUISUUQ15Om8AoF_hJaguVDndmMSa6QUki6aTEZ4xUVHukYbknEeYB");
//            GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi.getUsersTopTracks()
//                    .build();
//            final Paging<DbTrack> trackPaging = getUsersTopTracksRequest.execute();
//            DbTrack[] tracksArray = trackPaging.getItems();
//
//            for(DbTrack track: tracksArray) {
//                tracks.add(track.getName() + ", " + track.getPopularity() + ", " + track.getTrackNumber());
//            }
//        } catch(IOException | SpotifyWebApiException e) {
//            System.out.println("Error: " + e.getCause().getMessage());
//        }
//        return tracks;

}
