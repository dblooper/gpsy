package com.gpsy.service.dbApiServices.spotify;

import com.gpsy.domain.spotify.PlaylistTrack;
import com.gpsy.domain.spotify.PopularTrack;
import com.gpsy.domain.spotify.UserPlaylist;
import com.gpsy.externalApis.spotify.client.SpotifyClient;
import com.gpsy.mapper.spotify.SpotifyPlaylistMapper;
import com.gpsy.mapper.spotify.TrackMapper;
import com.gpsy.repository.spotify.SpotifyPopularTrackRepository;
import com.gpsy.repository.spotify.SpotifyUserPlaylistsRepository;
import com.wrapper.spotify.model_objects.miscellaneous.PlaylistTracksInformation;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SaveSpotifyDataToDbServiceTestSuite {

    @Mock
    private SpotifyPopularTrackRepository spotifyPopularTrackRepository;
    @Mock
    private SpotifyClient spotifyClient;
    @Mock
    private TrackMapper trackMapper;
    @Mock
    private SpotifyUserPlaylistsRepository spotifyUserPlaylistsRepository;
    @Mock
    private SpotifyPlaylistMapper spotifyPlaylistMapper;


    @InjectMocks
    private SaveSpotifyDataToDbService saveSpotifyDataToDbService;

    @Test
    public void shouldSaveOnePopularTrackTest() {
        //Given
        PopularTrack popularTrack = new PopularTrack.Buiilder()
                .artists("artist")
                .popularity(1)
                .stringId("123")
                .title("test")
                .build();
        List<Track> spotifyTracks = new ArrayList<>();
        Track spotifyTrack = new Track.Builder()
                .setName("test")
                .setId("123")
                .setPopularity(1)
                .setArtists(new ArtistSimplified.Builder().setName("artist").build())
                .build();
        spotifyTracks.add(spotifyTrack);
        when(spotifyPopularTrackRepository.findAll()).thenReturn(new ArrayList<>());
        when(spotifyClient.getSpotifyPopularTracks()).thenReturn(spotifyTracks);
        when(trackMapper.mapSpotifyTrackToDbPopularTrack(spotifyTrack)).thenReturn(popularTrack);

        //When
        saveSpotifyDataToDbService.savePopularTracks();

        //Then
        verify(spotifyPopularTrackRepository, times(1)).save(popularTrack);

    }

    @Test
    public void shouldSaveNoPopularTrackTest() {
        //Given
        List<Track> spotifyTracks = new ArrayList<>();
        List<PopularTrack> popularTracks = new ArrayList<>();
        for(int i = 1; i <=10; i++) {
            popularTracks.add(new PopularTrack.Buiilder()
                    .artists("artist")
                    .popularity(1+i)
                    .stringId("123" + i)
                    .title("test")
                    .build());
        }
        Track spotifyTrack1 = new Track.Builder()
                .setName("test")
                .setId("1232")
                .setPopularity(3)
                .setArtists(new ArtistSimplified.Builder().setName("artist").build())
                .build();
        Track spotifyTrack2 = new Track.Builder()
                .setName("test")
                .setId("1233")
                .setPopularity(4)
                .setArtists(new ArtistSimplified.Builder().setName("artist").build())
                .build();
        spotifyTracks.add(spotifyTrack1);
        spotifyTracks.add(spotifyTrack2);

        when(spotifyPopularTrackRepository.findAll()).thenReturn(popularTracks);
        when(spotifyClient.getSpotifyPopularTracks()).thenReturn(spotifyTracks);

        //When
        saveSpotifyDataToDbService.savePopularTracks();

        //Then
        verify(spotifyPopularTrackRepository, times(0)).save(new PopularTrack.Buiilder()
                .artists("artist")
                .popularity(3)
                .stringId("1232")
                .title("test")
                .build());
        verify(spotifyPopularTrackRepository, times(0)).save(new PopularTrack.Buiilder()
                .artists("artist")
                .popularity(4)
                .stringId("1233")
                .title("test")
                .build());

    }

    @Test
    public void shouldSaveUserPlaylist() {
        List<PlaylistTrack> playlistTracks = new ArrayList<>();
        List<PlaylistTrack> playlistTracksFromSpotify = new ArrayList<>();
        List<UserPlaylist> playlistsFromDb = new ArrayList<>();
        List<UserPlaylist> playlistsFromSpotify = new ArrayList<>();
        List<PlaylistSimplified> playlistSimplifiedsFromSpotify = new ArrayList<>();
        PlaylistSimplified playlistSimplified = new PlaylistSimplified.Builder()
                .setId("testSaveId")
                .setName("popular")
                .setTracks(new PlaylistTracksInformation.Builder().setTotal(20).build())
                .build();
        playlistSimplifiedsFromSpotify.add(playlistSimplified);

        for(int i = 1; i <=2; i++) {
            playlistTracks.add(new PlaylistTrack.Builder()
                    .artists("artist")
                    .stringId("123" + i)
                    .title("test")
                    .build());
        }
        for(int i = 1; i <=20; i++) {
            playlistTracksFromSpotify.add(new PlaylistTrack.Builder()
                    .artists("artist")
                    .stringId("123" + i)
                    .title("test")
                    .build());
        }

        UserPlaylist userPlaylist = new UserPlaylist.Builder()
                .name("popular")
                .stringId("testSaveId")
                .tracks(playlistTracks).build();

        UserPlaylist userPlaylistFromSpotify = new UserPlaylist.Builder()
                .name("popular")
                .stringId("testSaveId")
                .tracks(playlistTracksFromSpotify).build();
        playlistsFromDb.add(userPlaylist);
        playlistsFromSpotify.add(userPlaylistFromSpotify);

        when(spotifyUserPlaylistsRepository.findAll()).thenReturn(playlistsFromDb);
        when(spotifyClient.getUserPlaylists()).thenReturn(playlistSimplifiedsFromSpotify);
        when(spotifyPlaylistMapper.mapSpotifyPlaylistToDbUserPlaylist(playlistSimplified)).thenReturn(userPlaylistFromSpotify);

        //When
        saveSpotifyDataToDbService.saveUserPlaylists();

        //Then
        verify(spotifyUserPlaylistsRepository, times(1)).save(any(UserPlaylist.class));
        verify(spotifyUserPlaylistsRepository, times(1)).delete(any(UserPlaylist.class));

    }

}