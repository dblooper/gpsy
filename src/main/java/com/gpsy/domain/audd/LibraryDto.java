package com.gpsy.domain.audd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LibraryDto {

    private String libraryName;

    private List<LyricsInLibraryDto> lyrics;

}
