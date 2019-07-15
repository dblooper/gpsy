package com.gpsy.service.dbApiServices.spotify;

import com.gpsy.config.InitialLimitValues;
import com.gpsy.domain.spotify.DbMostFrequentTrackCalc;
import com.gpsy.domain.spotify.MostFrequentTrack;
import com.gpsy.domain.spotify.RecommendedPlaylist;
import com.gpsy.externalApis.spotify.client.SpotifyClient;
import com.gpsy.mapper.spotify.TrackMapper;
import com.gpsy.mapper.spotify.database.TrackDbMapper;
import com.gpsy.repository.spotify.DbMostFrequentTracksRepository;
import com.gpsy.repository.spotify.RecommendedPlaylistRepository;
import com.gpsy.repository.spotify.SpotifyPopularTrackRepository;
import com.gpsy.repository.spotify.SpotifyRecentPlayedTrackRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FetchDataFromDbServiceTestSuite {

    @Mock
    private SpotifyRecentPlayedTrackRepository spotifyRecentPlayedTrackRepository;
    @Mock
    private DbMostFrequentTracksRepository dbMostFrequentTracksRepository;
    @Mock
    private TrackDbMapper trackDbMapper;
    @Mock
    private TrackMapper trackMapper;
    @Mock
    private SpotifyClient spotifyClient;
    @Mock
    private RecommendedPlaylistRepository recommendedPlaylistRepository;

    @InjectMocks
    FetchDataFromDbService fetchDataFromDbService;

    @Test
    public void fetchPopularTracks() {
    }

    @Test
    public void fetchRecentPlayedTracks() {
    }

    @Test
    public void fetchUserPlaylists() {
    }

    @Test
    public void saveSpotifyByDbDataMostFrequentTrack() {
        //Given
        List<DbMostFrequentTrackCalc> dbMostFrequentTrackCalcList = new ArrayList<>();
        List<MostFrequentTrack> mostFrequentTracks = new ArrayList<>();
        DbMostFrequentTrackCalc dbMostFrequentTrackCalc1 = new DbMostFrequentTrackCalc("123",
                "Test_title",
                "Artist",
                12);
        DbMostFrequentTrackCalc dbMostFrequentTrackCalc2 = new DbMostFrequentTrackCalc("1234",
                "Test_title2",
                "Artist2",
                12);
        MostFrequentTrack mostFrequentTrack = new MostFrequentTrack.MostFrequentTrackBuilder()
                .stringId("123")
                .title("Test_title")
                .artists("Artist")
                .popularity(10).build();
        MostFrequentTrack mostFrequentTrack2 = new MostFrequentTrack.MostFrequentTrackBuilder()
                .stringId("1234")
                .title("Test_title2")
                .artists("Artist2")
                .popularity(12).build();
        dbMostFrequentTrackCalcList.add(dbMostFrequentTrackCalc1);
        dbMostFrequentTrackCalcList.add(dbMostFrequentTrackCalc2);
        mostFrequentTracks.add(mostFrequentTrack);

        when(spotifyRecentPlayedTrackRepository.retrieveWeekMostPopularTrack()).thenReturn(dbMostFrequentTrackCalcList);
        when(dbMostFrequentTracksRepository.findAll()).thenReturn(mostFrequentTracks);
        when(trackDbMapper.mapToMostFrequentTrack(dbMostFrequentTrackCalc1)).thenReturn(mostFrequentTrack);
        when(trackDbMapper.mapToMostFrequentTrack(dbMostFrequentTrackCalc2)).thenReturn(mostFrequentTrack2);

        //When
        fetchDataFromDbService.saveSpotifyByDbDataMostFrequentTracks();

        //Then
        verify(dbMostFrequentTracksRepository, times(1)).save(mostFrequentTrack);
    }

    @Test
    public void fetchMostFrequentTracks() {
    }

    @Test
    public void updateFetchRecommendedPlaylistFromDb() {
        //Given
        RecommendedPlaylist newRecommendedPlaylist = new RecommendedPlaylist.RecommendedPlaylistBuilder()
                .actual(true)
                .stringId(InitialLimitValues.RECOMMENDED_PLAYLIST_ID)
                .playlistTracks(new ArrayList<>())
                .name(InitialLimitValues.RECOMMENDED_PLAYLIST_NAME).build();
         when(spotifyClient.getRecommendedTracks()).thenReturn(new ArrayList<>());
         when(recommendedPlaylistRepository.findAll()).thenReturn(new ArrayList<>());
         when(trackMapper.mapToRecommendedPlaylistTracks(new ArrayList<>(), 5)).thenReturn(new ArrayList<>());
         when(recommendedPlaylistRepository.save(newRecommendedPlaylist)).thenReturn(newRecommendedPlaylist);

        //When
        RecommendedPlaylist fetchedRecommendedPlaylist = fetchDataFromDbService.updateFetchRecommendedPlaylistFromDb(5);

        //Then
        assertEquals("Tygodniowka", fetchedRecommendedPlaylist.getName());
        assertEquals(0, fetchedRecommendedPlaylist.getNumberOfTracks());
        assertTrue(fetchedRecommendedPlaylist.isActual());
    }

    @Test
    public void changeNumberOfTracks() {
    }

    @Test
    public void fetchRecommendedPlaylist() {
    }
}