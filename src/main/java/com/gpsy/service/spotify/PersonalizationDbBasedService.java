package com.gpsy.service.spotify;

import com.gpsy.config.InitialLimitValues;
import com.gpsy.domain.*;
import com.gpsy.domain.dto.MostFrequentTrackDto;
import com.gpsy.externalApis.spotify.client.SpotifyClient;
import com.gpsy.mapper.spotify.TrackMapper;
import com.gpsy.mapper.spotify.UniversalMethods;
import com.gpsy.mapper.spotify.database.mapper.TrackDbMapper;
import com.gpsy.repository.spotify.DbPopularWeekTracksRepository;
import com.gpsy.repository.spotify.RecommendedPlaylistRepository;
import com.gpsy.repository.spotify.SpotifyRecentPlayedTrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonalizationDbBasedService {

    @Autowired
    private SpotifyRecentPlayedTrackRepository spotifyRecentPlayedTrackRepository;

    @Autowired
    private DbPopularWeekTracksRepository dbPopularWeekTracksRepository;

    @Autowired
    private RecommendedPlaylistRepository recommendedPlaylistRepository;

    @Autowired
    private SpotifyClient spotifyClient;

    @Autowired
    private TrackDbMapper trackDbMapper;

    @Autowired
    private TrackMapper trackMapper;

    public List<DbMostFrequentTrack> saveSpotifyByDbDataMostFrequentTracks() {
        List<MostFrequentTrackDto> mostFrequentTrackDtos = spotifyRecentPlayedTrackRepository.retrieveWeekMostPopularTrack();
        List<DbMostFrequentTrack> dbMostFrequentTracksFromDb = dbPopularWeekTracksRepository.findAll();
        List<DbMostFrequentTrack> retireveDbMostFrequentTracksResult = new ArrayList<>();

        if(dbMostFrequentTracksFromDb.size() == 0) {
            for(MostFrequentTrackDto mostFrequentTrackDto: mostFrequentTrackDtos) {
                retireveDbMostFrequentTracksResult.add(dbPopularWeekTracksRepository.save(trackDbMapper.mapToDbMostFrequentTrack(mostFrequentTrackDto)));
            }
            return retireveDbMostFrequentTracksResult;
        }

        for(MostFrequentTrackDto mostFrequentTrackDto: mostFrequentTrackDtos){

            if(!dbMostFrequentTracksFromDb.contains(trackDbMapper.mapToDbMostFrequentTrack(mostFrequentTrackDto))) { ;
                retireveDbMostFrequentTracksResult.add(dbPopularWeekTracksRepository.save(trackDbMapper.mapToDbMostFrequentTrack(mostFrequentTrackDto)));
            }

            for(DbMostFrequentTrack dbMostFrequentTrackFromTable: dbMostFrequentTracksFromDb) {
                if(trackDbMapper.mapToDbMostFrequentTrack(mostFrequentTrackDto).getTrack_ids().equals(dbMostFrequentTrackFromTable.getTrack_ids())
                        && mostFrequentTrackDto.getPopularity() != dbMostFrequentTrackFromTable.getPopularity()) {
                    dbMostFrequentTrackFromTable.setPopularity(trackDbMapper.mapToDbMostFrequentTrack(mostFrequentTrackDto).getPopularity());
                    retireveDbMostFrequentTracksResult.add(dbPopularWeekTracksRepository.save(dbMostFrequentTrackFromTable));
                }
            }
        }

       return retireveDbMostFrequentTracksResult;
    }

    public List<DbMostFrequentTrack> getMostPopularTracks() {
        List<DbMostFrequentTrack> dbMostFrequentTracks = new ArrayList<>();
        saveSpotifyByDbDataMostFrequentTracks();
        Pageable pageable = PageRequest.of(0, InitialLimitValues.LIMIT_POPULAR);
        dbMostFrequentTracks.addAll(dbPopularWeekTracksRepository.findAllByPopularityGreaterThanOrderByPopularityDesc(0, pageable));
        dbMostFrequentTracks.sort(Collections.reverseOrder());
        return dbMostFrequentTracks;
    }

    public RecommendedPlaylist fetchRecommendedPlaylistFromDb(int numberOfTracks) {
        if(numberOfTracks > 50 || numberOfTracks < 0 ) return new RecommendedPlaylist("2ptqwasYqv1677gL4OEkIL","Tygodniowka", new ArrayList<>(), numberOfTracks, false);
        List<RecommendedTrackForPlaylist> recommendedTracks = spotifyClient.getRecommendedTracks().stream()
                .limit(numberOfTracks)
                .map(track -> new RecommendedTrackForPlaylist(track.getId(), track.getName(), UniversalMethods.simplifyArtist(track.getArtists()).toString(), track.getPreviewUrl()))
                .collect(Collectors.toList());
        List<RecommendedPlaylist> recommendedPlaylists = recommendedPlaylistRepository.findAll();
            if(recommendedPlaylists.size() == 0) {
                return recommendedPlaylistRepository.save(new RecommendedPlaylist("2ptqwasYqv1677gL4OEkIL","Tygodniowka", recommendedTracks, numberOfTracks, true));
            } else {
                RecommendedPlaylist playlistToEdit = recommendedPlaylistRepository.findByActualTrue();
                playlistToEdit.setActual(false);
                return recommendedPlaylistRepository.save(new RecommendedPlaylist("2ptqwasYqv1677gL4OEkIL","Tygodniowka", recommendedTracks, numberOfTracks, true));
            }
    }

    public RecommendedPlaylist changeNumberOfTracks(Integer numberOfTracks) {
//        List<DbRecommendedTrack> recommendedTracks = spotifyClient.getRecommendedTracks().stream()
//                .limit(numberOfTracks)
//                .map(track -> new DbRecommendedTrack(track.getId(), track.getName(), UniversalMethods.simplifyArtist(track.getArtists()).toString(), track.getPreviewUrl()))
//                .collect(Collectors.toList());
        RecommendedPlaylist recommendedPlaylistsToEdit = recommendedPlaylistRepository.findByActualTrue();
        List<RecommendedTrackForPlaylist> recommendedTracksToLimit = recommendedPlaylistsToEdit.getRecommendedTracksForPlaylist().stream()
                .limit(numberOfTracks)
                .collect(Collectors.toList());

        return recommendedPlaylistRepository.save(new RecommendedPlaylist(recommendedPlaylistsToEdit.getPlaylistStringId(),recommendedPlaylistsToEdit.getName(), recommendedTracksToLimit, numberOfTracks, true));
    }
}
