package com.gpsy.service.spotify;

import com.gpsy.domain.DbUserPlaylist;
import com.gpsy.externalApis.spotify.client.SpotifyClient;
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

    public DbUserPlaylist updatePlaylistName(DbUserPlaylist newDbUserPlaylist) {
        if(!newDbUserPlaylist.getPlaylistStringId().equals("2ptqwasYqv1677gL4OEkIL")) {
            spotifyClient.updatePlaylistDetails(newDbUserPlaylist);
        }
        return newDbUserPlaylist;
    }
}
