package com.gpsy.repository.spotify;

import com.gpsy.domain.DbMostFrequentTrack;
import com.gpsy.domain.DbRecentPlayedTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface DbPopularWeekTracksRepository extends JpaRepository<DbMostFrequentTrack, Long> {
}
