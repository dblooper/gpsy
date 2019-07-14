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

    public List<SearchTrackDto> fetchSearchedTracks(@RequestParam String searchedItem) {
        return spotifyHandleService.searchForTracks(searchedItem);
    }

    public List<PopularTrackDto> fetchPopularTracks() {
        return trackMapper.mapPopularTrackToPopularTrackDtoList(fetchDataFromDbService.fetchPopularTracks());
    }

    public List<RecentPlayedTrackDto> fetchRecentTracks() {
        return trackMapper.mapToRecentPlayedTrackDtoList(fetchDataFromDbService.fetchRecentPlayedTracks());
    }

    public List<UserPlaylistDto> fetchCurrentUserPlaylists() {

        return dbPlaylistMapper.mapToUserPlaylistsDto(fetchDataFromDbService.fetchUserPlaylists());
    }

    public List<MostFrequentTrackDto> fetchMostFrequentTracks() {
        return trackMapper.mapToPopularTrackDtoList(fetchDataFromDbService.fetchMostFrequentTracks());
    }

    public List<RecommendedTrackDto> fetchRecommendedTracks() {
        return trackMapper.mapToRecommendedTrackDtoList(spotifyHandleService.returnRecommendedTracks());
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
