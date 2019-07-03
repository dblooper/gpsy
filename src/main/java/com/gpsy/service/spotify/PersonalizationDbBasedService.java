package com.gpsy.service.spotify;

import com.gpsy.config.InitialLimitValues;
import com.gpsy.domain.*;
import com.gpsy.domain.dto.DbMostFrequentTrackDto;
import com.gpsy.externalApis.spotify.client.SpotifyClient;
import com.gpsy.mapper.spotify.TrackMapper;
import com.gpsy.mapper.spotify.UniversalMethods;
import com.gpsy.mapper.spotify.database.mapper.TrackDbMapper;
import com.gpsy.repository.spotify.DbMostFrequentTracksRepository;
import com.gpsy.repository.spotify.RecommendedPlaylistRepository;
import com.gpsy.repository.spotify.SpotifyRecentPlayedTrackRepository;
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
public class PersonalizationDbBasedService {

    @Autowired
    private SpotifyRecentPlayedTrackRepository spotifyRecentPlayedTrackRepository;

    @Autowired
    private DbMostFrequentTracksRepository dbMostFrequentTracksRepository;

    @Autowired
    private RecommendedPlaylistRepository recommendedPlaylistRepository;

    @Autowired
    private SpotifyClient spotifyClient;

    @Autowired
    private TrackDbMapper trackDbMapper;

    @Autowired
    private TrackMapper trackMapper;

    public List<DbMostFrequentTrack> saveSpotifyByDbDataMostFrequentTracks() {
        List<DbMostFrequentTrackDto> dbMostFrequentTrackDtos = spotifyRecentPlayedTrackRepository.retrieveWeekMostPopularTrack();
        List<DbMostFrequentTrack> dbMostFrequentTracksFromDb = dbMostFrequentTracksRepository.findAll();
        List<DbMostFrequentTrack> retireveDbMostFrequentTracksResult = new ArrayList<>();

        if(dbMostFrequentTracksFromDb.size() == 0) {
            for(DbMostFrequentTrackDto dbMostFrequentTrackDto : dbMostFrequentTrackDtos) {
                retireveDbMostFrequentTracksResult.add(dbMostFrequentTracksRepository.save(trackDbMapper.mapToDbMostFrequentTrack(dbMostFrequentTrackDto)));
            }
            return retireveDbMostFrequentTracksResult;
        }

        for(DbMostFrequentTrackDto dbMostFrequentTrackDto : dbMostFrequentTrackDtos){

            if(!dbMostFrequentTracksFromDb.contains(trackDbMapper.mapToDbMostFrequentTrack(dbMostFrequentTrackDto))) { ;
                retireveDbMostFrequentTracksResult.add(dbMostFrequentTracksRepository.save(trackDbMapper.mapToDbMostFrequentTrack(dbMostFrequentTrackDto)));
            }

            for(DbMostFrequentTrack dbMostFrequentTrackFromTable: dbMostFrequentTracksFromDb) {
                if(trackDbMapper.mapToDbMostFrequentTrack(dbMostFrequentTrackDto).getTrackId().equals(dbMostFrequentTrackFromTable.getTrackId())
                        && dbMostFrequentTrackDto.getPopularity() != dbMostFrequentTrackFromTable.getPopularity()) {
                    dbMostFrequentTrackFromTable.setPopularity(trackDbMapper.mapToDbMostFrequentTrack(dbMostFrequentTrackDto).getPopularity());
                    retireveDbMostFrequentTracksResult.add(dbMostFrequentTracksRepository.save(dbMostFrequentTrackFromTable));
                }
            }
        }

       return retireveDbMostFrequentTracksResult;
    }

    public List<DbMostFrequentTrack> fetchMostFrequentTracks() {
        List<DbMostFrequentTrack> dbMostFrequentTracks = new ArrayList<>();
        saveSpotifyByDbDataMostFrequentTracks();
        Pageable pageable = PageRequest.of(0, InitialLimitValues.LIMIT_POPULAR);
        dbMostFrequentTracks.addAll(dbMostFrequentTracksRepository.findAllByPopularityGreaterThanOrderByPopularityDesc(0, pageable));
        dbMostFrequentTracks.sort(Collections.reverseOrder());
        return dbMostFrequentTracks;
    }

    public RecommendedPlaylist updateFetchRecommendedPlaylistFromDb(int numberOfTracks) {

        if(numberOfTracks > 50 || numberOfTracks < 0 ) return new RecommendedPlaylist("2ptqwasYqv1677gL4OEkIL","Tygodniowka", new ArrayList<>(),  false);
        List<RecommendedPlaylistTrack> recommendedTracks = spotifyClient.getRecommendedTracks().stream()
                .limit(numberOfTracks)
                .map(track -> new RecommendedPlaylistTrack(track.getId(), track.getName(), UniversalMethods.simplifyArtist(track.getArtists()).toString(), track.getPreviewUrl()))
                .collect(Collectors.toList());
        List<RecommendedPlaylist> recommendedPlaylists = recommendedPlaylistRepository.findAll();
            if(recommendedPlaylists.size() == 0) {
                return recommendedPlaylistRepository.save(new RecommendedPlaylist("2ptqwasYqv1677gL4OEkIL","Tygodniowka", recommendedTracks,  true));
            } else {
                RecommendedPlaylist playlistToEdit = recommendedPlaylistRepository.findByActualTrue();
                playlistToEdit.setActual(false);
                return recommendedPlaylistRepository.save(new RecommendedPlaylist("2ptqwasYqv1677gL4OEkIL","Tygodniowka", recommendedTracks,  true));
            }
    }

    public RecommendedPlaylist changeNumberOfTracks(int numberOfTracks) {
        RecommendedPlaylist recommendedPlaylistsToEdit = recommendedPlaylistRepository.findByActualTrue();
        List<RecommendedPlaylistTrack> recommendedTracksToLimit = recommendedPlaylistRepository.findByActualTrue().getRecommendedPlaylistTracks().stream()
                .limit(numberOfTracks)
                .collect(Collectors.toList());
        recommendedPlaylistsToEdit.setRecommendedPlaylistTracks(recommendedTracksToLimit);
        recommendedPlaylistsToEdit.setNumberOfTracks();
        return recommendedPlaylistRepository.save(recommendedPlaylistsToEdit);
    }

    public RecommendedPlaylist fetchRecommendedPlaylist() {

        return Optional.ofNullable(recommendedPlaylistRepository.findByActualTrue()).orElse(new RecommendedPlaylist("n/a","n/a",new ArrayList<>(),false));
    }
}
