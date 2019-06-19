package com.gpsy.repository;

import com.gpsy.domain.DbPopularTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotifyPopularTrackRepository extends JpaRepository<DbPopularTrack, Long> {

    @Override
    DbPopularTrack save(DbPopularTrack dbPopularTrack);

}
