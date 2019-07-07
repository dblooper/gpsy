package com.gpsy.mapper.spotify.database.mapper;

import com.gpsy.domain.spotify.MostFrequentTrack;
import com.gpsy.domain.spotify.DbMostFrequentTrackDto;
import org.springframework.stereotype.Component;

@Component
public class TrackDbMapper {

    public MostFrequentTrack mapToDbMostFrequentTrack(DbMostFrequentTrackDto dbMostFrequentTrackDto) {
        return new MostFrequentTrack(dbMostFrequentTrackDto.getTrack_ids(), dbMostFrequentTrackDto.getTitles(), dbMostFrequentTrackDto.getAuthors(), dbMostFrequentTrackDto.getPopularity());
    }
}
