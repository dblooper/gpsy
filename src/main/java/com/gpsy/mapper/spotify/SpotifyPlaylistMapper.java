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
    private SpotifyClient spotifyClient;

    @Autowired
    private TrackMapper trackMapper;

    public UserPlaylist mapSpotifyPlaylistToDbUserPlaylist(PlaylistSimplified playlistSimplified) {

        return new UserPlaylist.UserPlaylistBuilder()
                                .name(playlistSimplified.getName())
                                .stringId(playlistSimplified.getId())
                                .tracks(mapToPlaylistTracks(playlistSimplified.getId()))
                                .build();
    }

    public List<PlaylistTrack> mapToPlaylistTracks (String playlistId) {

        return spotifyClient.getPlaylistTracks(playlistId).stream()
                .map(track -> new PlaylistTrack.PlaylistTrackBuilder()
                                                .title(track.getTrack().getName())
                                                .artists(UniversalMappingMethods.simplifyArtist(track.getTrack().getArtists()))
                                                .stringId(track.getTrack().getId())
                                                .build())
                .collect(Collectors.toList());
    }

    public UserPlaylist mapToDbUserPlaylist(UserPlaylistDto userPlaylistDto) {
        return new UserPlaylist.UserPlaylistBuilder()
                                .stringId(userPlaylistDto.getPlaylistStringId())
                                .name(userPlaylistDto.getName())
                                .tracks(trackMapper.mapToPlaylistTrack(userPlaylistDto.getTracks()))
                                .build();
    }

}
