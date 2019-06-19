package com.gpsy.repository;

import com.gpsy.domain.DbTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotifyRepository extends JpaRepository<DbTrack, Long> {

    @Override
    DbTrack save(DbTrack dbTrack);

}
