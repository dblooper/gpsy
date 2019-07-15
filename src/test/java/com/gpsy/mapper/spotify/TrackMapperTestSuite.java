package com.gpsy.mapper.spotify;

import com.gpsy.domain.spotify.*;
import com.gpsy.domain.spotify.dto.PlaylistTrackDto;
import com.gpsy.domain.spotify.dto.PopularTrackDto;
import com.gpsy.domain.spotify.dto.RecentPlayedTrackDto;
import com.gpsy.domain.spotify.dto.RecommendedTrackDto;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrackMapperTestSuite {

    @Autowired
    private TrackMapper trackMapper;

    @Test
    public void mapSpotifyTrackToDbPopoularTrackTest() {
        //Given
        Track spotifyTrack = new Track.Builder()
                .setArtists(new ArtistSimplified.Builder().setName("Test Bob").build())
                .setId("test_track_id")
                .setName("test_name")
                .setPopularity(25).build();

        //When
        PopularTrack popularTrack = trackMapper.mapSpotifyTrackToDbPopularTrack(spotifyTrack);

        //Then
        assertEquals("Test Bob", popularTrack.getArtists());
        assertEquals("test_track_id", popularTrack.getTrackStringId());
        assertEquals("test_name", popularTrack.getTitle());
        assertEquals(25, popularTrack.getPopularity().intValue());

    }

    @Test
    public void mapToRecentPlayedTrackDtoListTest() {
        //Given
        List<RecentPlayedTrack> recentPlayedTrackList = new ArrayList<>();
        RecentPlayedTrack recentPlayedTrack1 = new RecentPlayedTrack.RecentPlayedTrackBuilder()
                .stringId("1234")
                .title("Test_Title")
                .artists("John")
                .playDate(new Date(2019))
                .build();
        RecentPlayedTrack recentPlayedTrack2 = new RecentPlayedTrack.RecentPlayedTrackBuilder()
                .stringId("12343")
                .title("Test_Title2")
                .artists("John2")
                .playDate(new Date(2011))
                .build();
        recentPlayedTrackList.add(recentPlayedTrack1);
        recentPlayedTrackList.add(recentPlayedTrack2);

        //When
        List<RecentPlayedTrackDto> mappedRecentPlayedTrackDto = trackMapper.mapToRecentPlayedTrackDtoList(recentPlayedTrackList);

        //Then
        assertEquals("1234", mappedRecentPlayedTrackDto.get(0).getTrackStringId());
        assertEquals("12343", mappedRecentPlayedTrackDto.get(1).getTrackStringId());
        assertEquals("Test_Title", mappedRecentPlayedTrackDto.get(0).getTitle());
        assertEquals("Test_Title2", mappedRecentPlayedTrackDto.get(1).getTitle());
    }

    @Test
    public void mapToPopularTrackDtoList() {
        //Given
        List<PopularTrack> popularTracks = new ArrayList<>();
        PopularTrack popularTrack1 = new PopularTrack.PopularTrackBuiilder()
                .stringId("123")
                .title("Title1")
                .artists("Artist1")
                .popularity(124)
                .build();
        PopularTrack popularTrack2 = new PopularTrack.PopularTrackBuiilder()
                .stringId("122")
                .title("Title2")
                .artists("Artist2")
                .popularity(122)
                .build();
        popularTracks.add(popularTrack1);
        popularTracks.add(popularTrack2);

        //When
        List<PopularTrackDto> popularTrackDtoList = trackMapper.mapPopularTrackToPopularTrackDtoList(popularTracks);

        //Then
        assertEquals("123", popularTrackDtoList.get(0).getTrackStringId());
        assertEquals(124, popularTrackDtoList.get(0).getPopularity());
        assertEquals("Artist1", popularTrackDtoList.get(0).getArtists());
    }

    @Test
    public void mapToRecommendedTrackDtoList() {
        //Given
        List<RecommendedTrack> recommendedTracks = new ArrayList<>();
        RecommendedTrack recommendedTrack1 = new RecommendedTrack.RecommendedTrackBuilder()
                .stringId("123")
                .title("TitleT1")
                .artists("ArtistT1")
                .sample("https://test.com")
                .build();
        RecommendedTrack recommendedTrack2 = new RecommendedTrack.RecommendedTrackBuilder()
                .stringId("124")
                .title("TitleT2")
                .artists("ArtistT2")
                .sample("https://test2.com")
                .build();
        recommendedTracks.add(recommendedTrack1);
        recommendedTracks.add(recommendedTrack2);

        //When
        List<RecommendedTrackDto> recommendedTrackDtoList = trackMapper.mapToRecommendedTrackDtoList(recommendedTracks);

        //Then
        assertEquals("TitleT2", recommendedTrackDtoList.get(1).getTitle());
        assertEquals("ArtistT2", recommendedTrackDtoList.get(1).getArtists());
        assertEquals("124", recommendedTrackDtoList.get(1).getTrackStringId());
        assertEquals("https://test2.com", recommendedTrackDtoList.get(1).getSample());
    }

    @Test
    public void mapToPlaylistTrack() {

        //Given
        List<PlaylistTrackDto> playlistTracksDtoList = new ArrayList<>();
        PlaylistTrackDto testPlaylistTrackDto1 = new PlaylistTrackDto.PlaylistTrackDtoBuilder().title("Test_title1")
                                                                    .artists("Test_artist1")
                                                                    .stringId("12345@").build();
        PlaylistTrackDto testPlaylistTrackDto2 = new PlaylistTrackDto.PlaylistTrackDtoBuilder().title("Test_title2")
                .artists("Test_artist2")
                .stringId("6789@").build();

        playlistTracksDtoList.add(testPlaylistTrackDto1);
        playlistTracksDtoList.add(testPlaylistTrackDto2);

        //When
        List<PlaylistTrack> playlistTracks = trackMapper.mapToPlaylistTrack(playlistTracksDtoList);

        //Then
        assertEquals("Test_title1", playlistTracks.get(0).getTitle());
        assertEquals("6789@", playlistTracks.get(1).getTrackStringId());
        assertEquals("Test_artist2", playlistTracks.get(1).getArtists());
    }

    @Test
    public void mapToRecommendedTrackForPlaylistDto() {
    }

    @Test
    public void mapPopularTrackToPopularTrackDtoList() {
    }

    @Test
    public void mapToSearchTrackDto() {
    }

    @Test
    public void mapToRecommendedPlaylistTracks() {
    }

    @Test
    public void mapRecommendedPlaylistTracksToPlaylistTracks() {
    }

    @Test
    public void mapToRecommendedTracks() {
    }

    @Test
    public void mapRecommendedPlaylistToUserPlaylist() {


    }
}