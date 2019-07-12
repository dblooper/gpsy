package com.gpsy.domain.spotify.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Getter
public class SearchTrackDto {

    @JsonProperty("trackStringId")
    private String trackStringId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("artists")
    private String artists;

    @JsonProperty("sample")
    private String sample;

    private SearchTrackDto(String trackStringId, String title, String artists, String sample) {
        this.trackStringId = trackStringId;
        this.title = title;
        this.artists = artists;
        this.sample = sample;
    }

    public static class SearchTrackDtoBuilder {
        private String trackStringId;
        private String title;
        private String artists;
        private String sample;

        public SearchTrackDtoBuilder  stringId(String trackStringId) {
            this.trackStringId = trackStringId;
            return this;
        }

        public SearchTrackDtoBuilder  title(String title) {
            this.title = title;
            return this;
        }

        public SearchTrackDtoBuilder  aritsts(String artists) {
            this.artists = artists;
            return this;
        }

        public SearchTrackDtoBuilder  sample(String sample) {
            this.sample = sample;
            return this;
        }

        public SearchTrackDto  build() {
            return new SearchTrackDto(trackStringId, title, artists, sample);
        }
    }
}
