package com.gpsy.repository;

import com.gpsy.domain.DbRecentPlayedTrack;
import com.gpsy.domain.dto.MostFrequentTrackDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface SpotifyRecentPlayedTrackRepository extends JpaRepository<DbRecentPlayedTrack, Long> {

    @Query
    List<MostFrequentTrackDto> retrieveWeekMostPopularTrack();

}

