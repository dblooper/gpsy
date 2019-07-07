package com.gpsy.controller;

import com.gpsy.domain.lyrics.dto.LibraryDto;
import com.gpsy.domain.lyrics.dto.LyricsDto;
import com.gpsy.exceptions.LibraryNotFoundException;
import com.gpsy.exceptions.MusiXmatchServerResponseException;
import com.gpsy.mapper.lyrics.LibraryMapper;
import com.gpsy.mapper.lyrics.LyricsMapper;
import com.gpsy.service.musiXmatch.MusiXmatchService;
import com.gpsy.service.lyrics.LyricsLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/gpsy")
public class LyricsController {

    @Autowired
    private MusiXmatchService musiXmatchService;

    @Autowired
    private LyricsMapper lyricsMapper;

    @Autowired
    private LyricsLibraryService lyricsLibraryService;

    @Autowired
    private LibraryMapper libraryMapper;

    @GetMapping(value = "/audd/getLyrics")
    public LyricsDto getLyricsFromApi(@RequestParam String title, @RequestParam String author) throws MusiXmatchServerResponseException {
        return lyricsMapper.mapToLyricsDto(musiXmatchService.fetchLirycs(title, author));
    }

    @GetMapping(value = "/library/getLibraries")
    public List<LibraryDto> fetchLibraries() {
        return libraryMapper.mapToLibraryDto(lyricsLibraryService.fetchLibrariesFromDb());
    }

    @PostMapping(value = "/library/add")
    public LibraryDto addLibrary(@RequestBody LibraryDto libraryDto) {
        lyricsLibraryService.saveLibrary(libraryMapper.mapToSaveLibrary(libraryDto));
        return libraryDto;
    }

    @PostMapping(value = "/library/lyrics/add")
    public LibraryDto addLyricsToLibrary(@RequestBody LibraryDto libraryDto) throws LibraryNotFoundException {
        lyricsLibraryService.saveLyricsInLibrary(libraryMapper.mapToLibrary(libraryDto));
        return libraryDto;
    }

    @PutMapping(value = "/library/update")
    public LibraryDto updateLibraryName(@RequestBody LibraryDto libraryDto) throws LibraryNotFoundException {
        lyricsLibraryService.updateLibrary(libraryMapper.mapToLibrary(libraryDto));
        return libraryDto;
    }

    @DeleteMapping(value = "/library/lyrics/delete")
    public void deleteLyricsFromLibrary(@RequestBody LibraryDto libraryDto) throws LibraryNotFoundException {
        lyricsLibraryService.deleteLyricsFromLibrary(libraryMapper.mapToLibrary(libraryDto));
    }


    @DeleteMapping(value = "/library/delete")
    public void deleteLibrary(@RequestParam long libraryId) {
        lyricsLibraryService.deleteLibrary(libraryId);
    }
}
