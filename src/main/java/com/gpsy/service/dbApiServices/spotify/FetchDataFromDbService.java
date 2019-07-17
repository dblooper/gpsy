package com.gpsy.service.dbApiServices.spotify;

import com.gpsy.config.InitialLimitValues;
import com.gpsy.domain.spotify.*;
import com.gpsy.externalApis.spotify.client.SpotifyClient;
import com.gpsy.mapper.spotify.TrackMapper;
import com.gpsy.mapper.spotify.database.TrackDbMapper;
import com.gpsy.repository.spotify.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FetchDataFromDbService {

    @Autowired
    private SpotifyRecentPlayedTrackRepository spotifyRecentPlayedTrackRepository;

    @Autowired
    private SpotifyPopularTrackRepository spotifyPopularTrackRepository;

    @Autowired
    private SpotifyUserPlaylistsRepository spotifyUserPlaylistsRepository;

    @Autowired
    private DbMostFrequentTracksRepository dbMostFrequentTracksRepository;

    @Autowired
    private RecommendedPlaylistRepository recommendedPlaylistRepository;

    @Autowired
    private SaveSpotifyDataToDbService saveSpotifyDataToDbService;

    @Autowired
    private SpotifyClient spotifyClient;

    @Autowired
    private TrackDbMapper trackDbMapper;

    @Autowired
    private TrackMapper trackMapper;

    public List<PopularTrack> fetchPopularTracks(int qty) {
        saveSpotifyDataToDbService.savePopularTracks();
        return spotifyPopularTrackRepository.findAll().stream()
                .limit(qty)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }

    public List<RecentPlayedTrack> fetchRecentPlayedTracks(int qty) {
        saveSpotifyDataToDbService.saveRecentPlayedTracks();
        List<RecentPlayedTrack> recentPlayedTracks = spotifyRecentPlayedTrackRepository.findAll();
        Collections.sort(recentPlayedTracks, Collections.reverseOrder());
        return recentPlayedTracks.stream()
                .limit(qty)
                .collect(Collectors.toList());
    }

    public List<UserPlaylist> fetchUserPlaylists() {
        saveSpotifyDataToDbService.saveUserPlaylists();
        return spotifyUserPlaylistsRepository.findAll();
    }

    public void saveSpotifyByDbDataMostFrequentTracks() {
        List<DbMostFrequentTrackCalc> dbMostFrequentTrackCalcs = spotifyRecentPlayedTrackRepository.retrieveWeekMostPopularTrack();
        List<MostFrequentTrack> dbMostFrequentTracksFrom = dbMostFrequentTracksRepository.findAll();

        if(dbMostFrequentTracksFrom.size() == 0) {
            for(DbMostFrequentTrackCalc dbMostFrequentTrackCalc : dbMostFrequentTrackCalcs) {
                dbMostFrequentTracksRepository.save(trackDbMapper.mapToMostFrequentTrack(dbMostFrequentTrackCalc));
            }
        }

        for(DbMostFrequentTrackCalc dbMostFrequentTrackCalc : dbMostFrequentTrackCalcs){

            if(!dbMostFrequentTracksFrom.contains(trackDbMapper.mapToMostFrequentTrack(dbMostFrequentTrackCalc))) { ;
                dbMostFrequentTracksRepository.save(trackDbMapper.mapToMostFrequentTrack(dbMostFrequentTrackCalc));
            }

            for(MostFrequentTrack mostFrequentTrackFromTable : dbMostFrequentTracksFrom) {
                if(trackDbMapper.mapToMostFrequentTrack(dbMostFrequentTrackCalc).getTrackStringId().equals(mostFrequentTrackFromTable.getTrackStringId())
                        && dbMostFrequentTrackCalc.getPopularity() != mostFrequentTrackFromTable.getPopularity()) {
                    mostFrequentTrackFromTable.setPopularity(trackDbMapper.mapToMostFrequentTrack(dbMostFrequentTrackCalc).getPopularity());
                    dbMostFrequentTracksRepository.save(mostFrequentTrackFromTable);
                }
            }
        }
    }

    public List<MostFrequentTrack> fetchMostFrequentTracks(int qty) {
        List<MostFrequentTrack> mostFrequentTracks = new ArrayList<>();
        saveSpotifyByDbDataMostFrequentTracks();
        Pageable pageable = PageRequest.of(0, qty);
        mostFrequentTracks.addAll(dbMostFrequentTracksRepository.findAllByPopularityGreaterThanOrderByPopularityDesc(0, pageable));
        mostFrequentTracks.sort(Collections.reverseOrder());
        return mostFrequentTracks;
    }

    public RecommendedPlaylist updateFetchRecommendedPlaylistFromDb(int numberOfTracks) {

        List<RecommendedPlaylistTrack> recommendedTracks = trackMapper.mapToRecommendedPlaylistTracks(spotifyClient.getRecommendedTracks(numberOfTracks), numberOfTracks);

        List<RecommendedPlaylist> recommendedPlaylists = recommendedPlaylistRepository.findAll();

            if(recommendedPlaylists.size() == 0) {
                return recommendedPlaylistRepository.save(new RecommendedPlaylist.Builder()
                                                                                 .stringId(InitialLimitValues.RECOMMENDED_PLAYLIST_ID)
                                                                                 .name(InitialLimitValues.RECOMMENDED_PLAYLIST_NAME)
                                                                                 .actual(true)
                                                                                 .playlistTracks(recommendedTracks)
                                                                                 .build());
            } else {
                RecommendedPlaylist playlistToEdit = recommendedPlaylistRepository.findByActualTrue();
                playlistToEdit.setActual(false);

                return recommendedPlaylistRepository.save(new RecommendedPlaylist.Builder()
                        .stringId(InitialLimitValues.RECOMMENDED_PLAYLIST_ID)
                        .name(InitialLimitValues.RECOMMENDED_PLAYLIST_NAME)
                        .actual(true)
                        .playlistTracks(recommendedTracks)
                        .build());
            }
    }

    public RecommendedPlaylist changeNumberOfTracks(int numberOfTracks) {
        RecommendedPlaylist recommendedPlaylistsToEdit = recommendedPlaylistRepository.findByActualTrue();
        List<RecommendedPlaylistTrack> recommendedTracksToLimit = recommendedPlaylistRepository.findByActualTrue()
                                                                                                .getRecommendedPlaylistTracks().stream()
                                                                                                .limit(numberOfTracks)
                                                                                                .collect(Collectors.toList());
        recommendedPlaylistsToEdit.setRecommendedPlaylistTracks(recommendedTracksToLimit);
        recommendedPlaylistsToEdit.setNumberOfTracks();
        recommendedPlaylistRepository.save(recommendedPlaylistsToEdit);
        return recommendedPlaylistsToEdit;
    }

    public RecommendedPlaylist fetchRecommendedPlaylist() {
        return Optional.ofNullable(recommendedPlaylistRepository.findByActualTrue()).orElse(new RecommendedPlaylist.Builder()
                                                                                                                    .stringId("n/a")
                                                                                                                    .name("n/a")
                                                                                                                    .actual(false)
                                                                                                                    .playlistTracks(new ArrayList<>())
                                                                                                                    .build());
    }
}
