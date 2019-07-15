package com.gpsy.mapper.spotify.database;

import com.gpsy.domain.spotify.DbMostFrequentTrackCalc;
import com.gpsy.domain.spotify.MostFrequentTrack;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrackDbMapperTestSuite {

    @Autowired
    private TrackDbMapper trackDbMapper;

    @Test
    public void mapToMostFrequentTrackTest() {
        //Given
        DbMostFrequentTrackCalc dbMostFrequentTrackCalc = new DbMostFrequentTrackCalc("0987", "Title1", "Artist1", 25);

        //When
        MostFrequentTrack mostFrequentTrack = trackDbMapper.mapToMostFrequentTrack(dbMostFrequentTrackCalc);

        //Then
        assertEquals("0987", mostFrequentTrack.getTrackStringId());
        assertEquals("Title1", mostFrequentTrack.getTitle());
        assertEquals("Artist1", mostFrequentTrack.getArtists());
        assertEquals(25, mostFrequentTrack.getPopularity().intValue());
    }
}