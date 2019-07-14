package com.gpsy.service.dbApiServices.lyrics;

import com.gpsy.domain.lyrics.Library;
import com.gpsy.domain.lyrics.LyricsInLibrary;
import com.gpsy.exceptions.LibraryNotFoundException;
import com.gpsy.repository.audd.LibraryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class LyricsLibraryServiceTestSuite {

    @InjectMocks
    private LyricsLibraryService lyricsLibraryService;

    @Mock
    private LibraryRepository libraryRepository;

    @Test
    public void shouldSaveLyrics() throws LibraryNotFoundException {
        //Given
        List<LyricsInLibrary> lyrics = new ArrayList<>();
        lyrics.add(new LyricsInLibrary("test_title", "test_artist", "test_lyrics"));
        Library library = new Library(1L, "test_library", lyrics);
        when(libraryRepository.save(library)).thenReturn(library);
        when(libraryRepository.findById(1L)).thenReturn(Optional.of(library));

        //When
        LyricsInLibrary lyricsInLibrarySaved = lyricsLibraryService.saveLyricsInLibrary(library);

        //Then
        assertNotNull(lyricsInLibrarySaved);
        assertEquals("test_title", lyricsInLibrarySaved.getTitle());
    }

    @Test
    public void shouldUpdateLibrary()throws  LibraryNotFoundException {
        //Given
        List<LyricsInLibrary> lyrics = new ArrayList<>();
        lyrics.add(new LyricsInLibrary("test_title", "test_artist", "test_lyrics"));
        Library libraryToUpdate = new Library(1L, "test_library", lyrics);
        Library libraryWithNewName = new Library(1L,"test_updated_name", new ArrayList<>());
        when(libraryRepository.save(libraryToUpdate)).thenReturn(libraryToUpdate);
        when(libraryRepository.findById(1L)).thenReturn(Optional.of(libraryToUpdate));

        //When
        Library newUpdatedLibrary = lyricsLibraryService.updateLibrary(libraryWithNewName);

        //Then
        assertEquals("test_updated_name", newUpdatedLibrary.getLibraryName());
        assertEquals("test_artist", newUpdatedLibrary.getLyrics().get(0).getArtists());
        assertEquals(1L, newUpdatedLibrary.getLibraryId());
    }

    @Test
    public void shouldDeleteLibraryLyrics() throws LibraryNotFoundException {
        //Given
        List<LyricsInLibrary> lyrics = new ArrayList<>();
        List<LyricsInLibrary> lyricsToDelete = new ArrayList<>();
        lyricsToDelete.add(new LyricsInLibrary("test_title1", "test_artist1", "test_lyrics1"));
        lyrics.add(new LyricsInLibrary("test_title1", "test_artist1", "test_lyrics1"));
        lyrics.add(new LyricsInLibrary("test_title2", "test_artist2", "test_lyrics2"));
        Library libraryToDeleteLyrics = new Library(1L, "test_library", lyrics);
        Library libraryWithLyricsToDelete = new Library(1L,"test_library", lyricsToDelete);
        when(libraryRepository.save(libraryToDeleteLyrics)).thenReturn(libraryToDeleteLyrics);
        when(libraryRepository.findById(1L)).thenReturn(Optional.of(libraryToDeleteLyrics));

        //When
        lyricsLibraryService.deleteLyricsFromLibrary(libraryWithLyricsToDelete);
        //Then
        verify(libraryRepository, times(1)).save(libraryToDeleteLyrics);

    }
}