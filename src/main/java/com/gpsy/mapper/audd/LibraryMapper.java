package com.gpsy.mapper.audd;

import com.gpsy.domain.audd.Library;
import com.gpsy.domain.audd.LibraryDto;
import com.gpsy.domain.audd.LyricsInLibrary;
import com.gpsy.domain.audd.LyricsInLibraryDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LibraryMapper {

    public List<LibraryDto> mapToLibraryDto(List<Library> libraryList) {
        return libraryList.stream()
                .map(library -> new LibraryDto(library.getLibraryName(), mapToLyricsInLibraryDto(library.getLyrics())))
                .collect(Collectors.toList());
    }

    public List<LyricsInLibraryDto> mapToLyricsInLibraryDto(List<LyricsInLibrary> lyricsInLibraryList) {
        return lyricsInLibraryList.stream()
                .map(lyrics -> new LyricsInLibraryDto(lyrics.getTitle(), lyrics.getArtist(), lyrics.getLyrics()))
                .collect(Collectors.toList());
    }
}
