package com.gpsy.domain.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MostFrequentTrackDto {

    @JsonProperty(value = "trackStringId")
    private String trackStringId;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "artists")
    private String artists;

    @JsonProperty(value = "popularity")
    private Integer popularity;

    private MostFrequentTrackDto(String trackStringId, String title, String artists, Integer popularity) {
        this.trackStringId = trackStringId;
        this.title = title;
        this.artists = artists;
        this.popularity = popularity;
    }

    public static class MostFrequentTrackDtoBuilder {
        private String trackStringId;
        private String title;
        private String artists;
        private Integer popularity;

        public MostFrequentTrackDtoBuilder stringId(String trackStringId) {
            this.trackStringId = trackStringId;
            return this;
        }

        public MostFrequentTrackDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public MostFrequentTrackDtoBuilder artists(String artists) {
            this.artists = artists;
            return this;
        }

        public MostFrequentTrackDtoBuilder popularity(int popularity) {
            this.popularity = popularity;
            return this;
        }

        public MostFrequentTrackDto build() {
            return new MostFrequentTrackDto(trackStringId, title, artists, popularity);
        }
    }
}
