package com.gpsy.domain.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public static class Builder {
        private String trackStringId;
        private String title;
        private String artists;

        public Builder stringId(String trackStringId) {
            this.trackStringId = trackStringId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder artists(String artists) {
            this.artists = artists;
            return this;
        }

        public PlaylistTrackDto build() {
            return new  PlaylistTrackDto(trackStringId, title, artists);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaylistTrackDto that = (PlaylistTrackDto) o;

        return trackStringId.equals(that.trackStringId);
    }

    @Override
    public int hashCode() {
        return trackStringId.hashCode();
    }
}
