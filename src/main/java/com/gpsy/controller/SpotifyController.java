package com.gpsy.controller;

import com.gpsy.domain.spotify.dto.*;
import com.gpsy.mapper.spotify.DbPlaylistMapper;
import com.gpsy.mapper.spotify.SpotifyPlaylistMapper;
import com.gpsy.mapper.spotify.TrackMapper;
import com.gpsy.service.spotify.FetchDataFromDbService;
import com.gpsy.service.spotify.SpotifyHandleService;
import com.gpsy.service.spotify.SaveSpotifyDataToDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/gpsy")
@CrossOrigin("*")
public class SpotifyController {

    @Autowired
    private SaveSpotifyDataToDbService saveSpotifyDataToDbService;

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

    @GetMapping(value = "/tracks/search")
    public List<SearchTrackDto> fetchSearchedTracks(@RequestParam String searchedItem) {
        return spotifyHandleService.searchForTracks(searchedItem);
    }

    @GetMapping(value = "/tracks/spotify/popular")
    public List<PopularTrackDto> fetchTracks() {

        return trackMapper.mapPopularTrackToPopularTrackDtoList(saveSpotifyDataToDbService.fetchPopularTracks());
    }

    @GetMapping(value = "/tracks/recent")
    public List<RecentPlayedTrackDto> fetchRecentTracks() {
        return trackMapper.mapToRecentPlayedTrackDtos(fetchDataFromDbService.fetchRecentPlayedTracks());
    }

    @GetMapping(value = "/playlists/current")
    public List<UserPlaylistDto> fetchCurrentUserPlaylists() {

        return dbPlaylistMapper.mapToUserPlaylistsDto(fetchDataFromDbService.fetchUserPlaylists());
    }

    @GetMapping(value = "/tracks/frequent")
    public List<MostFrequentTrackDto> fetchMostFrequentTracks() {
        return trackMapper.mapToPopularTrackDtoList(fetchDataFromDbService.fetchMostFrequentTracks());
    }

    @GetMapping(value = "/tracks/recommended")
    public List<RecommendedTrackDto> fetchRecommendedTracks() {
        return trackMapper.mapToRecommendedTrackDtoList(spotifyHandleService.returnRecommendedTracks());
    }

    @GetMapping(value = "/playlists/recommended")
    public RecommendedPlaylistDto fetchRecommendedPlaylist() {
        return dbPlaylistMapper.mapToRecommendedPlaylistDto(fetchDataFromDbService.fetchRecommendedPlaylist());
    }

    @GetMapping(value = "/playlists/recommended/new")
    public RecommendedPlaylistDto updateFetchRecommendedPlaylist(@RequestParam int qty) {
        return dbPlaylistMapper.mapToRecommendedPlaylistDto(fetchDataFromDbService.updateFetchRecommendedPlaylistFromDb(qty));
    }

    @GetMapping(value = "/playlists/recommended/change")
    public RecommendedPlaylistDto changeQuantityOfRecommendedTracks(@RequestParam int qty) {
        return dbPlaylistMapper.mapToRecommendedPlaylistDto(fetchDataFromDbService.changeNumberOfTracks(qty));
    }

    @PostMapping(value = "/playlists/tracks/add")
    public UserPlaylistDto updateUserPlaylist(@RequestBody UserPlaylistDto playlistDto) {
        spotifyHandleService.updatePlaylistTracks(spotifyPlaylistMapper.mapToDbUserPlaylist(playlistDto));
        return playlistDto;
    }

    @PostMapping(value = "/playlists/new")
    public UserPlaylistDto createNewPlaylist(@RequestBody UserPlaylistDto playlistDto) {
        spotifyHandleService.createPlaylist(spotifyPlaylistMapper.mapToDbUserPlaylist(playlistDto));
        return playlistDto;
    }

    @DeleteMapping(value = "/playlists/tracks/delete")
    public UserPlaylistDto deleteUserTrack(@RequestBody UserPlaylistDto playlistDto) {
        spotifyHandleService.deletePlaylistTrack(spotifyPlaylistMapper.mapToDbUserPlaylist(playlistDto));
        return playlistDto;
    }

    @PutMapping(value = "/playlists/update")
    public UserPlaylistDto updatePlaylistDetails(@RequestBody UserPlaylistDto userPlaylistDto) {
        spotifyHandleService.updatePlaylistName(spotifyPlaylistMapper.mapToDbUserPlaylist(userPlaylistDto));
        return userPlaylistDto;
    }
}
