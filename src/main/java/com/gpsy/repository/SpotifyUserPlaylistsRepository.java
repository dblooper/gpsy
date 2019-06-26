package com.gpsy.repository;

import com.gpsy.domain.DbUserPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SpotifyUserPlaylistsRepository extends JpaRepository<DbUserPlaylist, Long> {
}
