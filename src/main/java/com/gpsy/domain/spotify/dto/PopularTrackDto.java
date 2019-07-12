package com.gpsy.domain.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PopularTrackDto {

    @JsonProperty(value = "trackStringId")
    private String trackStringId;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "aritsts")
    private String artists;

    @JsonProperty(value = "popularity")
    private int popularity;

    private PopularTrackDto(String trackStringId, String title, String artists, int popularity) {
        this.trackStringId = trackStringId;
        this.title = title;
        this.artists = artists;
        this.popularity = popularity;
    }

    public static class PopularTrackDtoBuilder {
        private String trackStringId;
        private String title;
        private String artists;
        private int popularity;

        public PopularTrackDtoBuilder stirngId(String trackStringId) {
            this.trackStringId = trackStringId;
            return this;
        }

        public PopularTrackDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public PopularTrackDtoBuilder artists(String artists) {
            this.artists = artists;
            return this;
        }

        public PopularTrackDtoBuilder popularity(int popularity) {
            this.popularity = popularity;
            return this;
        }

        public PopularTrackDto build() {
            return new PopularTrackDto(trackStringId, title, artists, popularity);
        }
    }
}
