package com.gpsy.domain.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaylistTrackDto {

    @JsonProperty("trackStringId")
    private String trackStringId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("artists")
    private String artists;

    private PlaylistTrackDto(String trackStringId, String title, String artists) {
        this.trackStringId = trackStringId;
        this.title = title;
        this.artists = artists;
    }

    public static class PlaylistTrackDtoBuilder {
        private String trackStringId;
        private String title;
        private String artists;

        public PlaylistTrackDtoBuilder stringId(String trackStringId) {
            this.trackStringId = trackStringId;
            return this;
        }

        public PlaylistTrackDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public PlaylistTrackDtoBuilder artists(String artists) {
            this.artists = artists;
            return this;
        }

        public PlaylistTrackDto build() {
            return new  PlaylistTrackDto(trackStringId, title, artists);
        }
    }
}
