package com.gpsy.externalApis.spotify.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.gpsy.config.InitialLimitValues;
import com.gpsy.domain.spotify.MostFrequentTrack;
import com.gpsy.domain.spotify.UserPlaylist;
import com.gpsy.externalApis.spotify.config.SpotifyConfig;
import com.gpsy.service.dbApiServices.spotify.FetchDataFromDbService;
import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.special.SearchResult;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.browse.GetRecommendationsRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import com.wrapper.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;
import com.wrapper.spotify.requests.data.playlists.*;
import com.wrapper.spotify.requests.data.search.SearchItemRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SpotifyClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpotifyClient.class);

    @Autowired
    private SpotifyAuthorizator spotifyAuthorizator;

    @Autowired
    private FetchDataFromDbService fetchDataFromDbService;

    @Autowired
    private SpotifyConfig spotifyConfig;

    public List<Track> searchForTrack(String searchedItem) {
        List<Track> searchedTracks = new ArrayList<>();

        try {
            final SearchItemRequest searchItemRequest = spotifyAuthorizator.getSpotifyApi().searchItem(searchedItem, ModelObjectType.TRACK.getType())
                    .limit(InitialLimitValues.LIMIT_FREE_SEARCH_TRACKS)
                    .build();
            final SearchResult results = searchItemRequest.execute();
            final Paging<Track> receivedPaging = results.getTracks();
            searchedTracks.addAll(Arrays.asList(receivedPaging.getItems()));
            return searchedTracks;
        } catch(IOException | SpotifyWebApiException e) {
            LOGGER.error(e.getMessage(), e);
            return searchedTracks;
        }

    }

    public List<Track> getSpotifyPopularTracks() {
        final List<Track> tracks = new ArrayList<>();

        try {
            final GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyAuthorizator.getSpotifyApi().getUsersTopTracks()
                    .limit(InitialLimitValues.LIMIT_POPULAR)
                    .build();
            final Paging<Track> trackPaging = getUsersTopTracksRequest.execute();
            tracks.addAll(Arrays.asList(trackPaging.getItems()));
        } catch (IOException | SpotifyWebApiException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return tracks;
    }

    public List<PlayHistory> getSpotifyRecentPlayedTracks() {
        final List<PlayHistory> recentTracks = new ArrayList<>();

        try {
            GetCurrentUsersRecentlyPlayedTracksRequest getCurrentUsersRecentlyPlayedTracksRequest = spotifyAuthorizator.getSpotifyApi().getCurrentUsersRecentlyPlayedTracks()
                    .limit(InitialLimitValues.LIMIT_RECENT_FROM_SPOTIFY)
                    .build();
            final PagingCursorbased<PlayHistory> historyOfPlays = getCurrentUsersRecentlyPlayedTracksRequest.execute();
            PlayHistory[] tracksArray = historyOfPlays.getItems();
            recentTracks.addAll(Arrays.asList(tracksArray));
        } catch (IOException | SpotifyWebApiException e) {
            LOGGER.error(e.getMessage(), e);
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
            LOGGER.error(e.getMessage(), e);
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
            LOGGER.error(e.getMessage(), e);
        }
        return playlistTracks;
    }

    public List<TrackSimplified> getRecommendedTracks(int qty) {
        List<TrackSimplified> recommendedTracks = new ArrayList<>();

        try {
            final GetRecommendationsRequest getRecommendationsRequest = spotifyAuthorizator.getSpotifyApi().getRecommendations()
                    .limit(qty)
                    .seed_tracks(popularTracksMerge(InitialLimitValues.LIMIT_TOP_TRACK_SIMILAR_TO_RECOMMEND))
                    .build();
            final Recommendations recommendations = getRecommendationsRequest.execute();
            recommendedTracks.addAll(Arrays.asList(recommendations.getTracks()));
        } catch ( IOException | SpotifyWebApiException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return recommendedTracks;
    }

    public UserPlaylist updatePlaylistTracks(UserPlaylist userPlaylist) {

        final AddTracksToPlaylistRequest addTracksToPlaylistRequest = spotifyAuthorizator.getSpotifyApi()
                .addTracksToPlaylist(userPlaylist.getPlaylistStringId(), getTrackUrisForSpotifyRequest(userPlaylist))
                .build();
        try {
            addTracksToPlaylistRequest.execute();
        } catch(IOException | SpotifyWebApiException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return userPlaylist;
    }

    public void deletePlaylistTrack(UserPlaylist userPlaylist) {

        final RemoveTracksFromPlaylistRequest removeTracksFromPlaylistRequest = spotifyAuthorizator.getSpotifyApi()
                .removeTracksFromPlaylist(userPlaylist.getPlaylistStringId(),
                                            jsonArrayTrackToDeleteMaker(userPlaylist)).build();
        try {
            removeTracksFromPlaylistRequest.execute();
        } catch(IOException | SpotifyWebApiException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public UserPlaylist updatePlaylistDetails(UserPlaylist updatedPlaylist) {

        final ChangePlaylistsDetailsRequest changePlaylistsDetailsRequest = spotifyAuthorizator.getSpotifyApi()
                .changePlaylistsDetails(updatedPlaylist.getPlaylistStringId())
                .name(updatedPlaylist.getName())
                .build();
        try {
            changePlaylistsDetailsRequest.execute();
            return updatedPlaylist;
        } catch(IOException | SpotifyWebApiException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new UserPlaylist.Builder().name("Not changed, spotify api error").stringId(updatedPlaylist.getPlaylistStringId()).tracks(new ArrayList<>()).build();
    }

    public void createPlaylist(UserPlaylist newPlaylist) {

        final CreatePlaylistRequest createPlaylistRequest = spotifyAuthorizator.getSpotifyApi()
                .createPlaylist(spotifyConfig.getUserId(), newPlaylist.getName())
                .build();
        try {
            createPlaylistRequest.execute();
        } catch(IOException | SpotifyWebApiException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    protected String[] getTrackUrisForSpotifyRequest(UserPlaylist userPlaylist) {
        return userPlaylist.getTracks().stream()
                .map(track -> track.getStringNameForAddingToPlaylist())
                .toArray(String[]::new);
    }

    protected JsonArray jsonArrayTrackToDeleteMaker(UserPlaylist userPlaylist) {

        StringBuilder stringBuilder = new StringBuilder();

        if(userPlaylist.getTracks().size() == 1) {
            stringBuilder.append("{\"uri\":\"").append(userPlaylist.getTracks().get(0).getStringNameForAddingToPlaylist()).append("\"}");
        } else if(userPlaylist.getTracks().size() > 1) {
            for(int i = 0; i < userPlaylist.getTracks().size() - 1; i++) {
                stringBuilder.append("{\"uri\":\"").append(userPlaylist.getTracks().get(i).getStringNameForAddingToPlaylist()).append("\"},");
            }
            stringBuilder.append("{\"uri\":\"").append(userPlaylist.getTracks().get(userPlaylist.getTracks().size() - 1).getStringNameForAddingToPlaylist()).append("\"}");
        }

        String trackToRemove = "["+ stringBuilder.toString() +"]";
        return new JsonParser().parse(trackToRemove).getAsJsonArray();
    }

    protected String popularTracksMerge(int limit) {

        List<MostFrequentTrack> mostFrequentTracks = fetchDataFromDbService.fetchMostFrequentTracks(limit);

        if(mostFrequentTracks.size() > 0 ) {
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < limit; i++) {
                if (i == limit - 1) {
                    stringBuilder.append(mostFrequentTracks.get(i).getTrackStringId());
                } else {
                    stringBuilder.append(mostFrequentTracks.get(i).getTrackStringId());
                    stringBuilder.append(",");
                }
            }
            return stringBuilder.toString();
        } else {
            return "";
        }
    }

}
