package com.gpsy.mapper.lyrics;

import com.gpsy.domain.lyrics.Library;
import com.gpsy.domain.lyrics.dto.LibraryDto;
import com.gpsy.domain.lyrics.LyricsInLibrary;
import com.gpsy.domain.lyrics.dto.LyricsInLibraryDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LibraryMapper {

    public List<LibraryDto> mapToLibraryDto(List<Library> libraryList) {
        return libraryList.stream()
                .map(library -> new LibraryDto(library.getLibraryId(), library.getLibraryName(), mapToLyricsInLibraryDto(library.getLyrics())))
                .collect(Collectors.toList());
    }

    public List<LyricsInLibraryDto> mapToLyricsInLibraryDto(List<LyricsInLibrary> lyricsInLibraryList) {
        return lyricsInLibraryList.stream()
                .map(lyrics -> new LyricsInLibraryDto(lyrics.getTitle(), lyrics.getArtists(), lyrics.getLyrics()))
                .collect(Collectors.toList());
    }

    public List<LyricsInLibrary> mapToLyricsInLibrary(List<LyricsInLibraryDto> lyricsInLibraryDtoList) {
        return lyricsInLibraryDtoList.stream()
                .map(lyricsInLibraryDto -> new LyricsInLibrary(lyricsInLibraryDto.getTitle(), lyricsInLibraryDto.getArtists(), lyricsInLibraryDto.getLyrics()))
                .collect(Collectors.toList());
    }

    public Library mapToLibrary(LibraryDto libraryDto) {
        return new Library(libraryDto.getId(), libraryDto.getLibraryName(), mapToLyricsInLibrary(libraryDto.getLyrics()));
    }

    public Library mapToSaveLibrary(LibraryDto libraryDto) {
        return new Library(libraryDto.getLibraryName(), mapToLyricsInLibrary(libraryDto.getLyrics()));
    }
}
