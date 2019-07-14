package com.gpsy.service.dbApiServices.musiXmatch;

import com.gpsy.domain.lyrics.DbLyrics;
import com.gpsy.domain.lyrics.dto.LyricsBaseDto;
import com.gpsy.domain.lyrics.dto.LyricsDto;
import com.gpsy.domain.lyrics.dto.TrackInfoForLyricsDto;
import com.gpsy.exceptions.MusiXmatchServerResponseException;
import com.gpsy.externalApis.musiXmatchApi.client.MusiXmatchClient;
import com.gpsy.mapper.lyrics.LyricsMapper;
import com.gpsy.mapper.spotify.TrackMapper;
import com.gpsy.repository.audd.LyricsDbRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MusiXmatchServiceTestSuite {

    @InjectMocks
    private MusiXmatchService musiXmatchService;

    @Mock
    private MusiXmatchClient musiXmatchClient;

    @Mock
    private LyricsMapper lyricsMapper;

    @Mock
    private LyricsDbRepository lyricsDbRepository;

    @Test
    public void shouldRetutnLyricsFromDb() {
        //Given
        TrackInfoForLyricsDto trackInfoForLyricsDto = new TrackInfoForLyricsDto("Test_title", "Test_artist");
        DbLyrics dbLyrics = new DbLyrics("Test_title", "Test_artist", "test_lyrics");
        LyricsDto lyricsDto = new LyricsDto("Test_title", "Test_artist", "test_lyrics");
        LyricsBaseDto lyricsBaseDto = new LyricsBaseDto(200, lyricsDto);

        when(lyricsDbRepository.findByTitleAndArtist("Test_title", "Test_artist")).thenReturn(Optional.of(dbLyrics));

        //When
        musiXmatchService.fetchLirycs("Test_title", "Test_artist");

        //Then
        verify(lyricsDbRepository, times(0)).save(dbLyrics);
    }

    @Test
    public void shouldSearchForLyricsInApiAndSaveToDb() {
        //Given
        TrackInfoForLyricsDto trackInfoForLyricsDto = new TrackInfoForLyricsDto("Test_title2", "Test_artist2");
        DbLyrics dbLyrics = new DbLyrics("Test_title2", "Test_artist2", "test_lyrics2");
        LyricsDto lyricsDto = new LyricsDto("Test_title2", "Test_artist2", "test_lyrics2");
        LyricsBaseDto lyricsBaseDto = new LyricsBaseDto(200, lyricsDto);

        when(musiXmatchClient.fetchLyrics(trackInfoForLyricsDto)).thenReturn(lyricsBaseDto);
        when(lyricsMapper.mapToDbLyrics(lyricsDto)).thenReturn(dbLyrics);
        when(lyricsDbRepository.save(dbLyrics)).thenReturn(dbLyrics);

        //When
        DbLyrics dbLyricsFetched = musiXmatchService.fetchLirycs("Test_title2", "Test_artist2");

        //Then
        verify(lyricsDbRepository, times(1)).save(dbLyrics);
        assertEquals("Test_title2", dbLyricsFetched.getTitle());
    }

}