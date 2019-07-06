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

    public List<LyricsInLibrary> mapToLyricsInLibrary(List<LyricsInLibraryDto> lyricsInLibraryDtoList) {
        return lyricsInLibraryDtoList.stream()
                .map(lyricsInLibraryDto -> new LyricsInLibrary(lyricsInLibraryDto.getTitle(), lyricsInLibraryDto.getArtist(), lyricsInLibraryDto.getLyrics()))
                .collect(Collectors.toList());
    }

    public List<Library> mapToLibraries(List<LibraryDto> libraryDtoList) {
        return libraryDtoList.stream()
                .map(libraryDto -> new Library(libraryDto.getLibraryName(), mapToLyricsInLibrary(libraryDto.getLyrics()) ))
                .collect(Collectors.toList());
    }

    public Library mapToLibrary(LibraryDto libraryDto) {
        return new Library(libraryDto.getLibraryName(), mapToLyricsInLibrary(libraryDto.getLyrics()));
    }
}
