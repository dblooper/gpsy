package com.gpsy.service.spotify;

import com.gpsy.config.InitialLimitValues;
import com.gpsy.domain.DbRecentPlayedTrack;
import com.gpsy.domain.dto.RecentPlayedTrackDto;
import com.gpsy.domain.dto.UserPlaylistDto;
import com.gpsy.mapper.spotify.DbPlaylistMapper;
import com.gpsy.mapper.spotify.TrackMapper;
import com.gpsy.repository.spotify.SpotifyRecentPlayedTrackRepository;
import com.gpsy.repository.spotify.SpotifyUserPlaylistsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        List<DbRecentPlayedTrack> limitedRecentPlayedTracks = recentPlayedTracks.stream()
                .limit(InitialLimitValues.LIMIT_RECENT)
                .collect(Collectors.toList());
        return trackMapper.mapToRecentPlayedTrackDtos(limitedRecentPlayedTracks) ;
    }

    public List<UserPlaylistDto> fetchUserPlaylists() {
        spotifyDataDbService.saveUserPlaylists();
        return dbPlaylistMapper.mapToUserPlaylistsDto(spotifyUserPlaylistsRepository.findAll());
    }

}
