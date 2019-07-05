package com.gpsy.externalApis.spotify.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.gpsy.config.InitialLimitValues;
import com.gpsy.domain.DbMostFrequentTrack;
import com.gpsy.domain.DbUserPlaylist;
import com.gpsy.externalApis.spotify.config.SpotifyConfig;
import com.gpsy.service.spotify.PersonalizationDbBasedService;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.special.SnapshotResult;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.browse.GetRecommendationsRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import com.wrapper.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;
import com.wrapper.spotify.requests.data.playlists.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Getter
public class SpotifyClient {

    @Autowired
    private SpofyAuthorizator spotifyAuthorizator;

    @Autowired
    private PersonalizationDbBasedService personalizationDbBasedService;

    @Autowired
    private SpotifyConfig spotifyConfig;

    private String[] getTrackUrisForSpotifyRequest(DbUserPlaylist dbUserPlaylist) {
        return dbUserPlaylist.getTracks().stream()
                .map(track -> track.getStringNameForAddingToPlaylist())
                .toArray(String[]::new);
    }

    public List<Track> getSpotifyPopularTracks() {
        final List<Track> tracks = new ArrayList<>();

        try {
            GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyAuthorizator.getSpotifyApi().getUsersTopTracks()
                    .limit(InitialLimitValues.LIMIT_POPULAR)
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
            GetCurrentUsersRecentlyPlayedTracksRequest getCurrentUsersRecentlyPlayedTracksRequest = spotifyAuthorizator.getSpotifyApi().getCurrentUsersRecentlyPlayedTracks()
                    .limit(InitialLimitValues.LIMIT_RECENT)
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
            final GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = spotifyAuthorizator.getSpotifyApi().getListOfCurrentUsersPlaylists().build();
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
            final GetPlaylistsTracksRequest playlistsTracksRequest = spotifyAuthorizator.getSpotifyApi().getPlaylistsTracks(playlistId)
                    .limit(InitialLimitValues.LIMIT_FETCHING_PLAYLIST_TRACKS_FROM_SPOTIFY)
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
            final GetRecommendationsRequest getRecommendationsRequest = spotifyAuthorizator.getSpotifyApi().getRecommendations()
                    .limit(InitialLimitValues.LIMIT_FETCHING_RECOMMENDED_FROM_SPOTIFY)
                    .seed_tracks(popularTracksMerge(InitialLimitValues.LIMIT_TOP_TRACK_SIMILAR_TO_RECOMMEND))
                    .build();
            final Recommendations recommendations = getRecommendationsRequest.execute();

            recommendedTracks.addAll(Arrays.asList(recommendations.getTracks()));

        } catch ( IOException | SpotifyWebApiException e) {
            System.out.println("Spotify Client exception: " + e.getMessage());
        }

        return recommendedTracks;
    }

    private String popularTracksMerge(int limit) {

        List<DbMostFrequentTrack> dbMostFrequentTracks = personalizationDbBasedService.fetchMostFrequentTracks();

        if(dbMostFrequentTracks.size() > 0 ) {
            Collections.sort(dbMostFrequentTracks, Collections.reverseOrder());
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 1; i <= limit; i++) {
                if (i == limit) {
                    stringBuilder.append(dbMostFrequentTracks.get(i).getTrackId());
                } else {
                    stringBuilder.append(dbMostFrequentTracks.get(i).getTrackId());
                    stringBuilder.append(",");
                }
            }

            return stringBuilder.toString();
        } else {
            return "";
        }
    }

    public DbUserPlaylist updatePlaylistTracks(DbUserPlaylist dbUserPlaylist) {

        final AddTracksToPlaylistRequest addTracksToPlaylistRequest = spotifyAuthorizator.getSpotifyApi()
                .addTracksToPlaylist(dbUserPlaylist.getPlaylistStringId(), getTrackUrisForSpotifyRequest(dbUserPlaylist))
                .build();

        try {
            final SnapshotResult snapshotResult = addTracksToPlaylistRequest.execute();
        } catch(IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return dbUserPlaylist;
    }

    public void deletePlaylistTrack(DbUserPlaylist dbUserPlaylist) {

        final RemoveTracksFromPlaylistRequest removeTracksFromPlaylistRequest = spotifyAuthorizator.getSpotifyApi()
                .removeTracksFromPlaylist(dbUserPlaylist.getPlaylistStringId(), jsonArrayTrackToDeleteMaker(dbUserPlaylist)).build();

        try {
            final SnapshotResult snapshotResult = removeTracksFromPlaylistRequest.execute();
        } catch(IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updatePlaylistDetails(DbUserPlaylist updatedPlaylist) {

        final ChangePlaylistsDetailsRequest changePlaylistsDetailsRequest = spotifyAuthorizator.getSpotifyApi()
                .changePlaylistsDetails(updatedPlaylist.getPlaylistStringId())
                .name(updatedPlaylist.getName())
                .build();

        try {
            final String changePlaylistName = changePlaylistsDetailsRequest.execute();
        } catch(IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void createPlaylist(DbUserPlaylist newPlaylist) {

        final CreatePlaylistRequest createPlaylistRequest = spotifyAuthorizator.getSpotifyApi()
                .createPlaylist(spotifyConfig.getUserId(), newPlaylist.getName())
                .build();

        try {
            final Playlist playlist = createPlaylistRequest.execute();
        } catch(IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private JsonArray jsonArrayTrackToDeleteMaker(DbUserPlaylist dbUserPlaylist) {

        StringBuilder stringBuilder = new StringBuilder();

        if(dbUserPlaylist.getTracks().size() == 1) {
            stringBuilder.append("{\"uri\":\"").append(dbUserPlaylist.getTracks().get(0).getStringNameForAddingToPlaylist()).append("\"}");
        } else if(dbUserPlaylist.getTracks().size() > 1) {
            for(int i = 0; i < dbUserPlaylist.getTracks().size() - 1; i++) {
                stringBuilder.append("{\"uri\":\"").append(dbUserPlaylist.getTracks().get(i).getStringNameForAddingToPlaylist()).append("\"},");
            }
            stringBuilder.append("{\"uri\":\"").append(dbUserPlaylist.getTracks().get(dbUserPlaylist.getTracks().size() - 1).getStringNameForAddingToPlaylist()).append("\"}");
        }

        String trackToRemove = "["+ stringBuilder.toString() +"]";
        return new JsonParser().parse(trackToRemove).getAsJsonArray();
    }


}
