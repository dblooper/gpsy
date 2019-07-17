package com.gpsy.service.dbApiServices.spotify;

import com.gpsy.config.InitialLimitValues;
import com.gpsy.domain.spotify.*;
import com.gpsy.externalApis.spotify.client.SpotifyClient;
import com.gpsy.mapper.spotify.TrackMapper;
import com.gpsy.mapper.spotify.database.TrackDbMapper;
import com.gpsy.repository.spotify.DbMostFrequentTracksRepository;
import com.gpsy.repository.spotify.RecommendedPlaylistRepository;
import com.gpsy.repository.spotify.SpotifyRecentPlayedTrackRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Date;
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
    private SaveSpotifyDataToDbService saveSpotifyDataToDbService;
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
    public void shouldFetchRecentPlayedTracks() {
        //Given
        List<RecentPlayedTrack> recentPlayedTrackList = new ArrayList<>();
        for(int i = 1; i<=15; i++) {
            recentPlayedTrackList.add(new RecentPlayedTrack.Builder()
                    .stringId("Test_id" + i)
                    .title("Test_title" + i)
                    .artists("Test_artist" + i)
                    .playDate(new Date(123+i))
                    .build()
            );
        }

        when(spotifyRecentPlayedTrackRepository.findAll()).thenReturn(recentPlayedTrackList);

        //When
        List<RecentPlayedTrack> recentPlayedTracksRetrieved = fetchDataFromDbService.fetchRecentPlayedTracks(6);

        //Then
        assertEquals(6, recentPlayedTracksRetrieved.size());
        assertEquals("Test_title15", recentPlayedTracksRetrieved.get(0).getTitle());
    }

    @Test
    public void shouldFetchUserPlaylists() {
    }

    @Test
    public void shouldSaveSpotifyByDbDataMostFrequentTrack() {
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
        MostFrequentTrack mostFrequentTrack = new MostFrequentTrack.Builder()
                .stringId("123")
                .title("Test_title")
                .artists("Artist")
                .popularity(10).build();
        MostFrequentTrack mostFrequentTrack2 = new MostFrequentTrack.Builder()
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
    public void shouldFetchMostFrequentTracks() {
        //Given
        List<MostFrequentTrack> mostFrequentTracks = new ArrayList<>();
        for(int i = 1; i<=8; i++) {
            mostFrequentTracks.add(new MostFrequentTrack.Builder()
                    .stringId("Test_id" + i)
                    .title("Test_title" + i)
                    .artists("Test_artist" + i)
                    .popularity(i)
                    .build()
            );
        }
        Pageable pageable = PageRequest.of(0, 8);
        when(dbMostFrequentTracksRepository.findAllByPopularityGreaterThanOrderByPopularityDesc(0, pageable)).thenReturn(mostFrequentTracks);

        //When
        List<MostFrequentTrack> mostFrequentTracksRetrieved = fetchDataFromDbService.fetchMostFrequentTracks(8);

        //Then
        assertEquals(8, mostFrequentTracksRetrieved.size());
        assertEquals("Test_title8", mostFrequentTracksRetrieved.get(0).getTitle());
    }

    @Test
    public void shouldUpdateFetchRecommendedPlaylistFromDb() {
        //Given
        RecommendedPlaylist newRecommendedPlaylist = new RecommendedPlaylist.Builder()
                .actual(true)
                .stringId(InitialLimitValues.RECOMMENDED_PLAYLIST_ID)
                .playlistTracks(new ArrayList<>())
                .name(InitialLimitValues.RECOMMENDED_PLAYLIST_NAME).build();
         when(spotifyClient.getRecommendedTracks(5)).thenReturn(new ArrayList<>());
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
    public void shouldChangeNumberOfTracks() {
        List<RecommendedPlaylistTrack> recommendedPlaylistTracks = new ArrayList<>();
        for(int i = 1; i <= 10; i++) {
            recommendedPlaylistTracks.add(new RecommendedPlaylistTrack.Builder()
                    .title("test_title" + i)
                    .artists("artist"+i)
                    .stringId("id" + i)
                    .sample("http://test" + i + ".test")
                    .build()
            );
        }
        RecommendedPlaylist recommendedPlaylist = new RecommendedPlaylist.Builder()
                .name("Test_name")
                .playlistTracks(recommendedPlaylistTracks)
                .actual(true)
                .stringId("Test_pl_id").build();

        when(recommendedPlaylistRepository.findByActualTrue()).thenReturn(recommendedPlaylist);
        when(recommendedPlaylistRepository.save(recommendedPlaylist)).thenReturn(recommendedPlaylist);

        //When
        RecommendedPlaylist recommendedPlaylistUpdated = fetchDataFromDbService.changeNumberOfTracks(2);

        //Then
        verify(recommendedPlaylistRepository, times(1)).save(recommendedPlaylist);
        assertEquals(2,recommendedPlaylistUpdated.getNumberOfTracks());
    }
}