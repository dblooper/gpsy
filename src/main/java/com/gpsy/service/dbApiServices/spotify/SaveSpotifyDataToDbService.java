package com.gpsy.service.dbApiServices.spotify;

import com.gpsy.domain.spotify.PopularTrack;
import com.gpsy.domain.spotify.RecentPlayedTrack;
import com.gpsy.domain.spotify.UserPlaylist;
import com.gpsy.mapper.spotify.SpotifyPlaylistMapper;
import com.gpsy.mapper.spotify.TrackMapper;
import com.gpsy.repository.spotify.SpotifyPopularTrackRepository;
import com.gpsy.repository.spotify.SpotifyRecentPlayedTrackRepository;
import com.gpsy.repository.spotify.SpotifyUserPlaylistsRepository;
import com.gpsy.externalApis.spotify.client.SpotifyClient;
import com.wrapper.spotify.model_objects.specification.PlayHistory;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@EnableScheduling
public class SaveSpotifyDataToDbService {

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

    public void savePopularTracks() {
        List<PopularTrack> savedTracks = new ArrayList<>();
        List<PopularTrack> storedTracks = spotifyPopularTrackRepository.findAll();
        List<Track> spotifyStoredTracks = spotifyClient.getSpotifyPopularTracks();
        if(storedTracks.size() == 0) {
            for (Track spotifyTrack : spotifyClient.getSpotifyPopularTracks()) {
                savedTracks.add(spotifyPopularTrackRepository.save(trackMapper.mapSpotifyTrackToDbPopularTrack(spotifyTrack)));
            }
            return;
        }

        for(PopularTrack popularTrack : storedTracks) {
            for (Track spotifyTrack : spotifyStoredTracks) {
                    if(popularTrack.getTrackStringId().equals(spotifyTrack.getId()) && popularTrack.getPopularity() != spotifyTrack.getPopularity()){
                        popularTrack.setPopularity(spotifyTrack.getPopularity());
                        savedTracks.add(spotifyPopularTrackRepository.save(popularTrack));
                    }else if(!storedTracks.contains(trackMapper.mapSpotifyTrackToDbPopularTrack(spotifyTrack))) {
                        savedTracks.add(spotifyPopularTrackRepository.save(trackMapper.mapSpotifyTrackToDbPopularTrack(spotifyTrack)));
                    }
            }
        }
    }

    public void saveRecentPlayedTracks() {
        List<RecentPlayedTrack> storedTracks = spotifyRecentPlayedTrackRepository.findAll();
        Collections.sort(storedTracks, Collections.reverseOrder());
        List<PlayHistory> recentTracks = spotifyClient.getSpotifyRecentPlayedTracks();

        if (storedTracks.size() == 0) {
            for (PlayHistory recentTrack : recentTracks) {
                spotifyRecentPlayedTrackRepository.save(trackMapper.mapSpotifyTrackToDbRecentPlayedTrack(recentTrack));
            }
            return;
        }

        for (PlayHistory spotifyTrack : recentTracks) {
                if(spotifyTrack.getPlayedAt().after(storedTracks.get(0).getPlayDate())) {
                    spotifyRecentPlayedTrackRepository.save(trackMapper.mapSpotifyTrackToDbRecentPlayedTrack(spotifyTrack));
                }
        }
    }

    void saveUserPlaylists() {
        List<UserPlaylist> userPlaylists = spotifyUserPlaylistsRepository.findAll();
        List<PlaylistSimplified> spotifyUserPlaylists = spotifyClient.getUserPlaylists();

        for (PlaylistSimplified playlistSimplified : spotifyUserPlaylists) {
                if(!userPlaylists.contains(spotifyPlaylistMapper.mapSpotifyPlaylistToDbUserPlaylist(playlistSimplified))) {
                    spotifyUserPlaylistsRepository.save(spotifyPlaylistMapper.mapSpotifyPlaylistToDbUserPlaylist(playlistSimplified));
            }
        }

        if(userPlaylists.size() !=0) {
            for (PlaylistSimplified playlistSimplified : spotifyUserPlaylists) {
                for (UserPlaylist userPlaylist : userPlaylists) {
                    if (userPlaylist.getPlaylistStringId().equals(playlistSimplified.getId()) && userPlaylist.getTracks().size() != playlistSimplified.getTracks().getTotal()) {
                        spotifyUserPlaylistsRepository.delete(userPlaylist);
                        spotifyUserPlaylistsRepository.save(spotifyPlaylistMapper.mapSpotifyPlaylistToDbUserPlaylist(playlistSimplified));
                    }

                    if(userPlaylist.getPlaylistStringId().equals(playlistSimplified.getId()) && !userPlaylist.getName().equals(playlistSimplified.getName())) {
                            UserPlaylist playlistToUpdate = spotifyUserPlaylistsRepository.findByPlaylistStringId(userPlaylist.getPlaylistStringId());
                            userPlaylist.setName(playlistSimplified.getName());
                            spotifyUserPlaylistsRepository.save(playlistToUpdate);
                    }
                }
            }
        }
    }
}
