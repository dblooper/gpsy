package com.gpsy.mapper.spotify;

import com.gpsy.domain.spotify.UserPlaylist;
import com.gpsy.domain.spotify.PlaylistTrack;
import com.gpsy.domain.spotify.RecommendedPlaylist;
import com.gpsy.domain.spotify.dto.PlaylistTrackDto;
import com.gpsy.domain.spotify.dto.RecommendedPlaylistDto;
import com.gpsy.domain.spotify.dto.UserPlaylistDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DbPlaylistMapper {

    @Autowired
    private TrackMapper trackMapper;

    public List<PlaylistTrackDto> mapToPlaylistTrackDtos(List<PlaylistTrack> userPlaylistTracks) {
        return userPlaylistTracks.stream()
                .map(track -> new PlaylistTrackDto.PlaylistTrackDtoBuilder()
                                                .stringId(track.getTrackStringId())
                                                .title(track.getTitle())
                                                .artists(track.getArtists())
                                                .build())
                .collect(Collectors.toList());
    }

    public List<UserPlaylistDto> mapToUserPlaylistsDto(List<UserPlaylist> userPlaylists) {
        return userPlaylists.stream()
                .map(playlist -> new UserPlaylistDto.UserPlaylistDtoBuilder()
                                                    .name(playlist.getName())
                                                    .stringId(playlist.getPlaylistStringId())
                                                    .tracks( mapToPlaylistTrackDtos(playlist.getTracks()))
                                                    .build())
                .collect(Collectors.toList());
    }

    public RecommendedPlaylistDto mapToRecommendedPlaylistDto(RecommendedPlaylist recommendedPlaylist) {
        return new RecommendedPlaylistDto.RecommendedPlaylistDtoBuilder()
                .name(recommendedPlaylist.getName())
                .stringId(recommendedPlaylist.getPlaylistStringId())
                .playlistTracks(trackMapper.mapToRecommendedTrackForPlaylistDto(recommendedPlaylist.getRecommendedPlaylistTracks()))
                .isActual(recommendedPlaylist.isActual())
                .numberOfTracks(recommendedPlaylist.getNumberOfTracks())
                .build();
    }

    public UserPlaylist mapRecommendedPlaylistToUserPlaylist(RecommendedPlaylist recommendedPlaylist) {
        return new UserPlaylist.UserPlaylistBuilder()
                                .name(recommendedPlaylist.getName())
                                .stringId(recommendedPlaylist.getPlaylistStringId())
                                .tracks(trackMapper.mapRecommendedPlaylistTracksToPlaylistTracks(recommendedPlaylist.getRecommendedPlaylistTracks()))
                                .build();
    }

}
