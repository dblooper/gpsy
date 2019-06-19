package com.gpsy.mapper;

import com.gpsy.domain.DbUserPlaylist;
import com.gpsy.domain.PlaylistTrack;
import com.gpsy.spotify.client.SpotifyClient;
import com.wrapper.spotify.model_objects.miscellaneous.PlaylistTracksInformation;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlaylistMapper {

    @Autowired
    SpotifyClient spotifyClient;

    public DbUserPlaylist mapSpotifyPlaylistToDbUserPlaylist(PlaylistSimplified playlistSimplified) {

        return new DbUserPlaylist(playlistSimplified.getName(), playlistSimplified.getId(), mapToPlaylistTracks(playlistSimplified.getId()));
    }

    public List<PlaylistTrack> mapToPlaylistTracks (String playlistId) {

        return spotifyClient.getPlaylistTracks(playlistId).stream()
                .map(track -> new PlaylistTrack(track.getTrack().getId(),track.getTrack().getName(), UniversalMethods.simplifyArtist(track.getTrack().getArtists()).toString()))
                .collect(Collectors.toList());


    }
}
