package com.gpsy.mapper.spotify;

import com.gpsy.domain.spotify.UserPlaylist;
import com.gpsy.domain.spotify.PlaylistTrack;
import com.gpsy.domain.spotify.dto.UserPlaylistDto;
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

    public UserPlaylist mapSpotifyPlaylistToDbUserPlaylist(PlaylistSimplified playlistSimplified) {

        return new UserPlaylist(playlistSimplified.getName(), playlistSimplified.getId(), mapToPlaylistTracks(playlistSimplified.getId()));
    }

    public List<PlaylistTrack> mapToPlaylistTracks (String playlistId) {

        return spotifyClient.getPlaylistTracks(playlistId).stream()
                .map(track -> new PlaylistTrack(track.getTrack().getId(),track.getTrack().getName(), UniversalMappingMethods.simplifyArtist(track.getTrack().getArtists()).toString()))
                .collect(Collectors.toList());
    }

    public UserPlaylist mapToDbUserPlaylist(UserPlaylistDto userPlaylistDto) {
        return new UserPlaylist(userPlaylistDto.getName(), userPlaylistDto.getPlaylistStringId(),trackMapper.mapToPlaylistTrack(userPlaylistDto.getTracks()));
    }

}
