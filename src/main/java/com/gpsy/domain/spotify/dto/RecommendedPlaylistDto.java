package com.gpsy.domain.spotify.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    public static class Builder {
        private String playlistStringId;
        private String name;
        private List<RecommendedTrackForPlaylistDto> playlistTracks;
        private Integer numberOfTracks;
        private boolean actual;

        public Builder stringId(String playlistStringId) {
            this.playlistStringId = playlistStringId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder playlistTracks(List<RecommendedTrackForPlaylistDto> playlistTracks) {
            this.playlistTracks = playlistTracks;
            return this;
        }

        public Builder numberOfTracks(int numberOfTracks) {
            this.numberOfTracks = numberOfTracks;
            return this;
        }

        public Builder isActual(boolean actual) {
            this.actual = actual;
            return this;
        }

        public RecommendedPlaylistDto build() {
            return new RecommendedPlaylistDto(playlistStringId, name, playlistTracks, numberOfTracks, actual);
        }
    }
}
