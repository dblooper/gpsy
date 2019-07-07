package com.gpsy.repository.spotify;

import com.gpsy.domain.spotify.MostFrequentTrack;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface DbMostFrequentTracksRepository extends JpaRepository<MostFrequentTrack, Long> {

    List<MostFrequentTrack> findAllByPopularityGreaterThanOrderByPopularityDesc(int quantity, Pageable pageable);
}
