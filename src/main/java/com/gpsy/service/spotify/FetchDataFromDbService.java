package com.gpsy.service.spotify;

import com.gpsy.config.InitialLimitValues;
import com.gpsy.domain.spotify.*;
import com.gpsy.externalApis.spotify.client.SpotifyClient;
import com.gpsy.mapper.spotify.TrackMapper;
import com.gpsy.mapper.spotify.UniversalMappingMethods;
import com.gpsy.mapper.spotify.database.mapper.TrackDbMapper;
import com.gpsy.repository.spotify.DbMostFrequentTracksRepository;
import com.gpsy.repository.spotify.RecommendedPlaylistRepository;
import com.gpsy.repository.spotify.SpotifyRecentPlayedTrackRepository;
import com.gpsy.repository.spotify.SpotifyUserPlaylistsRepository;
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

    public List<RecentPlayedTrack> fetchRecentPlayedTracks() {
        saveSpotifyDataToDbService.saveRecentPlayedTracks();
        List<RecentPlayedTrack> recentPlayedTracks = spotifyRecentPlayedTrackRepository.findAll();
        Collections.sort(recentPlayedTracks, Collections.reverseOrder());
        List<RecentPlayedTrack> limitedRecentPlayedTracks = recentPlayedTracks.stream()
                .limit(InitialLimitValues.LIMIT_RECENT)
                .collect(Collectors.toList());
        return limitedRecentPlayedTracks;
    }

    public List<UserPlaylist> fetchUserPlaylists() {
        saveSpotifyDataToDbService.saveUserPlaylists();
        return spotifyUserPlaylistsRepository.findAll();
    }

    public List<MostFrequentTrack> saveSpotifyByDbDataMostFrequentTracks() {
        List<DbMostFrequentTrackCalc> dbMostFrequentTrackCalcs = spotifyRecentPlayedTrackRepository.retrieveWeekMostPopularTrack();
        List<MostFrequentTrack> dbMostFrequentTracksFrom = dbMostFrequentTracksRepository.findAll();
        List<MostFrequentTrack> retireveMostFrequentTracksResult = new ArrayList<>();

        if(dbMostFrequentTracksFrom.size() == 0) {
            for(DbMostFrequentTrackCalc dbMostFrequentTrackCalc : dbMostFrequentTrackCalcs) {
                retireveMostFrequentTracksResult.add(dbMostFrequentTracksRepository.save(trackDbMapper.mapToMostFrequentTrack(dbMostFrequentTrackCalc)));
            }
            return retireveMostFrequentTracksResult;
        }

        for(DbMostFrequentTrackCalc dbMostFrequentTrackCalc : dbMostFrequentTrackCalcs){

            if(!dbMostFrequentTracksFrom.contains(trackDbMapper.mapToMostFrequentTrack(dbMostFrequentTrackCalc))) { ;
                retireveMostFrequentTracksResult.add(dbMostFrequentTracksRepository.save(trackDbMapper.mapToMostFrequentTrack(dbMostFrequentTrackCalc)));
            }

            for(MostFrequentTrack mostFrequentTrackFromTable : dbMostFrequentTracksFrom) {
                if(trackDbMapper.mapToMostFrequentTrack(dbMostFrequentTrackCalc).getTrackStringId().equals(mostFrequentTrackFromTable.getTrackStringId())
                        && dbMostFrequentTrackCalc.getPopularity() != mostFrequentTrackFromTable.getPopularity()) {
                    mostFrequentTrackFromTable.setPopularity(trackDbMapper.mapToMostFrequentTrack(dbMostFrequentTrackCalc).getPopularity());
                    retireveMostFrequentTracksResult.add(dbMostFrequentTracksRepository.save(mostFrequentTrackFromTable));
                }
            }
        }

       return retireveMostFrequentTracksResult;
    }

    public List<MostFrequentTrack> fetchMostFrequentTracks() {
        List<MostFrequentTrack> mostFrequentTracks = new ArrayList<>();
        saveSpotifyByDbDataMostFrequentTracks();
        Pageable pageable = PageRequest.of(0, InitialLimitValues.LIMIT_POPULAR);
        mostFrequentTracks.addAll(dbMostFrequentTracksRepository.findAllByPopularityGreaterThanOrderByPopularityDesc(0, pageable));
        mostFrequentTracks.sort(Collections.reverseOrder());
        return mostFrequentTracks;
    }

    public RecommendedPlaylist updateFetchRecommendedPlaylistFromDb(int numberOfTracks) {

        if(numberOfTracks > 50 || numberOfTracks < 0 ) return new RecommendedPlaylist.RecommendedPlaylistBuilder()
                                                                                     .stringId(InitialLimitValues.RECOMMENDED_PLAYLIST_ID)
                                                                                     .name(InitialLimitValues.RECOMMENDED_PLAYLIST_NAME)
                                                                                     .actual(false)
                                                                                     .playlistTracks(new ArrayList<>())
                                                                                     .build();

        List<RecommendedPlaylistTrack> recommendedTracks = trackMapper.mapToRecommendedPlaylistTracks(spotifyClient.getRecommendedTracks(), numberOfTracks);

        List<RecommendedPlaylist> recommendedPlaylists = recommendedPlaylistRepository.findAll();

            if(recommendedPlaylists.size() == 0) {
                return recommendedPlaylistRepository.save(new RecommendedPlaylist.RecommendedPlaylistBuilder()
                                                                                 .stringId(InitialLimitValues.RECOMMENDED_PLAYLIST_ID)
                                                                                 .name(InitialLimitValues.RECOMMENDED_PLAYLIST_NAME)
                                                                                 .actual(true)
                                                                                 .playlistTracks(recommendedTracks)
                                                                                 .build());
            } else {
                RecommendedPlaylist playlistToEdit = recommendedPlaylistRepository.findByActualTrue();
                playlistToEdit.setActual(false);

                return recommendedPlaylistRepository.save(new RecommendedPlaylist.RecommendedPlaylistBuilder()
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
        return recommendedPlaylistRepository.save(recommendedPlaylistsToEdit);
    }

    public RecommendedPlaylist fetchRecommendedPlaylist() {
        return Optional.ofNullable(recommendedPlaylistRepository.findByActualTrue()).orElse(new RecommendedPlaylist.RecommendedPlaylistBuilder()
                                                                                                                    .stringId("n/a")
                                                                                                                    .name("n/a")
                                                                                                                    .actual(false)
                                                                                                                    .playlistTracks(new ArrayList<>())
                                                                                                                    .build());
    }
}
