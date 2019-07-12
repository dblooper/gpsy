package com.gpsy.domain.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPlaylistDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("playlistStringId")
    private String playlistStringId;

    @JsonProperty("tracks")
    private List<PlaylistTrackDto> tracks;


    private UserPlaylistDto(String name, String playlistStringId, List<PlaylistTrackDto> tracks) {
        this.name = name;
        this.playlistStringId = playlistStringId;
        this.tracks = tracks;
    }

    public static class UserPlaylistDtoBuilder {
        private String name;
        private String playlistStringId;
        private List<PlaylistTrackDto> tracks;

        public UserPlaylistDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserPlaylistDtoBuilder stringId(String playlistStringId) {
            this.playlistStringId = playlistStringId;
            return this;
        }

        public UserPlaylistDtoBuilder tracks(List<PlaylistTrackDto> tracks) {
            this.tracks = tracks;
            return this;
        }

        public UserPlaylistDto build() {
            return new UserPlaylistDto(name, playlistStringId, tracks);
        }
    }
}
