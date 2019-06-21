package com.gpsy.service;

import com.gpsy.domain.DbPopularTrack;
import com.gpsy.domain.DbRecentPlayedTrack;
import com.gpsy.domain.DbUserPlaylist;
import com.gpsy.mapper.PlaylistMapper;
import com.gpsy.mapper.TrackMapper;
import com.gpsy.repository.SpotifyPopularTrackRepository;
import com.gpsy.repository.SpotifyRecentPlayedTrackRepository;
import com.gpsy.repository.SpotifyUserPlaylistsRepository;
import com.gpsy.spotify.client.SpotifyClient;
import com.wrapper.spotify.model_objects.specification.PlayHistory;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class SpotifyDataDbService {

    @Autowired
    private SpotifyPopularTrackRepository spotifyPopularTrackRepository;
    @Autowired
    private SpotifyRecentPlayedTrackRepository spotifyRecentPlayedTrackRepository;
    @Autowired
    private SpotifyUserPlaylistsRepository spotifyUserPlaylistsRepository;

    @Autowired
    private TrackMapper trackMapper;
    @Autowired
    private PlaylistMapper playlistMapper;

    @Autowired
    private SpotifyClient spotifyClient;

    public List<DbPopularTrack> savePopularTracks() {
        List<DbPopularTrack> savedTracks = new ArrayList<>();
        List<DbPopularTrack> storedTracks = spotifyPopularTrackRepository.findAll();
        List<Track> spotifyStoredTracks = spotifyClient.getSpotifyPopularTracks();
        if(storedTracks.size() == 0) {
            for (Track spotifyTrack : spotifyClient.getSpotifyPopularTracks()) {
                savedTracks.add(spotifyPopularTrackRepository.save(trackMapper.mapSpotifyTrackToDbPopularTrack(spotifyTrack)));
            }
            Collections.sort(savedTracks, Collections.reverseOrder());
            return savedTracks;
        }

        for(DbPopularTrack dbPopularTrack : storedTracks) {
            for (Track spotifyTrack : spotifyStoredTracks) {
                    if(dbPopularTrack.getTrackId().equals(spotifyTrack.getId()) && dbPopularTrack.getPopularity() != spotifyTrack.getPopularity()){
                        dbPopularTrack.setPopularity(spotifyTrack.getPopularity());
                        savedTracks.add(spotifyPopularTrackRepository.save(dbPopularTrack));
                    }else if(!storedTracks.contains(trackMapper.mapSpotifyTrackToDbPopularTrack(spotifyTrack))) {
                        savedTracks.add(spotifyPopularTrackRepository.save(trackMapper.mapSpotifyTrackToDbPopularTrack(spotifyTrack)));
                    }
            }
        }
        Collections.sort(savedTracks, Collections.reverseOrder());
        return savedTracks;
    }

    public List<DbRecentPlayedTrack> saveRecentPlayedTracks() {
        List<DbRecentPlayedTrack> savedTracks = new ArrayList<>();
        List<DbRecentPlayedTrack> storedTracks = spotifyRecentPlayedTrackRepository.findAll();
        List<PlayHistory> recentTracks = spotifyClient.getSpotifyRecentPlayedTracks();

        if (storedTracks.size() == 0) {
            for (PlayHistory recentTrack : recentTracks) {
                savedTracks.add(spotifyRecentPlayedTrackRepository.save(trackMapper.mapSpotifyTrackToDbRecentPlayedTrack(recentTrack)));
            }
            return savedTracks;
        }

        for (PlayHistory spotifyTrack : recentTracks) {
                if(spotifyTrack.getPlayedAt().after(storedTracks.get(0).getPlayDate())) {
                    savedTracks.add(spotifyRecentPlayedTrackRepository.save(trackMapper.mapSpotifyTrackToDbRecentPlayedTrack(spotifyTrack)));
                }
        }

//
////                if(dbPopularTrack.getTrackId().equals(spotifyTrack.getId()) && dbPopularTrack.getPopularity() != spotifyTrack.getPopularity()){
////                    savedTracks.add(spotifyPopularTrackRepository.save(trackMapper.mapSpotifyTrackToDbPopularTrack(spotifyTrack)));
////                }else if(!storedTracks.contains(trackMapper.mapSpotifyTrackToDbPopularTrack(spotifyTrack))) {
////                    savedTracks.add(spotifyPopularTrackRepository.save(trackMapper.mapSpotifyTrackToDbPopularTrack(spotifyTrack)));
////                }
//
//        }
        return savedTracks;
    }

    public List<DbUserPlaylist> saveUserPlaylists() {
        List<DbUserPlaylist> savedPlaylists = new ArrayList<>();
        List<DbUserPlaylist> dbUserPlaylists = spotifyUserPlaylistsRepository.findAll();
        List<PlaylistSimplified> spotifyUserPlaylists = spotifyClient.getUserPlaylists();

        for(PlaylistSimplified playlistSimplified: spotifyUserPlaylists) {
            if(!dbUserPlaylists.contains(playlistMapper.mapSpotifyPlaylistToDbUserPlaylist(playlistSimplified))) {
                savedPlaylists.add(spotifyUserPlaylistsRepository.save(playlistMapper.mapSpotifyPlaylistToDbUserPlaylist(playlistSimplified)));
            }
        }

        return savedPlaylists;
    }

    public List<String> returnRecommendedTracks() {
        List<String> recommendedTracks = new ArrayList<>();
        List<TrackSimplified> tracksSimplified = spotifyClient.getRecommendedTracks();
        recommendedTracks = tracksSimplified.stream()
                .map(track -> track.getName())
                .collect(Collectors.toList());


        return recommendedTracks;
    }
}
