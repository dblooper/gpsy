package com.gpsy.repository.spotify;

import com.gpsy.domain.spotify.RecommendedPlaylistTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RecommendedPlaylistTracksRepository extends JpaRepository<RecommendedPlaylistTrack, Long> {

}
