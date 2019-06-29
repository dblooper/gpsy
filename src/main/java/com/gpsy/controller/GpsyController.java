package com.gpsy.controller;

import com.gpsy.domain.DbPopularTrack;
import com.gpsy.domain.dto.PopularTrackDto;
import com.gpsy.domain.dto.RecentPlayedTrackDto;
import com.gpsy.domain.dto.RecommendedTrackDto;
import com.gpsy.domain.dto.UserPlaylistDto;
import com.gpsy.mapper.DbPlaylistMapper;
import com.gpsy.mapper.SpotifyPlaylistMapper;
import com.gpsy.mapper.TrackMapper;
import com.gpsy.service.DbUserService;
import com.gpsy.service.PersonalizationDbBasedService;
import com.gpsy.service.SpotifyHandleService;
import com.gpsy.service.SpotifyDataDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/gpsy")
public class GpsyController {

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

    @GetMapping(value = "/tracks")
    public List<DbPopularTrack> getTracks() {
        return spotifyDataDbService.savePopularTracks();
    }

    @GetMapping(value = "/tracks/recent")
    public List<RecentPlayedTrackDto> getRecentTracks() {
        return dbUserService.fetchRecentPlayedTracks();
    }

    @GetMapping(value = "/playlists/current")
    public List<UserPlaylistDto> getCurrentUserPlaylists() {

        return dbUserService.fetchUserPlaylists();
//        return dbPlaylistMapper.mapToUserPlaylistsDto(dbUserService.fetchUserPlaylists());
    }

    @GetMapping(value = "/tracks/popular")
    public List<PopularTrackDto> getPopularTracks() {
        return trackMapper.mapToPopularTrackDtoList(personalizationDbBasedService.getMostPopularTracks());
    }

    @GetMapping(value = "/tracks/recommended")
    public List<RecommendedTrackDto> gerRecommendedTracks() {
        return trackMapper.mapToRecommendedTrackDtoList(spotifyDataDbService.returnRecommendedTracks());
    }

    @PostMapping(value = "playlists/addToPlaylist")
    public UserPlaylistDto userPlaylist(@RequestBody UserPlaylistDto playlistDto) {
        spotifyHandleService.updatePlaylistTracks(spotifyPlaylistMapper.mapToDbUserPlaylist(playlistDto));
        return playlistDto;
    }
}
