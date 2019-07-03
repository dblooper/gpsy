package com.gpsy.mapper.spotify;

import com.gpsy.domain.DbUserPlaylist;
import com.gpsy.domain.PlaylistTrack;
import com.gpsy.domain.dto.UserPlaylistDto;
import com.gpsy.externalApis.spotify.client.SpotifyClient;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SpotifyPlaylistMapper {

    @Autowired
    SpotifyClient spotifyClient;

    @Autowired
    TrackMapper trackMapper;

    public DbUserPlaylist mapSpotifyPlaylistToDbUserPlaylist(PlaylistSimplified playlistSimplified) {

        return new DbUserPlaylist(playlistSimplified.getName(), playlistSimplified.getId(), mapToPlaylistTracks(playlistSimplified.getId()));
    }

    public List<PlaylistTrack> mapToPlaylistTracks (String playlistId) {

        return spotifyClient.getPlaylistTracks(playlistId).stream()
                .map(track -> new PlaylistTrack(track.getTrack().getId(),track.getTrack().getName(), UniversalMethods.simplifyArtist(track.getTrack().getArtists()).toString()))
                .collect(Collectors.toList());
    }

    public DbUserPlaylist mapToDbUserPlaylist(UserPlaylistDto userPlaylistDto) {
        return new DbUserPlaylist(userPlaylistDto.getName(), userPlaylistDto.getPlaylistStringId(),trackMapper.mapToPlaylistTrack(userPlaylistDto.getTracks()));
    }

}
