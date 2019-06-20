package com.gpsy.repository;

import com.gpsy.domain.DbMostFrequentTrack;
import com.gpsy.domain.DbRecentPlayedTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface DbPopularWeekTracksRepository extends JpaRepository<DbMostFrequentTrack, Long> {
}
