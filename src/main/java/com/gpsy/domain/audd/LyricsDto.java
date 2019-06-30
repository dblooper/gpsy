package com.gpsy.domain.audd;

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

    @JsonProperty(value = "artist")
    private String artist;

    @JsonProperty("lyrics")
    private String lyrics;
}
