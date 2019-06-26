package com.gpsy.mapper;

import com.gpsy.domain.DbUserPlaylist;
import com.gpsy.domain.PlaylistTrack;
import com.gpsy.domain.dto.PlaylistTrackDto;
import com.gpsy.domain.dto.UserPlaylistDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DbPlaylistMapper {

    public List<PlaylistTrackDto> mapToPlaylistTrackDtos(List<PlaylistTrack> userPlaylistTracks) {
        return userPlaylistTracks.stream()
                .map(track -> new PlaylistTrackDto(track.getTrackStringId(), track.getTitle(), track.getAuthors()))
                .collect(Collectors.toList());
    }

    public List<UserPlaylistDto> mapToUserPlaylistsDto(List<DbUserPlaylist> dbUserPlaylists) {

        return dbUserPlaylists.stream()
                .map(playlist -> new UserPlaylistDto(playlist.getName(), playlist.getPlaylistStringId(), mapToPlaylistTrackDtos(playlist.getTracks())))
                .collect(Collectors.toList());
    }

}
