package com.gpsy.domain.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecentPlayedTrackDto {

    @JsonProperty(value = "trackStringId")
    private String trackStringId;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "aritsts")
    private String artists;

    @JsonProperty(value = "playDate")
    private String playDate;

    public RecentPlayedTrackDto(String trackStringId, String title, String artists, LocalDateTime playDate) {
        this.trackStringId = trackStringId;
        this.title = title;
        this.artists = artists;
        this.playDate = playDate.format(DateTimeFormatter.ofPattern("HH:mm:ss MM-dd"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecentPlayedTrackDto that = (RecentPlayedTrackDto) o;

        return trackStringId.equals(that.trackStringId);

    }

    @Override
    public int hashCode() {
        return trackStringId.hashCode();
    }
}
