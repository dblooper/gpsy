package com.gpsy.repository.spotify;

import com.gpsy.domain.spotify.PopularTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotifyPopularTrackRepository extends JpaRepository<PopularTrack, Long> {

    @Override
    PopularTrack save(PopularTrack popularTrack);

}
