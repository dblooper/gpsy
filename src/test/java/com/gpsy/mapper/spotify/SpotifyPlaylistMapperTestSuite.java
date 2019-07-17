package com.gpsy.mapper.spotify;

import com.gpsy.domain.spotify.UserPlaylist;
import com.gpsy.domain.spotify.dto.PlaylistTrackDto;
import com.gpsy.domain.spotify.dto.UserPlaylistDto;
import com.gpsy.externalApis.spotify.client.SpotifyClient;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Track;
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
public class SpotifyPlaylistMapperTestSuite {

    @Autowired
    private SpotifyPlaylistMapper spotifyPlaylistMapper;

    @MockBean
    private SpotifyClient spotifyClient;

    @Test
    public void mapSpotifyPlaylistToDbUserPlaylistTest() {
        //Given
        List<PlaylistTrack> playlistTracks = new ArrayList<>();
        PlaylistSimplified playlistSimplified = new PlaylistSimplified.Builder()
                .setName("Test_Playlist")
                .setId("12345")
                .build();
        Track spotifyTrack = new Track.Builder()
                .setArtists(new ArtistSimplified.Builder().setName("Test Bob").build())
                .setId("test_track_id1")
                .setName("test_name1")
                .setPopularity(25).build();
        Track spotifyTrack2 = new Track.Builder()
                .setArtists(new ArtistSimplified.Builder().setName("Test John").build())
                .setId("test_track_id2")
                .setName("test_name2")
                .setPopularity(20).build();

        PlaylistTrack playlistTrack1 = new PlaylistTrack.Builder()
                .setTrack(spotifyTrack)
                .build();
        PlaylistTrack playlistTrack2 = new PlaylistTrack.Builder()
                .setTrack(spotifyTrack2)
                .build();
        playlistTracks.add(playlistTrack1);
        playlistTracks.add(playlistTrack2);
        when(spotifyClient.getPlaylistTracks("12345")).thenReturn(playlistTracks);

        //When
        UserPlaylist userPlaylist = spotifyPlaylistMapper.mapSpotifyPlaylistToDbUserPlaylist(playlistSimplified);

        //Then
        assertEquals("test_track_id1", userPlaylist.getTracks().get(0).getTrackStringId());
        assertEquals("Test_Playlist", userPlaylist.getName());
        assertEquals("test_name2", userPlaylist.getTracks().get(1).getTitle());
        assertEquals("Test John", userPlaylist.getTracks().get(1).getArtists());
    }

    @Test
    public void mapToDbUserPlaylistTest() {
        //Given
        List<PlaylistTrackDto> playlistTrackDtoList = new ArrayList<>();
        PlaylistTrackDto playlistTrackDto1 = new PlaylistTrackDto.Builder()
                                                                .title("Test_track1")
                                                                .artists("Artist1")
                                                                .stringId("123").build();

        PlaylistTrackDto playlistTrackDto2 = new PlaylistTrackDto.Builder()
                .title("Test_track2")
                .artists("Artist2")
                .stringId("456").build();
        playlistTrackDtoList.add(playlistTrackDto1);
        playlistTrackDtoList.add(playlistTrackDto2);

        UserPlaylistDto userPlaylistDto = new UserPlaylistDto.Builder()
                .name("Test_Playlist")
                .stringId("123")
                .tracks(playlistTrackDtoList).build();

        //When
        UserPlaylist userPlaylist = spotifyPlaylistMapper.mapToDbUserPlaylist(userPlaylistDto);

        //Then
        assertEquals("Test_track1", userPlaylist.getTracks().get(0).getTitle());
        assertEquals("Artist2", userPlaylist.getTracks().get(1).getArtists());
        assertEquals("456", userPlaylist.getTracks().get(1).getTrackStringId());
        assertEquals("Test_Playlist", userPlaylist.getName());
        assertEquals("123", userPlaylist.getPlaylistStringId());
    }
}