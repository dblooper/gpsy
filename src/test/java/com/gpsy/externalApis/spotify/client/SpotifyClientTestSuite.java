package com.gpsy.externalApis.spotify.client;

import com.gpsy.config.InitialLimitValues;
import com.gpsy.domain.spotify.MostFrequentTrack;
import com.gpsy.domain.spotify.PlaylistTrack;
import com.gpsy.domain.spotify.UserPlaylist;
import com.gpsy.service.dbApiServices.spotify.FetchDataFromDbService;
import com.wrapper.spotify.enums.ModelObjectType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpotifyClientTestSuite {

    @Mock
    private SpotifyAuthorizator spotifyAuthorizator;

    @Mock
    private FetchDataFromDbService fetchDataFromDbService;

    @InjectMocks
    private SpotifyClient spotifyClient;

    @Test
//    public void shouldReturnTrack() {
//        //Given
//        String searchedItem = "Test";
//        when(spotifyAuthorizator.getSpotifyApi().searchItem(searchedItem, ModelObjectType.TRACK.getType())
//                .limit(InitialLimitValues.LIMIT_FREE_SEARCH_TRACKS)
//                .build()).thenReturn()
//        //When
//        //Then
//
//    }

    public void shouldReturnTrackUrisForSpotifyRequest() {
        //Given
        List<PlaylistTrack> playlistTrackList = new ArrayList<>();
        for(int i = 1; i < 4; i++ ) {
            playlistTrackList.add(new PlaylistTrack.PlaylistTrackBuilder()
                                                    .title("title" + i)
                                                    .artists("artist" + i)
                                                    .stringId("string" + i).build());
        }

        UserPlaylist userPlaylist = new UserPlaylist.UserPlaylistBuilder()
                                                    .name("testName")
                                                    .stringId("test123")
                                                    .tracks(playlistTrackList).build();
        //When
        String[] tracksUriArray = spotifyClient.getTrackUrisForSpotifyRequest(userPlaylist);

        //Then
        assertEquals("spotify:track:string1",tracksUriArray[0]);
        assertEquals("spotify:track:string2",tracksUriArray[1]);
    }

    @Test
    public void shouldReturnJsonArrayTrackToDeleteMaker() {
        //Given
        List<PlaylistTrack> playlistTrackList = new ArrayList<>();
        for(int i = 1; i < 3; i++ ) {
            playlistTrackList.add(new PlaylistTrack.PlaylistTrackBuilder()
                                                    .title("title" + i)
                                                    .artists("artist" + i)
                                                    .stringId("string" + i).build());
        }

        UserPlaylist userPlaylist = new UserPlaylist.UserPlaylistBuilder()
                                                    .name("testName")
                                                    .stringId("test123")
                                                    .tracks(playlistTrackList).build();

        String expectedArray = "[{\"uri\":\"spotify:track:string1\"},{\"uri\":\"spotify:track:string2\"}]";

        List<PlaylistTrack> playlistTrackList2 = new ArrayList<>();
            playlistTrackList2.add(new PlaylistTrack.PlaylistTrackBuilder()
                                                     .title("title1")
                                                     .artists("artist1")
                                                     .stringId("string1").build());
        UserPlaylist userPlaylist2 = new UserPlaylist.UserPlaylistBuilder()
                                                        .name("testName")
                                                        .stringId("test123")
                                                        .tracks(playlistTrackList2).build();

        String expectedArray2 = "[{\"uri\":\"spotify:track:string1\"}]";
        //When
        String retrievedJsonArray = spotifyClient.jsonArrayTrackToDeleteMaker(userPlaylist).toString();
        String retrievedJsonArray2 = spotifyClient.jsonArrayTrackToDeleteMaker(userPlaylist2).toString();
        //Then
        assertEquals(expectedArray, retrievedJsonArray);
        assertEquals(expectedArray2, retrievedJsonArray2);
    }

    @Test
    public void shouldReturnMergedTrackString() {

        //Given
        List<MostFrequentTrack> mostFrequentTracks = new ArrayList<>();
        for(int i = 1; i < 4; i++ ) {
            mostFrequentTracks.add(new MostFrequentTrack.MostFrequentTrackBuilder()
                                                        .title("title" + i)
                                                        .artists("artist" + i)
                                                        .stringId("string" + i)
                                                        .popularity(i)
                                                        .build());
        }
        when(fetchDataFromDbService.fetchMostFrequentTracks()).thenReturn(mostFrequentTracks);

        String expectedResponse = "";
        String expectedResponse1 = "string1";
        String expectedResponse2 = "string1,string2";

        //When
        String tracksToSend = spotifyClient.popularTracksMerge(0);
        String tracksToSend1 = spotifyClient.popularTracksMerge(1);
        String tracksToSend2 = spotifyClient.popularTracksMerge(2);

        //Then
        assertEquals(expectedResponse, tracksToSend);
        assertEquals(expectedResponse1, tracksToSend1);
        assertEquals(expectedResponse2, tracksToSend2);
    }
}