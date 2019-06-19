package com.gpsy.controller;

import com.gpsy.domain.DbPopularTrack;
import com.gpsy.domain.DbRecentPlayedTrack;
import com.gpsy.domain.RecentPlayedTrackDto;
import com.gpsy.mapper.TrackMapper;
import com.gpsy.service.SpotifyDataDbService;
import com.gpsy.spotify.client.SpotifyClient;
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
    private TrackMapper trackMapper;

    @GetMapping(value = "/tracks")
    public List<DbPopularTrack> getTracks() {
        return spotifyDataDbService.savePopularTracks();
    }

    @GetMapping(value = "/tracks/recent")
    public List<RecentPlayedTrackDto> getRecentTracks() {
        return trackMapper.mapDbRecentPlayedTrackToDto(spotifyDataDbService.saveRecentPlayedTracks());
    }
}
