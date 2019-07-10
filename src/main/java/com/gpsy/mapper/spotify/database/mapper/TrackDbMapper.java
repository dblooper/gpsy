package com.gpsy.mapper.spotify.database.mapper;

import com.gpsy.domain.spotify.MostFrequentTrack;
import com.gpsy.domain.spotify.DbMostFrequentTrackCalc;
import org.springframework.stereotype.Component;

@Component
public class TrackDbMapper {

    public MostFrequentTrack mapToMostFrequentTrack(DbMostFrequentTrackCalc dbMostFrequentTrackCalc) {
        return new MostFrequentTrack.MostFrequentTrackBuilder().stringId(dbMostFrequentTrackCalc.getTrackStringId())
                                                                .title(dbMostFrequentTrackCalc.getTitle())
                                                                .artists(dbMostFrequentTrackCalc.getArtists())
                                                                .popularity(dbMostFrequentTrackCalc.getPopularity())
                                                                .build();
    }
}
