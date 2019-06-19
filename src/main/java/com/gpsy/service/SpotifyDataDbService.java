package com.gpsy.service;

import com.gpsy.domain.DbPopularTrack;
import com.gpsy.domain.DbRecentPlayedTrack;
import com.gpsy.mapper.TrackMapper;
import com.gpsy.repository.SpotifyPopularTrackRepository;
import com.gpsy.repository.SpotifyRecentPlayedTrackRepository;
import com.gpsy.spotify.client.SpotifyClient;
import com.wrapper.spotify.model_objects.specification.PlayHistory;
import com.wrapper.spotify.model_objects.specification.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SpotifyDataDbService {

    @Autowired
    private SpotifyPopularTrackRepository spotifyPopularTrackRepository;
    @Autowired
    private SpotifyRecentPlayedTrackRepository spotifyRecentPlayedTrackRepository;

    @Autowired
    private TrackMapper trackMapper;

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
                        savedTracks.add(spotifyPopularTrackRepository.save(trackMapper.mapSpotifyTrackToDbPopularTrack(spotifyTrack)));
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
        Collections.sort(storedTracks, Collections.reverseOrder());
        List<PlayHistory> recentTracks = spotifyClient.getSppotifyRecentPlayedTracks();

        if (storedTracks.size() == 0) {
            for (PlayHistory recentTrack : recentTracks) {
                savedTracks.add(spotifyRecentPlayedTrackRepository.save(trackMapper.mapSpotifyTrackToDbRecentPlayedTrack(recentTrack)));
            }
            Collections.sort(savedTracks, Collections.reverseOrder());
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
        Collections.sort(savedTracks, Collections.reverseOrder());
        return savedTracks;
    }
}
