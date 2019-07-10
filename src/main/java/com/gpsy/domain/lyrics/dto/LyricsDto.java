package com.gpsy.domain.lyrics.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LyricsDto {

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "artists")
    private String artists;

    @JsonProperty(value = "lyrics")
    private String lyrics;

    @Override
    public String toString() {
        return "LyricsDto{" +
                "title='" + title + '\'' +
                ", artists='" + artists + '\'' +
                ", body='" + lyrics + '\'' +
                '}';
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
