package com.gpsy.service;

import com.gpsy.domain.DbMostFrequentTrack;
import com.gpsy.domain.DbRecentPlayedTrack;
import com.gpsy.domain.dto.RecentPlayedTrackDto;
import com.gpsy.domain.dto.UserPlaylistDto;
import com.gpsy.mapper.DbPlaylistMapper;
import com.gpsy.mapper.TrackMapper;
import com.gpsy.repository.SpotifyPopularTrackRepository;
import com.gpsy.repository.SpotifyRecentPlayedTrackRepository;
import com.gpsy.repository.SpotifyUserPlaylistsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DbUserService {

    @Autowired
    private SpotifyRecentPlayedTrackRepository spotifyRecentPlayedTrackRepository;
    @Autowired
    private SpotifyUserPlaylistsRepository spotifyUserPlaylistsRepository;

    @Autowired
    private SpotifyDataDbService spotifyDataDbService;

    @Autowired
    private PersonalizationDbBasedService personalizationDbBasedService;

    @Autowired
    private TrackMapper trackMapper;

    @Autowired
    private DbPlaylistMapper dbPlaylistMapper;

    public List<RecentPlayedTrackDto> fetchRecentPlayedTracks() {
        spotifyDataDbService.saveRecentPlayedTracks();
        List<DbRecentPlayedTrack> recentPlayedTracks = spotifyRecentPlayedTrackRepository.findAll();
        Collections.sort(recentPlayedTracks, Collections.reverseOrder());
        return trackMapper.mapToRecentPlayedTrackDtos(recentPlayedTracks) ;
    }

    public List<UserPlaylistDto> fetchUserPlaylists() {
        spotifyDataDbService.saveUserPlaylists();
        return dbPlaylistMapper.mapToUserPlaylistsDto(spotifyUserPlaylistsRepository.findAll());
    }

}
