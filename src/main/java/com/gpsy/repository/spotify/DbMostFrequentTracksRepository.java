package com.gpsy.repository.spotify;

import com.gpsy.domain.DbMostFrequentTrack;
import com.gpsy.domain.DbRecentPlayedTrack;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface DbMostFrequentTracksRepository extends JpaRepository<DbMostFrequentTrack, Long> {

    List<DbMostFrequentTrack> findAllByPopularityGreaterThanOrderByPopularityDesc(int quantity, Pageable pageable);
}
