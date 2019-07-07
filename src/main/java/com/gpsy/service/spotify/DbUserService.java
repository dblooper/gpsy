package com.gpsy.service.spotify;

import com.gpsy.config.InitialLimitValues;
import com.gpsy.domain.spotify.RecentPlayedTrack;
import com.gpsy.domain.spotify.UserPlaylist;
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

    public List<RecentPlayedTrack> fetchRecentPlayedTracks() {
        spotifyDataDbService.saveRecentPlayedTracks();
        List<RecentPlayedTrack> recentPlayedTracks = spotifyRecentPlayedTrackRepository.findAll();
        Collections.sort(recentPlayedTracks, Collections.reverseOrder());
        List<RecentPlayedTrack> limitedRecentPlayedTracks = recentPlayedTracks.stream()
                .limit(InitialLimitValues.LIMIT_RECENT)
                .collect(Collectors.toList());
        return limitedRecentPlayedTracks;
    }

    public List<UserPlaylist> fetchUserPlaylists() {
        spotifyDataDbService.saveUserPlaylists();
        return spotifyUserPlaylistsRepository.findAll();
    }

}
