package com.gpsy.domain.lyrics.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LibraryDto {

    @JsonProperty(value = "id")
    private long id;

    @JsonProperty(value = "libraryName")
    private String libraryName;

    @JsonProperty(value = "lyrics")

    private List<LyricsInLibraryDto> lyrics;

}
