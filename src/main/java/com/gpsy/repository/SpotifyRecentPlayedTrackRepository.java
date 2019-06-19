package com.gpsy.repository;

import com.gpsy.domain.DbRecentPlayedTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotifyRecentPlayedTrackRepository extends JpaRepository<DbRecentPlayedTrack, Long> {

}
