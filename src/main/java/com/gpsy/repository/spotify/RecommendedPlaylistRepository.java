package com.gpsy.repository.spotify;

import com.gpsy.domain.spotify.RecommendedPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendedPlaylistRepository extends JpaRepository<RecommendedPlaylist, Long> {

    RecommendedPlaylist findByActualTrue();
}
