package com.gpsy.domain.spotify.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class RecommendedTrackForPlaylistDto {

    @JsonProperty("trackStringId")
    private String trackStringId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("artists")
    private String artists;

    @JsonProperty("sample")
    private String sample;

    private RecommendedTrackForPlaylistDto(String trackStringId, String title, String artists, String sample) {
        this.trackStringId = trackStringId;
        this.title = title;
        this.artists = artists;
        this.sample = sample;
    }

    public static class RecommendedTrackForPlaylistDtoBuilder {
        private String trackStringId;
        private String title;
        private String artists;
        private String sample;

        public RecommendedTrackForPlaylistDtoBuilder stringId(String trackStringId) {
            this.trackStringId = trackStringId;
            return this;
        }

        public RecommendedTrackForPlaylistDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public RecommendedTrackForPlaylistDtoBuilder aritsts(String artists) {
            this.artists = artists;
            return this;
        }

        public RecommendedTrackForPlaylistDtoBuilder sample(String sample) {
            this.sample = sample;
            return this;
        }

        public RecommendedTrackForPlaylistDto build() {
            return new RecommendedTrackForPlaylistDto(trackStringId, title, artists, sample);
        }
    }
}
