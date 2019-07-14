package com.gpsy.domain.lyrics.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

//@Component
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackInfoForLyricsDto {

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "artists")
    private String artists;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrackInfoForLyricsDto that = (TrackInfoForLyricsDto) o;

        if (!title.equals(that.title)) return false;
        return artists.equals(that.artists);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + artists.hashCode();
        return result;
    }
}
