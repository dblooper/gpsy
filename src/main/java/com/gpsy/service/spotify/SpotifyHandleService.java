package com.gpsy.service.spotify;

import com.gpsy.domain.spotify.UserPlaylist;
import com.gpsy.domain.spotify.dto.SearchTrackDto;
import com.gpsy.externalApis.spotify.client.SpotifyClient;
import com.gpsy.mapper.spotify.TrackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpotifyHandleService {

    @Autowired
    private SpotifyClient spotifyClient;

    @Autowired
    private SpotifyDataDbService spotifyDataDbService;

    @Autowired
    private TrackMapper trackMapper;

    public UserPlaylist updatePlaylistTracks(UserPlaylist userPlaylist) {
        spotifyClient.updatePlaylistTracks(userPlaylist);
        return userPlaylist;
    }

    public UserPlaylist deletePlaylistTrack(UserPlaylist userPlaylist) {
        spotifyClient.deletePlaylistTrack(userPlaylist);
        return userPlaylist;
    }

    public UserPlaylist updatePlaylistName(UserPlaylist newUserPlaylist) {
        if(!newUserPlaylist.getPlaylistStringId().equals("2ptqwasYqv1677gL4OEkIL")) {
            spotifyClient.updatePlaylistDetails(newUserPlaylist);
        }
        return newUserPlaylist;
    }

    public UserPlaylist createPlaylist(UserPlaylist userPlaylist) {
        spotifyClient.createPlaylist(userPlaylist);
        return spotifyDataDbService.saveUserPlaylists().get(0);
    }

    public List<SearchTrackDto> searchForTracks(String searchedItem) {
        return trackMapper.mapToSearchTrackDto(spotifyClient.searchForTrack(searchedItem));
    }
}
