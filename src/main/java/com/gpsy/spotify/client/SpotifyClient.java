package com.gpsy.spotify.client;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import com.wrapper.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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


//
//final GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = SpofyAuthorizator.spotifyApi.getCurrentUsersProfile().build();
//    User user = getCurrentUsersProfileRequest.execute();
//
//    final GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = SpofyAuthorizator.spotifyApi.getListOfCurrentUsersPlaylists().build();
//    final Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfCurrentUsersPlaylistsRequest.execute();
//
//    final GetCurrentUsersRecentlyPlayedTracksRequest getCurrentUsersRecentlyPlayedTracksRequest = SpofyAuthorizator.spotifyApi.getCurrentUsersRecentlyPlayedTracks().build();
//
//    final GetListOfCategoriesRequest getListOfCategoriesRequest = SpofyAuthorizator.spotifyApi.getListOfCategories()
//            .limit(50)
//            .build();

//    }

    public List<Track> getSpotifyPopularTracks() {
        final List<Track> tracks = new ArrayList<>();

        try {
            GetUsersTopTracksRequest getUsersTopTracksRequest = SpofyAuthorizator.spotifyApi.getUsersTopTracks()
                    .limit(20)
                    .build();
            final Paging<Track> trackPaging = getUsersTopTracksRequest.execute();
            Track[] tracksArray = trackPaging.getItems();
            tracks.addAll(Arrays.asList(tracksArray));
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Spotify Client exception: " + e.getMessage());
        }

        return tracks;
    }

    public List<PlayHistory> getSppotifyRecentPlayedTracks() {
        final List<PlayHistory> recentTracks = new ArrayList<>();

        try {
            GetCurrentUsersRecentlyPlayedTracksRequest getCurrentUsersRecentlyPlayedTracksRequest = SpofyAuthorizator.spotifyApi.getCurrentUsersRecentlyPlayedTracks()
                    .limit(10)
                    .build();
            final PagingCursorbased<PlayHistory> historyOfPlays = getCurrentUsersRecentlyPlayedTracksRequest.execute();
            PlayHistory[] tracksArray = historyOfPlays.getItems();
            recentTracks.addAll(Arrays.asList(tracksArray));
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Spotify Client exception: " + e.getMessage());
        }

        return recentTracks;
    }
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
//            final Paging<DbPopularTrack> trackPaging = getUsersTopTracksRequest.execute();
//            DbPopularTrack[] tracksArray = trackPaging.getItems();
//
//            for(DbPopularTrack track: tracksArray) {
//                tracks.add(track.getName() + ", " + track.getPopularity() + ", " + track.getTrackNumber());
//            }
//        } catch(IOException | SpotifyWebApiException e) {
//            System.out.println("Error: " + e.getCause().getMessage());
//        }
//        return tracks;

}
