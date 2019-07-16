package com.gpsy.domain.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    public static class Builder {
        private String name;
        private String playlistStringId;
        private List<PlaylistTrackDto> tracks;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder stringId(String playlistStringId) {
            this.playlistStringId = playlistStringId;
            return this;
        }

        public Builder tracks(List<PlaylistTrackDto> tracks) {
            this.tracks = tracks;
            return this;
        }

        public UserPlaylistDto build() {
            return new UserPlaylistDto(name, playlistStringId, tracks);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPlaylistDto that = (UserPlaylistDto) o;

        return playlistStringId.equals(that.playlistStringId);
    }

    @Override
    public int hashCode() {
        return playlistStringId.hashCode();
    }
}
