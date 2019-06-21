package com.gpsy.service;

import com.gpsy.domain.DbRecentPlayedTrack;
import com.gpsy.domain.dto.RecentPlayedTrackDto;
import com.gpsy.mapper.TrackMapper;
import com.gpsy.repository.SpotifyPopularTrackRepository;
import com.gpsy.repository.SpotifyRecentPlayedTrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DbUserService {

    @Autowired
    private SpotifyRecentPlayedTrackRepository spotifyRecentPlayedTrackRepository;
    @Autowired
    private SpotifyDataDbService spotifyDataDbService;

    @Autowired
    TrackMapper trackMapper;

    public List<RecentPlayedTrackDto> fetchRecentPlayedTracks() {
        spotifyDataDbService.saveRecentPlayedTracks();
        List<DbRecentPlayedTrack> recentPlayedTracks = spotifyRecentPlayedTrackRepository.findAll();
        Collections.sort(recentPlayedTracks, Collections.reverseOrder());
        return trackMapper.mapToRecentPlayedTrackDtos(recentPlayedTracks) ;
    }
}
