package com.gpsy.service.spotify;

import com.gpsy.config.InitialLimitValues;
import com.gpsy.domain.spotify.DbMostFrequentTrackDto;
import com.gpsy.domain.spotify.MostFrequentTrack;
import com.gpsy.domain.spotify.RecommendedPlaylist;
import com.gpsy.domain.spotify.RecommendedPlaylistTrack;
import com.gpsy.externalApis.spotify.client.SpotifyClient;
import com.gpsy.mapper.spotify.TrackMapper;
import com.gpsy.mapper.spotify.UniversalMappingMethods;
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

    public List<MostFrequentTrack> saveSpotifyByDbDataMostFrequentTracks() {
        List<DbMostFrequentTrackDto> dbMostFrequentTrackDtos = spotifyRecentPlayedTrackRepository.retrieveWeekMostPopularTrack();
        List<MostFrequentTrack> dbMostFrequentTracksFrom = dbMostFrequentTracksRepository.findAll();
        List<MostFrequentTrack> retireveMostFrequentTracksResult = new ArrayList<>();

        if(dbMostFrequentTracksFrom.size() == 0) {
            for(DbMostFrequentTrackDto dbMostFrequentTrackDto : dbMostFrequentTrackDtos) {
                retireveMostFrequentTracksResult.add(dbMostFrequentTracksRepository.save(trackDbMapper.mapToDbMostFrequentTrack(dbMostFrequentTrackDto)));
            }
            return retireveMostFrequentTracksResult;
        }

        for(DbMostFrequentTrackDto dbMostFrequentTrackDto : dbMostFrequentTrackDtos){

            if(!dbMostFrequentTracksFrom.contains(trackDbMapper.mapToDbMostFrequentTrack(dbMostFrequentTrackDto))) { ;
                retireveMostFrequentTracksResult.add(dbMostFrequentTracksRepository.save(trackDbMapper.mapToDbMostFrequentTrack(dbMostFrequentTrackDto)));
            }

            for(MostFrequentTrack mostFrequentTrackFromTable : dbMostFrequentTracksFrom) {
                if(trackDbMapper.mapToDbMostFrequentTrack(dbMostFrequentTrackDto).getTrackId().equals(mostFrequentTrackFromTable.getTrackId())
                        && dbMostFrequentTrackDto.getPopularity() != mostFrequentTrackFromTable.getPopularity()) {
                    mostFrequentTrackFromTable.setPopularity(trackDbMapper.mapToDbMostFrequentTrack(dbMostFrequentTrackDto).getPopularity());
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

        if(numberOfTracks > 50 || numberOfTracks < 0 ) return new RecommendedPlaylist("2ptqwasYqv1677gL4OEkIL","Tygodniowka", new ArrayList<>(),  false);
        List<RecommendedPlaylistTrack> recommendedTracks = spotifyClient.getRecommendedTracks().stream()
                .limit(numberOfTracks)
                .map(track -> new RecommendedPlaylistTrack(track.getId(), track.getName(), UniversalMappingMethods.simplifyArtist(track.getArtists()).toString(), track.getPreviewUrl()))
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
