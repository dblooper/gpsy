package com.gpsy.repository;

import com.gpsy.domain.DbMostFrequentTrack;
import com.gpsy.domain.DbRecentPlayedTrack;
import com.gpsy.domain.dto.database.MostFrequentTrackDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotifyRecentPlayedTrackRepository extends JpaRepository<DbRecentPlayedTrack, Long> {

    @Query
    List<MostFrequentTrackDto> retrieveWeekMostPopularTrack();

}
