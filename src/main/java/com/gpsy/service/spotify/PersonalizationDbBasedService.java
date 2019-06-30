package com.gpsy.service.spotify;

import com.gpsy.domain.DbMostFrequentTrack;
import com.gpsy.domain.dto.MostFrequentTrackDto;
import com.gpsy.mapper.spotify.database.mapper.TrackDbMapper;
import com.gpsy.repository.spotify.DbPopularWeekTracksRepository;
import com.gpsy.repository.spotify.SpotifyRecentPlayedTrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PersonalizationDbBasedService {

    @Autowired
    SpotifyRecentPlayedTrackRepository spotifyRecentPlayedTrackRepository;

    @Autowired
    DbPopularWeekTracksRepository dbPopularWeekTracksRepository;

    @Autowired
    TrackDbMapper trackDbMapper;

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
        dbMostFrequentTracks.addAll(dbPopularWeekTracksRepository.findAll());
        dbMostFrequentTracks.sort(Collections.reverseOrder());

        return dbMostFrequentTracks;
    }
}
