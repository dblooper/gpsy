package com.gpsy.service;

import com.gpsy.domain.DbUserPlaylist;
import com.gpsy.spotify.client.SpotifyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpotifyHandleService {

    @Autowired
    private SpotifyClient spotifyClient;

    public DbUserPlaylist updatePlaylistTracks(DbUserPlaylist dbUserPlaylist) {
        spotifyClient.updatePlaylistTracks(dbUserPlaylist);
        return dbUserPlaylist;
    }

    public DbUserPlaylist deletePlaylistTrack(DbUserPlaylist dbUserPlaylist) {
        spotifyClient.deletePlaylistTrack(dbUserPlaylist);
        return dbUserPlaylist;
    }
}
