package com.gpsy.repository.spotify;

import com.gpsy.domain.RecommendedPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface RecommendedPlaylistRepository extends JpaRepository<RecommendedPlaylist, Long> {

    List<RecommendedPlaylist> findAllByOrderByIdDesc();

    RecommendedPlaylist findByActualTrue();
}
