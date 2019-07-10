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
                .map(track -> new PlaylistTrackDto(track.getTrackStringId(), track.getTitle(), track.getArtists()))
                .collect(Collectors.toList());
    }

    public List<UserPlaylistDto> mapToUserPlaylistsDto(List<UserPlaylist> userPlaylists) {
        return userPlaylists.stream()
                .map(playlist -> new UserPlaylistDto(playlist.getName(), playlist.getPlaylistStringId(), mapToPlaylistTrackDtos(playlist.getTracks())))
                .collect(Collectors.toList());
    }

    public RecommendedPlaylistDto mapToRecommendedPlaylistDto(RecommendedPlaylist recommendedPlaylist) {
        return new RecommendedPlaylistDto(recommendedPlaylist.getPlaylistStringId(), recommendedPlaylist.getName(), trackMapper.mapToRecommendedTrackForPlaylistDto(recommendedPlaylist.getRecommendedPlaylistTracks()), recommendedPlaylist.getNumberOfTracks(), recommendedPlaylist.isActual());
    }

}
