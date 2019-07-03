package com.gpsy.mapper.spotify.database.mapper;

import com.gpsy.domain.DbMostFrequentTrack;
import com.gpsy.domain.dto.DbMostFrequentTrackDto;
import org.springframework.stereotype.Component;

@Component
public class TrackDbMapper {

    public DbMostFrequentTrack mapToDbMostFrequentTrack(DbMostFrequentTrackDto dbMostFrequentTrackDto) {
        return new DbMostFrequentTrack(dbMostFrequentTrackDto.getTrack_ids(), dbMostFrequentTrackDto.getTitles(), dbMostFrequentTrackDto.getAuthors(), dbMostFrequentTrackDto.getPopularity());
    }
}
