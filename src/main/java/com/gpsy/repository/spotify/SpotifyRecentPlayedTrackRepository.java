package com.gpsy.repository.spotify;

import com.gpsy.domain.spotify.RecentPlayedTrack;
import com.gpsy.domain.spotify.DbMostFrequentTrackCalc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface SpotifyRecentPlayedTrackRepository extends JpaRepository<RecentPlayedTrack, Long> {

    @Query
    List<DbMostFrequentTrackCalc> retrieveWeekMostPopularTrack();

}

