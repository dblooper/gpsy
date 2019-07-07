package com.gpsy.domain.lyrics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LibraryDto {

    private long id;

    private String libraryName;

    private List<LyricsInLibraryDto> lyrics;

}
