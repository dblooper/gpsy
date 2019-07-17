package com.gpsy.mapper.spotify;

import com.gpsy.domain.spotify.RecommendedPlaylist;
import com.gpsy.domain.spotify.RecommendedPlaylistTrack;
import com.gpsy.domain.spotify.UserPlaylist;
import com.gpsy.domain.spotify.dto.RecommendedPlaylistDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DbPlaylistMapperTestSuite {

    @Autowired
    private DbPlaylistMapper dbPlaylistMapper;

    @Test
    public void mapToRecommendedPlaylistDtoTest() {
        //Given
        List<RecommendedPlaylistTrack> recommendedPlaylistTracks = new ArrayList<>();
        RecommendedPlaylistTrack recommendedPlaylistTrack1 = new RecommendedPlaylistTrack.Builder()
                .title("TrackTested1")
                .artists("ArtistTested1")
                .stringId("0987")
                .sample("https://testTrack1.com").build();
        RecommendedPlaylistTrack recommendedPlaylistTrack2 = new RecommendedPlaylistTrack.Builder()
                .title("TrackTested2")
                .artists("ArtistTested2")
                .stringId("6543")
                .sample("https://testTrack2.com").build();
        recommendedPlaylistTracks.add(recommendedPlaylistTrack1);
        recommendedPlaylistTracks.add(recommendedPlaylistTrack2);
        RecommendedPlaylist recommendedPlaylist = new RecommendedPlaylist.Builder()
                .name("Recommended_playlist1")
                .playlistTracks(recommendedPlaylistTracks)
                .stringId("123")
                .actual(false).build();

        //When
        RecommendedPlaylistDto recommendedPlaylistDto = dbPlaylistMapper.mapToRecommendedPlaylistDto(recommendedPlaylist);

        //Then
        assertEquals("Recommended_playlist1", recommendedPlaylistDto.getName());
        assertEquals("123", recommendedPlaylistDto.getPlaylistStringId());
        assertFalse(recommendedPlaylistDto.isActual());
        assertEquals(2, recommendedPlaylistDto.getNumberOfTracks().intValue());
        assertEquals("ArtistTested2", recommendedPlaylistDto.getPlaylistTracks().get(1).getArtists());
        assertEquals("6543", recommendedPlaylistDto.getPlaylistTracks().get(1).getTrackStringId());
    }

    @Test
    public void mapRecommendedPlaylistToUserPlaylistTest() {
        //Given
        List<RecommendedPlaylistTrack> recommendedPlaylistTracks = new ArrayList<>();
        RecommendedPlaylistTrack recommendedPlaylistTrack1 = new RecommendedPlaylistTrack.Builder()
                .title("TrackTested1")
                .artists("ArtistTested1")
                .stringId("0987")
                .sample("https://testTrack1.com").build();
        RecommendedPlaylistTrack recommendedPlaylistTrack2 = new RecommendedPlaylistTrack.Builder()
                .title("TrackTested2")
                .artists("ArtistTested2")
                .stringId("6543")
                .sample("https://testTrack2.com").build();
        recommendedPlaylistTracks.add(recommendedPlaylistTrack1);
        recommendedPlaylistTracks.add(recommendedPlaylistTrack2);

        RecommendedPlaylist recommendedPlaylist = new RecommendedPlaylist.Builder()
                .playlistTracks(recommendedPlaylistTracks)
                .name("Playlist_recommended_test")
                .actual(false)
                .stringId("pl1234").build();

        //When
        UserPlaylist userPlaylist = dbPlaylistMapper.mapRecommendedPlaylistToUserPlaylist(recommendedPlaylist);

        //Then
        assertEquals("TrackTested2", userPlaylist.getTracks().get(1).getTitle());
        assertEquals("ArtistTested1", userPlaylist.getTracks().get(0).getArtists());
        assertEquals("0987", userPlaylist.getTracks().get(0).getTrackStringId());
        assertEquals("pl1234", userPlaylist.getPlaylistStringId());
        assertEquals("Playlist_recommended_test", userPlaylist.getName());
    }
}