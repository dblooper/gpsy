package com.gpsy.spotify.client;

import com.gpsy.domain.DbMostFrequentTrack;
import com.gpsy.domain.DbUserPlaylist;
import com.gpsy.service.PersonalizationDbBasedService;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.special.SnapshotResult;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.browse.GetRecommendationsRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import com.wrapper.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;
import com.wrapper.spotify.requests.data.playlists.AddTracksToPlaylistRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsTracksRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SpotifyClient {

    @Autowired
    SpotifyConfig spotifyConfig;

    @Autowired
    private PersonalizationDbBasedService personalizationDbBasedService;

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

    public List<PlayHistory> getSpotifyRecentPlayedTracks() {
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

    public List<PlaylistSimplified> getUserPlaylists() {
        List<PlaylistSimplified> playlistsSimplified = new ArrayList<>();

        try {
            final GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = SpofyAuthorizator.spotifyApi.getListOfCurrentUsersPlaylists().build();
            final Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfCurrentUsersPlaylistsRequest.execute();

            playlistsSimplified.addAll(Arrays.asList(playlistSimplifiedPaging.getItems()));

        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Spotify Client exception: " + e.getMessage());
        }

        return playlistsSimplified;
    }

    public List<PlaylistTrack> getPlaylistTracks(String playlistId) {
        List<PlaylistTrack> playlistTracks = new ArrayList<>();

        try {
            final GetPlaylistsTracksRequest playlistsTracksRequest = SpofyAuthorizator.spotifyApi.getPlaylistsTracks(playlistId)
                    .limit(50)
                    .build();
            final Paging<PlaylistTrack> playlistTracksPaging = playlistsTracksRequest.execute();
            PlaylistTrack[] playlistTracksSpotify = playlistTracksPaging.getItems();
            playlistTracks.addAll(Arrays.asList(playlistTracksSpotify));

        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Spotify Client exception: " + e.getMessage());
        }
        return playlistTracks;
    }

    public List<TrackSimplified> getRecommendedTracks() {
        List<TrackSimplified> recommendedTracks = new ArrayList<>();
        try {
            final GetRecommendationsRequest getRecommendationsRequest = SpofyAuthorizator.spotifyApi.getRecommendations()
                    .limit(20)
                    .seed_tracks(recentlyPlayedTracksMerge())
                    .build();
            final Recommendations recommendations = getRecommendationsRequest.execute();

            recommendedTracks.addAll(Arrays.asList(recommendations.getTracks()));

        } catch ( IOException | SpotifyWebApiException e) {
            System.out.println("Spotify Client exception: " + e.getMessage());
        }

        return recommendedTracks;
    }

    private String recentlyPlayedTracksMerge() {
        List<DbMostFrequentTrack> dbMostFrequentTracks = personalizationDbBasedService.getMostPopularTracks();
        Collections.sort(dbMostFrequentTracks, Collections.reverseOrder());
        int quantityOfTracksToTake = 3;
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 1; i <= quantityOfTracksToTake; i++) {
            if(i == quantityOfTracksToTake){
                stringBuilder.append(dbMostFrequentTracks.get(i).getTrack_ids());
            } else {
                stringBuilder.append(dbMostFrequentTracks.get(i).getTrack_ids());
                stringBuilder.append(",");
            }

        }

        return stringBuilder.toString();
    }

    public DbUserPlaylist updatePlaylistTracks(DbUserPlaylist dbUserPlaylist) {

        final AddTracksToPlaylistRequest addTracksToPlaylistRequest = SpofyAuthorizator.spotifyApi
                .addTracksToPlaylist(dbUserPlaylist.getPlaylistStringId(), getTrackUrisForSpotifyRequest(dbUserPlaylist))
                .build();

        try {
            final SnapshotResult snapshotResult = addTracksToPlaylistRequest.execute();
        } catch(IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return dbUserPlaylist;
    }

    private String[] getTrackUrisForSpotifyRequest(DbUserPlaylist dbUserPlaylist) {
        return dbUserPlaylist.getTracks().stream()
                .map(track -> track.getStringNameForAddingToPlaylist())
                .toArray(String[]::new);
    }

}
