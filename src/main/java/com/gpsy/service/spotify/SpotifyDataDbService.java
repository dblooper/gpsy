package com.gpsy.service.spotify;

import com.gpsy.config.InitialLimitValues;
import com.gpsy.domain.spotify.PopularTrack;
import com.gpsy.domain.spotify.RecentPlayedTrack;
import com.gpsy.domain.spotify.RecommendedTrack;
import com.gpsy.domain.spotify.UserPlaylist;
import com.gpsy.mapper.spotify.SpotifyPlaylistMapper;
import com.gpsy.mapper.spotify.TrackMapper;
import com.gpsy.mapper.spotify.UniversalMappingMethods;
import com.gpsy.repository.spotify.SpotifyPopularTrackRepository;
import com.gpsy.repository.spotify.SpotifyRecentPlayedTrackRepository;
import com.gpsy.repository.spotify.SpotifyUserPlaylistsRepository;
import com.gpsy.externalApis.spotify.client.SpotifyClient;
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


    public List<PopularTrack> savePopularTracks() {
        List<PopularTrack> savedTracks = new ArrayList<>();
        List<PopularTrack> storedTracks = spotifyPopularTrackRepository.findAll();
        List<Track> spotifyStoredTracks = spotifyClient.getSpotifyPopularTracks();
        if(storedTracks.size() == 0) {
            for (Track spotifyTrack : spotifyClient.getSpotifyPopularTracks()) {
                savedTracks.add(spotifyPopularTrackRepository.save(trackMapper.mapSpotifyTrackToDbPopularTrack(spotifyTrack)));
            }
            Collections.sort(savedTracks, Collections.reverseOrder());
            return savedTracks;
        }

        for(PopularTrack popularTrack : storedTracks) {
            for (Track spotifyTrack : spotifyStoredTracks) {
                    if(popularTrack.getTrackId().equals(spotifyTrack.getId()) && popularTrack.getPopularity() != spotifyTrack.getPopularity()){
                        popularTrack.setPopularity(spotifyTrack.getPopularity());
                        savedTracks.add(spotifyPopularTrackRepository.save(popularTrack));
                    }else if(!storedTracks.contains(trackMapper.mapSpotifyTrackToDbPopularTrack(spotifyTrack))) {
                        savedTracks.add(spotifyPopularTrackRepository.save(trackMapper.mapSpotifyTrackToDbPopularTrack(spotifyTrack)));
                    }
            }
        }
        Collections.sort(savedTracks, Collections.reverseOrder());
        return savedTracks;
    }

    public List<PopularTrack> fetchPopularTracks() {
        savePopularTracks();
        return spotifyPopularTrackRepository.findAll().stream()
                .limit(InitialLimitValues.LIMIT_POPULAR)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }

    public List<RecentPlayedTrack> saveRecentPlayedTracks() {
        List<RecentPlayedTrack> savedTracks = new ArrayList<>();
        List<RecentPlayedTrack> storedTracks = spotifyRecentPlayedTrackRepository.findAll();
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

    public List<UserPlaylist> saveUserPlaylists() {
        List<UserPlaylist> savedPlaylists = new ArrayList<>();
        List<UserPlaylist> userPlaylists = spotifyUserPlaylistsRepository.findAll();
        List<PlaylistSimplified> spotifyUserPlaylists = spotifyClient.getUserPlaylists();

        for (PlaylistSimplified playlistSimplified : spotifyUserPlaylists) {
                if(!userPlaylists.contains(spotifyPlaylistMapper.mapSpotifyPlaylistToDbUserPlaylist(playlistSimplified))) {
                    savedPlaylists.add(spotifyUserPlaylistsRepository.save(spotifyPlaylistMapper.mapSpotifyPlaylistToDbUserPlaylist(playlistSimplified)));
            }
        }

        if(userPlaylists.size() !=0) {
            for (PlaylistSimplified playlistSimplified : spotifyUserPlaylists) {
                for (UserPlaylist userPlaylist : userPlaylists) {
                    if (userPlaylist.getPlaylistStringId().equals(playlistSimplified.getId()) && userPlaylist.getTracks().size() != playlistSimplified.getTracks().getTotal()) {
                        spotifyUserPlaylistsRepository.delete(userPlaylist);
                        savedPlaylists.add(spotifyUserPlaylistsRepository.save(spotifyPlaylistMapper.mapSpotifyPlaylistToDbUserPlaylist(playlistSimplified)));
                    }

                    if(userPlaylist.getPlaylistStringId().equals(playlistSimplified.getId()) && !userPlaylist.getName().equals(playlistSimplified.getName())) {
                            UserPlaylist playlistToUpdate = spotifyUserPlaylistsRepository.findByPlaylistStringId(userPlaylist.getPlaylistStringId());
                            userPlaylist.setName(playlistSimplified.getName());
                            savedPlaylists.add(spotifyUserPlaylistsRepository.save(playlistToUpdate));
                    }
                }
            }
        }
        return savedPlaylists;
    }

    public List<RecommendedTrack> returnRecommendedTracks() {
        List<RecommendedTrack> recommendedTracks = new ArrayList<>();
        List<TrackSimplified> tracksSimplified = spotifyClient.getRecommendedTracks();
        recommendedTracks = tracksSimplified.stream()
                .limit(InitialLimitValues.LIMIT_RECOMMENDED_TRACKS_ON_BOARD)
                .map(track -> new RecommendedTrack(track.getId(), track.getName(), UniversalMappingMethods.simplifyArtist(track.getArtists()).toString(), track.getPreviewUrl()))
                .collect(Collectors.toList());
        return recommendedTracks;
    }
}
