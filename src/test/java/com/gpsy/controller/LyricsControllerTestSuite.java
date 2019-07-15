package com.gpsy.controller;

import com.google.gson.Gson;
import com.gpsy.domain.lyrics.Library;
import com.gpsy.domain.lyrics.LyricsInLibrary;
import com.gpsy.domain.lyrics.dto.LibraryDto;
import com.gpsy.domain.lyrics.dto.LyricsDto;
import com.gpsy.domain.lyrics.dto.LyricsInLibraryDto;
import com.gpsy.exceptions.LibraryNotFoundException;
import com.gpsy.facade.LyricsFacade;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LyricsController.class)
public class LyricsControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LyricsFacade lyricsFacade;

    @Test
    public void shouldFetchLyrics() throws Exception{
        //Given
        String title = "Test_title";
        String author = "Test_author";
        LyricsDto lyricsDto = new LyricsDto(title, author, "Test_lyrics");
        when(lyricsFacade.fetchLyricsFromApi(title, author)).thenReturn(lyricsDto);

        //When/Then
        mockMvc.perform(get("/v1/gpsy/musixmatch/lyrics?title=Test_title&author=Test_author").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test_title")))
                .andExpect(jsonPath("$.lyrics", is("Test_lyrics")));
    }

    @Test
    public void shouldFetchLyricsLibraries() throws Exception {
        //Given
        List<LibraryDto> libraries = new ArrayList<>();
        List<LyricsInLibraryDto> lyrics = new ArrayList<>();
        LyricsInLibraryDto lyricsInLibrary = new LyricsInLibraryDto("Test_title", "Test_author", "Test_lyrics");
        lyrics.add(lyricsInLibrary);
        LibraryDto library = new LibraryDto(1L,"Test_library", new ArrayList<>());
        LibraryDto libraryWithLyrics = new LibraryDto(2L, "Test_library2",lyrics);
        libraries.add(library);
        libraries.add(libraryWithLyrics);

        when(lyricsFacade.fetchLibraries()).thenReturn(libraries);

        //When/Then
        mockMvc.perform(get("/v1/gpsy/libraries/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].libraryName", is("Test_library")))
                .andExpect(jsonPath("$.[0].lyrics", is(emptyCollectionOf(LyricsInLibraryDto.class))))
                .andExpect(jsonPath("$.[1].libraryName", is("Test_library2")))
                .andExpect(jsonPath("$.[1].lyrics[0].lyrics", is("Test_lyrics")));
    }

    @Test
    public void shouldCreateNewLibrary() throws Exception {
        //Given
        LibraryDto libraryDto = new LibraryDto(122L, "Test_library", new ArrayList<>());
        Gson gson = new Gson();
        String jsonLibraryDto = gson.toJson(libraryDto);

        //When/Then
        mockMvc.perform(post("/v1/gpsy/libraries/new")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonLibraryDto))
                .andExpect(status().isOk());

        verify(lyricsFacade, times(1)).addLibrary(libraryDto);
    }

    @Test
    public void shouldAddLyricsToLibrary() throws Exception {
        //Given
        List<LyricsInLibraryDto> lyrics = new ArrayList<>();
        LyricsInLibraryDto lyricsInLibraryDto = new LyricsInLibraryDto("Title1", "Artist1", "Lyrics1");
        lyrics.add(lyricsInLibraryDto);
        LibraryDto libraryDto = new LibraryDto(122L, "Test_library", lyrics);
        Gson gson = new Gson();
        String lyricsToAdd = gson.toJson(libraryDto);

        //When/Then
        mockMvc.perform(post("/v1/gpsy/libraries/lyrics/add")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(lyricsToAdd))
                .andExpect(status().isOk());

        verify(lyricsFacade, times(1)).addLyricsToLibrary(libraryDto);

    }

    @Test
    public void shouldNotAddLyricsToLibrary() throws Exception {
        //Given
        List<LyricsInLibraryDto> lyrics = new ArrayList<>();
        LyricsInLibraryDto lyricsInLibraryDto = new LyricsInLibraryDto("Title1", "Artist1", "Lyrics1");
        lyrics.add(lyricsInLibraryDto);
        LibraryDto libraryDto = null;
        Gson gson = new Gson();
        String lyricsToAdd = gson.toJson(libraryDto);

        //When/Then
        mockMvc.perform(post("/v1/gpsy/libraries/lyrics/add")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(lyricsToAdd))
                .andExpect(status().is(400));

        verify(lyricsFacade, times(0)).addLyricsToLibrary(libraryDto);
    }

    @Test
    public void shouldUpdateLibraryName() throws Exception {
        //Given
        LibraryDto libraryDto = new LibraryDto(122L, "Test_library", new ArrayList<>());
        Gson gson = new Gson();
        String lyricsToAdd = gson.toJson(libraryDto);
        when(lyricsFacade.updateLibraryName(libraryDto)).thenReturn(libraryDto);

        //When/Then
        mockMvc.perform(put("/v1/gpsy/libraries/update")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(lyricsToAdd))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libraryName", is("Test_library")))
                .andExpect(jsonPath("$.id", is(122)));

    }

    @Test
    public void shouldDeleteLyricsFromLibrary() throws Exception{
        //Given
        List<LyricsInLibraryDto> lyrics = new ArrayList<>();
        LyricsInLibraryDto lyricsInLibraryDto = new LyricsInLibraryDto("Title1", "Artist1", "Lyrics1");
        lyrics.add(lyricsInLibraryDto);
        LibraryDto libraryDto = new LibraryDto(122L, "Test_library", lyrics);
        Gson gson = new Gson();
        String lyricsToAdd = gson.toJson(libraryDto);

        //When/Then
        mockMvc.perform(delete("/v1/gpsy/libraries/lyrics/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(lyricsToAdd))
                .andExpect(status().isOk());

        verify(lyricsFacade, times(1)).deleteLyricsFromLibrary(libraryDto);
    }

    @Test
    public void shouldDeleteLibrary() throws Exception {
        //Given

        //When/Then
        mockMvc.perform(delete("/v1/gpsy/libraries/delete?libraryId=2"))
                .andExpect(status().isOk());

        verify(lyricsFacade, times(1)).deleteLibrary(2);
    }

    @Test
    public void shouldNotDeleteLibrary() throws Exception {
        //Given

        //When/Then
        mockMvc.perform(delete("/v1/gpsy/libraries/delete"))
                .andExpect(status().is(400));
    }

}