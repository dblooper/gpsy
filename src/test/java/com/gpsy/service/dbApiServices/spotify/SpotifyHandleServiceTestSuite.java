package com.gpsy.service.dbApiServices.spotify;

import com.gpsy.domain.spotify.RecommendedTrack;
import com.gpsy.domain.spotify.UserPlaylist;
import com.gpsy.externalApis.spotify.client.SpotifyClient;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpotifyHandleServiceTestSuite {

    @Autowired
    private SpotifyHandleService spotifyHandleService;

    @MockBean
    private SpotifyClient spotifyClient;

    @Test
    public void shouldReturnRecommendedTracks() {
        //Given
        List<TrackSimplified> trackSimplifiedList = new ArrayList<>();
        for(int i = 1; i <=10; i++) {
            trackSimplifiedList.add(new TrackSimplified.Builder()
                    .setArtists(new ArtistSimplified.Builder().setName("test_artist" + i).build())
                    .setName("test_name"+i)
                    .setId("_id" + i)
                    .setPreviewUrl("http://prev" + i + ".test").build());
        }

        when(spotifyClient.getRecommendedTracks(10)).thenReturn(trackSimplifiedList);

        //When
        List<RecommendedTrack> recommendedTracksFetched = spotifyHandleService.returnRecommendedTracks(10);

        //Then
        assertEquals("test_artist3", recommendedTracksFetched.get(2).getAuthors());
        assertEquals("test_name8", recommendedTracksFetched.get(7).getTitle());

    }

    @Test
    public void shouldNotUpdatePlaylistName() {
        //Given
        UserPlaylist userPlaylist = new UserPlaylist.Builder()
                .name("test_name")
                .stringId("2ptqwasYqv1677gL4OEkIL")
                .tracks(new ArrayList<>()).build();

        when(spotifyClient.updatePlaylistDetails(userPlaylist)).thenReturn(userPlaylist);

        //When
        UserPlaylist userPlaylistRetrieved = spotifyHandleService.updatePlaylistName(userPlaylist);

        //Then
        assertEquals("Not changed, not allowed id",userPlaylistRetrieved.getName());

    }

}
