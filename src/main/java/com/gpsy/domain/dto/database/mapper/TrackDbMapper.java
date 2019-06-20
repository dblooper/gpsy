package com.gpsy.domain.dto.database.mapper;

import com.gpsy.domain.DbMostFrequentTrack;
import com.gpsy.domain.dto.database.MostFrequentTrackDto;
import org.springframework.stereotype.Component;

@Component
public class TrackDbMapper {

    public DbMostFrequentTrack mapToDbMostFrequentTrack(MostFrequentTrackDto mostFrequentTrackDto) {
        return new DbMostFrequentTrack(mostFrequentTrackDto.getTrack_ids(), mostFrequentTrackDto.getPopularity());
    }
}
