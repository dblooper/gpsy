package com.gpsy.domain.lyrics.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LyricsInLibraryDto extends LyricsDto{

    public LyricsInLibraryDto(String title, String artist, String lyrics) {
        super(title, artist, lyrics);
    }
}
