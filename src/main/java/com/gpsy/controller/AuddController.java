package com.gpsy.controller;

import com.gpsy.domain.audd.LibraryDto;
import com.gpsy.domain.audd.LyricsDto;
import com.gpsy.domain.audd.LyricsInLibrary;
import com.gpsy.domain.audd.LyricsInLibraryDto;
import com.gpsy.exceptions.LibraryNotFoundException;
import com.gpsy.exceptions.LyricsServerResponseException;
import com.gpsy.mapper.audd.LibraryMapper;
import com.gpsy.mapper.audd.LyricsMapper;
import com.gpsy.service.audd.AuddService;
import com.gpsy.service.audd.LyricsLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/gpsy")
public class AuddController {

    @Autowired
    private AuddService auddService;

    @Autowired
    private LyricsMapper lyricsMapper;

    @Autowired
    private LyricsLibraryService lyricsLibraryService;

    @Autowired
    private LibraryMapper libraryMapper;

    @GetMapping(value = "/audd/getLyrics")
    public LyricsDto getLyricsFromApi(@RequestParam String title, @RequestParam String author) throws LyricsServerResponseException {
        return lyricsMapper.mapToLyricsDto(auddService.fetchLirycs(title, author));
    }

    @GetMapping(value = "/library/getLibraries")
    public List<LibraryDto> fetchLibraries() {
        return libraryMapper.mapToLibraryDto(lyricsLibraryService.fetchLibrariesFromDb());
    }

    @PostMapping(value = "/library/add")
    public LibraryDto addLibrary(@RequestBody LibraryDto libraryDto) {
        lyricsLibraryService.saveLibrary(libraryMapper.mapToLibrary(libraryDto));
        return libraryDto;
    }

    @PostMapping(value = "/library/lyrics/add")
    public LibraryDto addLyricsToLibrary(@RequestBody LibraryDto libraryDto) throws LibraryNotFoundException {
        lyricsLibraryService.saveLyricsInLibrary(libraryMapper.mapToLibrary(libraryDto));
        return libraryDto;
    }

    @DeleteMapping(value = "/library/delete")
    public void deleteLibrary(@RequestBody LibraryDto libraryDto) {
        lyricsLibraryService.deleteLibrary(libraryMapper.mapToLibrary(libraryDto));
    }
}
