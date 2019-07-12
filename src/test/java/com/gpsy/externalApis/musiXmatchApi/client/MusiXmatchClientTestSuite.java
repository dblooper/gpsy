package com.gpsy.externalApis.musiXmatchApi.client;

import com.gpsy.domain.lyrics.dto.LyricsBaseDto;
import com.gpsy.domain.lyrics.dto.TrackInfoForLyricsDto;
import com.gpsy.exceptions.MusiXmatchServerResponseException;
import com.gpsy.externalApis.musiXmatchApi.config.MusiXmatchConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MusiXmatchClientTestSuite {

    @InjectMocks
    private MusiXmatchClient musiXmatchClient;

    @Mock
    private MusiXmatchConfig musiXmatchConfig;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void init() {
        when(musiXmatchConfig.getApiEndpointRoot()).thenReturn("https://test.com/");
        when(musiXmatchConfig.getApiKey()).thenReturn("testKey");
    }

    @Test
    public void shouldFetchLyrics() throws URISyntaxException, MusiXmatchServerResponseException {
        //Given
        TrackInfoForLyricsDto trackInfoForLyricsDto = new TrackInfoForLyricsDto("test", "test");
        String testLyrics = "{\"message\":{\"header\":{\"status_code\":200,\"execute_time\":0.015320062637329},\"body\":{\"lyrics\":{\"lyrics_id\":18040511,\"explicit\":0,\"lyrics_body\":\"Test lyrics\"}}}}}";
        URI url = new URI("https://test.com/matcher.lyrics.get?format=json&q_track=test&q_artist=test&apikey=testKey");
        when(restTemplate.getForObject(url, String.class)).thenReturn(testLyrics);

        //When
        LyricsBaseDto lyricsBaseDto = musiXmatchClient.fetchLyrics(trackInfoForLyricsDto);

        //Then
        assertEquals(200, lyricsBaseDto.getStatusCode());
        assertEquals("Test lyrics", lyricsBaseDto.getBody().getLyrics());
    }

    @Test
    public void shouldReturnLyricsNotFoundWithErrorStatusCode() throws URISyntaxException, MusiXmatchServerResponseException {
        //Given
        TrackInfoForLyricsDto trackInfoForLyricsDto = new TrackInfoForLyricsDto("test", "test");
        String testLyrics = "{\"message\":{\"header\":{\"status_code\":401,\"execute_time\":0.015320062637329},\"body\":{\"lyrics\":{\"lyrics_id\":18040511,\"explicit\":0,\"lyrics_body\":\"Test lyrics\"}}}}}";
        URI url = new URI("https://test.com/matcher.lyrics.get?format=json&q_track=test&q_artist=test&apikey=testKey");
        when(restTemplate.getForObject(url, String.class)).thenReturn(testLyrics);

        //When
        LyricsBaseDto lyricsBaseDto = musiXmatchClient.fetchLyrics(trackInfoForLyricsDto);

        //Then
        assertEquals(401, lyricsBaseDto.getStatusCode());
        assertEquals("Lyrics not found, sorry :(", lyricsBaseDto.getBody().getLyrics());
    }

    @Test
    public void shouldReturnLyricsNotFoundWithNoLyricsInjected() throws URISyntaxException, MusiXmatchServerResponseException {
        //Given
        TrackInfoForLyricsDto trackInfoForLyricsDto = new TrackInfoForLyricsDto("test", "test");
        String testLyrics = "{\"message\":{\"header\":{\"status_code\":401,\"execute_time\":0.015320062637329},\"body\":{\"lyrics\":{\"lyrics_id\":18040511,\"explicit\":0,\"lyrics_body\":null}}}}}";
        URI url = new URI("https://test.com/matcher.lyrics.get?format=json&q_track=test&q_artist=test&apikey=testKey");
        when(restTemplate.getForObject(url, String.class)).thenReturn(testLyrics);

        //When
        LyricsBaseDto lyricsBaseDto = musiXmatchClient.fetchLyrics(trackInfoForLyricsDto);

        //Then
        assertEquals(401, lyricsBaseDto.getStatusCode());
        assertEquals("Lyrics not found, sorry :(", lyricsBaseDto.getBody().getLyrics());
    }
}