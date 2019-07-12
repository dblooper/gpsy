package com.gpsy.domain.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendedTrackDto {

    @JsonProperty("trackStringId")
    private String trackStringId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("artists")
    private String artists;

    @JsonProperty("sample")
    private String sample;

    private RecommendedTrackDto(String trackStringId, String title, String artists, String sample) {
        this.trackStringId = trackStringId;
        this.title = title;
        this.artists = artists;
        this.sample = sample;
    }

    public static class RecommendedTrackDtoBuilder {
        private String trackStringId;
        private String title;
        private String artists;
        private String sample;

        public RecommendedTrackDtoBuilder stringId(String trackStringId) {
            this.trackStringId = trackStringId;
            return this;
        }

        public RecommendedTrackDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public RecommendedTrackDtoBuilder aritsts(String artists) {
            this.artists = artists;
            return this;
        }

        public RecommendedTrackDtoBuilder sample(String sample) {
            this.sample = sample;
            return this;
        }

        public RecommendedTrackDto buidl() {
            return new RecommendedTrackDto(trackStringId, title, artists, sample);
        }
    }
}
