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

    @JsonProperty(value = "authors")
    private String authors;
}
