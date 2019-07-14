package com.gpsy.controller;

import com.gpsy.domain.lyrics.dto.LibraryDto;
import com.gpsy.domain.lyrics.dto.LyricsDto;
import com.gpsy.exceptions.LibraryNotFoundException;
import com.gpsy.facade.LyricsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/v1/gpsy")
@CrossOrigin("*")
public class LyricsController {

    @Autowired
    private LyricsFacade lyricsFacade;

    @GetMapping(value = "/musixmatch/lyrics")
    public LyricsDto fetchLyricsFromApi(@RequestParam @Size(min = 1, max = 20) String title,
                                        @RequestParam @Size(min = 1, max = 20) String author) {
        return lyricsFacade.fetchLyricsFromApi(title, author);
    }

    @GetMapping(value = "/libraries/get")
    public List<LibraryDto> fetchLibraries() {
        return lyricsFacade.fetchLibraries();
    }

    @PostMapping(value = "/libraries/new")
    public void addLibrary(@RequestBody LibraryDto libraryDto) {
        lyricsFacade.addLibrary(libraryDto);
    }

    @PostMapping(value = "/libraries/lyrics/add")
    public void addLyricsToLibrary(@RequestBody LibraryDto libraryDto) throws LibraryNotFoundException {
        lyricsFacade.addLyricsToLibrary(libraryDto);
    }

    @PutMapping(value = "/libraries/update")
    public LibraryDto updateLibraryName(@RequestBody LibraryDto libraryDto) throws LibraryNotFoundException {
        return lyricsFacade.updateLibraryName(libraryDto);
    }

    @DeleteMapping(value = "/libraries/lyrics/delete")
    public void deleteLyricsFromLibrary(@RequestBody LibraryDto libraryDto) throws LibraryNotFoundException {
        lyricsFacade.deleteLyricsFromLibrary(libraryDto);
    }

    @DeleteMapping(value = "/libraries/delete")
    public void deleteLibrary(@RequestParam long libraryId) {
        lyricsFacade.deleteLibrary(libraryId);
    }
}
