package com.gpsy.facade;

import com.gpsy.domain.lyrics.DbLyrics;
import com.gpsy.domain.lyrics.dto.LibraryDto;
import com.gpsy.domain.lyrics.dto.LyricsDto;
import com.gpsy.exceptions.LibraryNotFoundException;
import com.gpsy.mapper.lyrics.LibraryMapper;
import com.gpsy.mapper.lyrics.LyricsMapper;
import com.gpsy.service.dbApiServices.lyrics.LyricsLibraryService;
import com.gpsy.service.dbApiServices.musiXmatch.MusiXmatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LyricsFacade {

    @Autowired
    private MusiXmatchService musiXmatchService;
    @Autowired
    private LyricsMapper lyricsMapper;
    @Autowired
    private LyricsLibraryService lyricsLibraryService;
    @Autowired
    private LibraryMapper libraryMapper;


    public LyricsDto fetchLyricsFromApi(String title, String author) {
        if(title.length() < 1 || author.length() < 1)
            return lyricsMapper.mapToLyricsDto(new DbLyrics("n/a", "n/a", "Your request parameters are too short!"));

        return lyricsMapper.mapToLyricsDto(musiXmatchService.fetchLirycs(title, author));
    }

    public List<LibraryDto> fetchLibraries() {
        return libraryMapper.mapToLibraryDto(lyricsLibraryService.fetchLibrariesFromDb());
    }

    public void addLibrary(LibraryDto libraryDto) {
        lyricsLibraryService.saveLibrary(libraryMapper.mapToSaveLibrary(libraryDto));
    }

    public void addLyricsToLibrary(LibraryDto libraryDto) throws LibraryNotFoundException {
        lyricsLibraryService.saveLyricsInLibrary(libraryMapper.mapToLibrary(libraryDto));
    }

    public LibraryDto updateLibraryName(LibraryDto libraryDto) throws LibraryNotFoundException {
        lyricsLibraryService.updateLibrary(libraryMapper.mapToLibrary(libraryDto));
        return libraryDto;
    }

    public void deleteLyricsFromLibrary(LibraryDto libraryDto) throws LibraryNotFoundException {
        lyricsLibraryService.deleteLyricsFromLibrary(libraryMapper.mapToLibrary(libraryDto));
    }

    public void deleteLibrary(long libraryId) {
        lyricsLibraryService.deleteLibrary(libraryId);
    }
}
