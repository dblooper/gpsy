package com.gpsy.domain.audd;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class LibraryDto {

    private String libraryName;

    private List<LyricsInLibraryDto> lyrics;

}
