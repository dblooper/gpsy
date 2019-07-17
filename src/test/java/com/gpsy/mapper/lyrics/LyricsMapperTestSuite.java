package com.gpsy.mapper.lyrics;

import com.gpsy.domain.lyrics.DbLyrics;
import com.gpsy.domain.lyrics.dto.LyricsDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LyricsMapperTestSuite {

    @Autowired
    private LyricsMapper lyricsMapper;

    @Test
    public void mapToDbLyricsTest() {
        //Given
        LyricsDto lyricsDto = new LyricsDto("Test_title", "Test_artist", "Test_lyrics");

        //When
        DbLyrics dbLyrics = lyricsMapper.mapToDbLyrics(lyricsDto);

        //Then
        assertEquals("Test_lyrics", dbLyrics.getLyrics());
        assertEquals("Test_title", dbLyrics.getTitle());
    }

    @Test
    public void mapToLyricsDtoTest() {
        //Given
        DbLyrics dbLyrics = new DbLyrics("Test_title", "Test_artist", "Test_lyrics");

        //When
        LyricsDto lyricsDto = lyricsMapper.mapToLyricsDto(dbLyrics);

        //Then
        assertEquals("Test_artist", lyricsDto.getArtists());
        assertEquals("Test_title", lyricsDto.getTitle());
    }

}