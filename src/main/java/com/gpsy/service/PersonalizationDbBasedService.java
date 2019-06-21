package com.gpsy.service;

import com.gpsy.domain.DbMostFrequentTrack;
import com.gpsy.domain.dto.database.MostFrequentTrackDto;
import com.gpsy.domain.dto.database.mapper.TrackDbMapper;
import com.gpsy.repository.DbPopularWeekTracksRepository;
import com.gpsy.repository.SpotifyRecentPlayedTrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        dbMostFrequentTracks.addAll(dbPopularWeekTracksRepository.findAll());

        return dbMostFrequentTracks;
    }
}
