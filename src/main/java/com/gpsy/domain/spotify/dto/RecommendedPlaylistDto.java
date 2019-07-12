package com.gpsy.domain.spotify.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gpsy.domain.spotify.RecommendedPlaylist;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class RecommendedPlaylistDto {

    @JsonProperty(value = "playlistStringId")
    private String playlistStringId;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "playlistTracks")
    private List<RecommendedTrackForPlaylistDto> playlistTracks;

    @JsonProperty(value = "numberOfTracks")
    private Integer numberOfTracks;

    @JsonProperty(value = "actual")
    private boolean actual;

    private RecommendedPlaylistDto(String playlistStringId, String name, List<RecommendedTrackForPlaylistDto> playlistTracks, Integer numberOfTracks, boolean actual) {
        this.playlistStringId = playlistStringId;
        this.name = name;
        this.playlistTracks = playlistTracks;
        this.numberOfTracks = numberOfTracks;
        this.actual = actual;
    }

    public static class RecommendedPlaylistDtoBuilder {
        private String playlistStringId;
        private String name;
        private List<RecommendedTrackForPlaylistDto> playlistTracks;
        private Integer numberOfTracks;
        private boolean actual;

        public RecommendedPlaylistDtoBuilder stringId(String playlistStringId) {
            this.playlistStringId = playlistStringId;
            return this;
        }

        public RecommendedPlaylistDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RecommendedPlaylistDtoBuilder playlistTracks(List<RecommendedTrackForPlaylistDto> playlistTracks) {
            this.playlistTracks = playlistTracks;
            return this;
        }

        public RecommendedPlaylistDtoBuilder numberOfTracks(int numberOfTracks) {
            this.numberOfTracks = numberOfTracks;
            return this;
        }

        public RecommendedPlaylistDtoBuilder isActual(boolean actual) {
            this.actual = actual;
            return this;
        }

        public RecommendedPlaylistDto build() {
            return new RecommendedPlaylistDto(playlistStringId, name, playlistTracks, numberOfTracks, actual);
        }
    }
}
