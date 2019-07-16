package com.gpsy.facade;

import com.gpsy.domain.spotify.dto.*;
import com.gpsy.mapper.spotify.DbPlaylistMapper;
import com.gpsy.mapper.spotify.SpotifyPlaylistMapper;
import com.gpsy.mapper.spotify.TrackMapper;
import com.gpsy.service.dbApiServices.spotify.FetchDataFromDbService;
import com.gpsy.service.dbApiServices.spotify.SpotifyHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
public class SpotifyFacade {

    @Autowired
    private FetchDataFromDbService fetchDataFromDbService;

    @Autowired
    private SpotifyHandleService spotifyHandleService;

    @Autowired
    private TrackMapper trackMapper;

    @Autowired
    private SpotifyPlaylistMapper spotifyPlaylistMapper;

    @Autowired
    private DbPlaylistMapper dbPlaylistMapper;

    public List<SearchTrackDto> fetchSearchedTracks(String searchedItem) {
        return spotifyHandleService.searchForTracks(searchedItem);
    }

    public List<PopularTrackDto> fetchPopularTracks(int qty) {
        return trackMapper.mapPopularTrackToPopularTrackDtoList(fetchDataFromDbService.fetchPopularTracks(qty));
    }

    public List<RecentPlayedTrackDto> fetchRecentTracks(int qty) {
        return trackMapper.mapToRecentPlayedTrackDtoList(fetchDataFromDbService.fetchRecentPlayedTracks(qty));
    }

    public List<UserPlaylistDto> fetchCurrentUserPlaylists() {

        return dbPlaylistMapper.mapToUserPlaylistsDto(fetchDataFromDbService.fetchUserPlaylists());
    }

    public List<MostFrequentTrackDto> fetchMostFrequentTracks(int qty) {
        return trackMapper.mapToPopularTrackDtoList(fetchDataFromDbService.fetchMostFrequentTracks(qty));
    }

    public List<RecommendedTrackDto> fetchRecommendedTracks(int qty) {
        return trackMapper.mapToRecommendedTrackDtoList(spotifyHandleService.returnRecommendedTracks(qty));
    }

    public RecommendedPlaylistDto fetchRecommendedPlaylist() {
        return dbPlaylistMapper.mapToRecommendedPlaylistDto(fetchDataFromDbService.fetchRecommendedPlaylist());
    }

    public void addTracksToUserPlaylist(@RequestBody UserPlaylistDto playlistDto) {
        spotifyHandleService.updatePlaylistTracks(spotifyPlaylistMapper.mapToDbUserPlaylist(playlistDto));
    }

    public void createNewPlaylist(@RequestBody UserPlaylistDto playlistDto) {
        spotifyHandleService.createPlaylist(spotifyPlaylistMapper.mapToDbUserPlaylist(playlistDto));
    }

    public RecommendedPlaylistDto updateFetchRecommendedPlaylist(@RequestParam int qty) {
        return dbPlaylistMapper.mapToRecommendedPlaylistDto(fetchDataFromDbService.updateFetchRecommendedPlaylistFromDb(qty));
    }

    public RecommendedPlaylistDto changeQuantityOfRecommendedTracks(@RequestParam int qty) {

        return dbPlaylistMapper.mapToRecommendedPlaylistDto(fetchDataFromDbService.changeNumberOfTracks(qty));
    }

    public UserPlaylistDto updatePlaylistDetails(@RequestBody UserPlaylistDto userPlaylistDto) {
        spotifyHandleService.updatePlaylistName(spotifyPlaylistMapper.mapToDbUserPlaylist(userPlaylistDto));
        return userPlaylistDto;
    }

    public void deleteUserPlaylistTrack(@RequestBody UserPlaylistDto playlistDto) {
        spotifyHandleService.deletePlaylistTrack(spotifyPlaylistMapper.mapToDbUserPlaylist(playlistDto));
    }
}
