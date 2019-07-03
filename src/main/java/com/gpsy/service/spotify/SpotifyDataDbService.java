package com.gpsy.service.spotify;

import com.gpsy.config.InitialLimitValues;
import com.gpsy.domain.DbPopularTrack;
import com.gpsy.domain.DbRecentPlayedTrack;
import com.gpsy.domain.DbRecommendedTrack;
import com.gpsy.domain.DbUserPlaylist;
import com.gpsy.mapper.spotify.SpotifyPlaylistMapper;
import com.gpsy.mapper.spotify.TrackMapper;
import com.gpsy.mapper.spotify.UniversalMethods;
import com.gpsy.repository.spotify.SpotifyPopularTrackRepository;
import com.gpsy.repository.spotify.SpotifyRecentPlayedTrackRepository;
import com.gpsy.repository.spotify.SpotifyUserPlaylistsRepository;
import com.gpsy.externalApis.spotify.client.SpotifyClient;
import com.sun.org.apache.xml.internal.security.Init;
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
    private SpotifyPlaylistMapper spotifyPlaylistMapper;

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

        for(DbPopularTrack dbPopularTrack: storedTracks) {
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

    public List<DbPopularTrack> fetchPopularTracks() {
        savePopularTracks();
        return spotifyPopularTrackRepository.findAll().stream()
                .limit(InitialLimitValues.LIMIT_POPULAR)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }

    public List<DbRecentPlayedTrack> saveRecentPlayedTracks() {
        List<DbRecentPlayedTrack> savedTracks = new ArrayList<>();
        List<DbRecentPlayedTrack> storedTracks = spotifyRecentPlayedTrackRepository.findAll();
        Collections.sort(storedTracks, Collections.reverseOrder());
        List<PlayHistory> recentTracks = spotifyClient.getSpotifyRecentPlayedTracks();

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

        Collections.sort(savedTracks, Collections.reverseOrder());
        return savedTracks;
    }

    public List<DbUserPlaylist> saveUserPlaylists() {
        List<DbUserPlaylist> savedPlaylists = new ArrayList<>();
        List<DbUserPlaylist> dbUserPlaylists = spotifyUserPlaylistsRepository.findAll();
        List<PlaylistSimplified> spotifyUserPlaylists = spotifyClient.getUserPlaylists();

        for (PlaylistSimplified playlistSimplified : spotifyUserPlaylists) {
                if(!dbUserPlaylists.contains(spotifyPlaylistMapper.mapSpotifyPlaylistToDbUserPlaylist(playlistSimplified))) {
                    savedPlaylists.add(spotifyUserPlaylistsRepository.save(spotifyPlaylistMapper.mapSpotifyPlaylistToDbUserPlaylist(playlistSimplified)));
            }
        }

        if(dbUserPlaylists.size() !=0) {
            for (PlaylistSimplified playlistSimplified : spotifyUserPlaylists) {
                for (DbUserPlaylist dbUserPlaylist : dbUserPlaylists) {
                    if (dbUserPlaylist.getPlaylistStringId().equals(playlistSimplified.getId()) && dbUserPlaylist.getTracks().size() != playlistSimplified.getTracks().getTotal()) {
                        spotifyUserPlaylistsRepository.delete(dbUserPlaylist);
                        savedPlaylists.add(spotifyUserPlaylistsRepository.save(spotifyPlaylistMapper.mapSpotifyPlaylistToDbUserPlaylist(playlistSimplified)));
                    }

                    if(dbUserPlaylist.getPlaylistStringId().equals(playlistSimplified.getId()) && !dbUserPlaylist.getName().equals(playlistSimplified.getName())) {
                            DbUserPlaylist playlistToUpdate = spotifyUserPlaylistsRepository.findByPlaylistStringId(dbUserPlaylist.getPlaylistStringId());
                            dbUserPlaylist.setName(playlistSimplified.getName());
                            savedPlaylists.add(spotifyUserPlaylistsRepository.save(playlistToUpdate));
                    }
                }
            }
        }
        return savedPlaylists;
    }

    public List<DbRecommendedTrack> returnRecommendedTracks() {
        List<DbRecommendedTrack> recommendedTracks = new ArrayList<>();
        List<TrackSimplified> tracksSimplified = spotifyClient.getRecommendedTracks();
        recommendedTracks = tracksSimplified.stream()
                .limit(InitialLimitValues.LIMIT_RECOMMENDED_TRACKS_ON_BOARD)
                .map(track -> new DbRecommendedTrack(track.getId(), track.getName(), UniversalMethods.simplifyArtist(track.getArtists()).toString(), track.getPreviewUrl()))
                .collect(Collectors.toList());
        return recommendedTracks;
    }
}
