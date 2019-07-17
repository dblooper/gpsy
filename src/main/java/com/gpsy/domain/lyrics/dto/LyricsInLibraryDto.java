package com.gpsy.domain.lyrics.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LyricsInLibraryDto extends LyricsDto{

    public LyricsInLibraryDto(String title, String artist, String lyrics) {
        super(title, artist, lyrics);
    }
}
