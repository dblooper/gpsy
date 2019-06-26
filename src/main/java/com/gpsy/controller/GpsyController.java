package com.gpsy.controller;

import com.gpsy.domain.DbMostFrequentTrack;
import com.gpsy.domain.DbPopularTrack;
import com.gpsy.domain.DbUserPlaylist;
import com.gpsy.domain.dto.PopularTrackDto;
import com.gpsy.domain.dto.RecentPlayedTrackDto;
import com.gpsy.domain.dto.UserPlaylistDto;
import com.gpsy.mapper.TrackMapper;
import com.gpsy.service.DbUserService;
import com.gpsy.service.PersonalizationDbBasedService;
import com.gpsy.service.SpotifyDataDbService;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private TrackMapper trackMapper;

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
    }

    @GetMapping(value = "/tracks/popular")
    public List<PopularTrackDto> getPopularTracks() {
        return trackMapper.mapToPopularTrackDtoList(personalizationDbBasedService.getMostPopularTracks());
    }

    @GetMapping(value = "/tracks/recommended")
    public List<String> gerRecommendedTracks() {
        return spotifyDataDbService.returnRecommendedTracks();
    }
}
