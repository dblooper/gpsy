package com.gpsy.repository;

import com.gpsy.domain.DbUserPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotifyUserPlaylistsRepository extends JpaRepository<DbUserPlaylist, Long> {
}
