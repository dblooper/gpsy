package com.gpsy.controller;

import com.gpsy.domain.spotify.dto.*;
import com.gpsy.mapper.spotify.DbPlaylistMapper;
import com.gpsy.mapper.spotify.SpotifyPlaylistMapper;
import com.gpsy.mapper.spotify.TrackMapper;
import com.gpsy.service.spotify.DbUserService;
import com.gpsy.service.spotify.PersonalizationDbBasedService;
import com.gpsy.service.spotify.SpotifyHandleService;
import com.gpsy.service.spotify.SpotifyDataDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/gpsy")
public class SpotifyController {

    @Autowired
    private SpotifyDataDbService spotifyDataDbService;

    @Autowired
    private PersonalizationDbBasedService personalizationDbBasedService;

    @Autowired
    private DbUserService dbUserService;

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

        return trackMapper.mapPopularTrackToPopularTrackDtoList(spotifyDataDbService.fetchPopularTracks());
    }

    @GetMapping(value = "/tracks/recent")
    public List<RecentPlayedTrackDto> fetchRecentTracks() {
        return trackMapper.mapToRecentPlayedTrackDtos(dbUserService.fetchRecentPlayedTracks());
    }

    @GetMapping(value = "/playlists/current")
    public List<UserPlaylistDto> fetchCurrentUserPlaylists() {

        return dbPlaylistMapper.mapToUserPlaylistsDto(dbUserService.fetchUserPlaylists());
//        return dbPlaylistMapper.mapToUserPlaylistsDto(dbUserService.fetchUserPlaylists());
    }

    @GetMapping(value = "/tracks/frequent")
    public List<MostFrequentTrackDto> fetchMostFrequentTracks() {
        return trackMapper.mapToPopularTrackDtoList(personalizationDbBasedService.fetchMostFrequentTracks());
    }

    @GetMapping(value = "/tracks/recommended")
    public List<RecommendedTrackDto> fetchRecommendedTracks() {
        return trackMapper.mapToRecommendedTrackDtoList(spotifyDataDbService.returnRecommendedTracks());
    }

    @GetMapping(value = "/playlists/recommended")
    public RecommendedPlaylistDto fetchRecommendedPlaylist() {
        return dbPlaylistMapper.mapToRecommendedPlaylistDto(personalizationDbBasedService.fetchRecommendedPlaylist());
    }

    @GetMapping(value = "/playlists/recommended/new")
    public RecommendedPlaylistDto updateFetchRecommendedPlaylist(@RequestParam int qty) {
        return dbPlaylistMapper.mapToRecommendedPlaylistDto(personalizationDbBasedService.updateFetchRecommendedPlaylistFromDb(qty));
    }

    @GetMapping(value = "/playlists/recommended/change")
    public RecommendedPlaylistDto changeQuantityOfRecommendedTracks(@RequestParam int qty) {
        return dbPlaylistMapper.mapToRecommendedPlaylistDto(personalizationDbBasedService.changeNumberOfTracks(qty));
    }

    @PostMapping(value = "/playlists/addToPlaylist")
    public UserPlaylistDto updateUserPlaylist(@RequestBody UserPlaylistDto playlistDto) {
        spotifyHandleService.updatePlaylistTracks(spotifyPlaylistMapper.mapToDbUserPlaylist(playlistDto));
        return playlistDto;
    }

    @PostMapping(value = "/playlists/addNewPlaylist")
    public UserPlaylistDto createNewPlaylist(@RequestBody UserPlaylistDto playlistDto) {
        spotifyHandleService.createPlaylist(spotifyPlaylistMapper.mapToDbUserPlaylist(playlistDto));
        return playlistDto;
    }

    @DeleteMapping(value = "/playlists/deleteTrack")
    public UserPlaylistDto deleteUserTrack(@RequestBody UserPlaylistDto playlistDto) {
        spotifyHandleService.deletePlaylistTrack(spotifyPlaylistMapper.mapToDbUserPlaylist(playlistDto));
        return playlistDto;
    }

    @PostMapping(value = "/playlists/updateDetails")
    public UserPlaylistDto updatePlaylistDetails(@RequestBody UserPlaylistDto userPlaylistDto) {
        spotifyHandleService.updatePlaylistName(spotifyPlaylistMapper.mapToDbUserPlaylist(userPlaylistDto));
        return userPlaylistDto;
    }
}
