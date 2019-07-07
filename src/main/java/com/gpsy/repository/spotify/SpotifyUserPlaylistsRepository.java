package com.gpsy.repository.spotify;

import com.gpsy.domain.spotify.UserPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SpotifyUserPlaylistsRepository extends JpaRepository<UserPlaylist, Long> {

    UserPlaylist findByPlaylistStringId(String playlistID);
}
