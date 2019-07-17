package com.gpsy.controller;

import com.google.gson.Gson;
import com.gpsy.domain.spotify.dto.*;
import com.gpsy.facade.SpotifyFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SpotifyController.class)
public class SpotifyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpotifyFacade spotifyFacade;

    @Test
    public void shouldfetchSearchedTracks() throws Exception{
        //Given
        List<SearchTrackDto> searchTrackDtoList = new ArrayList<>();
        SearchTrackDto searchTrackDto1 = new SearchTrackDto.Builder()
                .title("Test_Search1")
                .aritsts("Test_Artist1")
                .stringId("Test_id1")
                .sample("https://test.com").build();
        SearchTrackDto searchTrackDto2 = new SearchTrackDto.Builder()
                .title("Test_Search2")
                .aritsts("Test_Artist2")
                .stringId("Test_id2")
                .sample("https://test.com").build();
        searchTrackDtoList.add(searchTrackDto1);
        searchTrackDtoList.add(searchTrackDto2);
        when(spotifyFacade.fetchSearchedTracks("Test_Search")).thenReturn(searchTrackDtoList);

        //When
        mockMvc.perform(get("/v1/gpsy/tracks/search")
                .param("searchedItem", "Test_Search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title", is("Test_Search1")))
                .andExpect(jsonPath("$.[1].trackStringId", is("Test_id2")));
    }

    @Test
    public void fetchPopularTracks() throws Exception {
        //Given
        List<PopularTrackDto> popularTrackDtoList = new ArrayList<>();
        for(int i = 1; i <= 10; i++) {
            popularTrackDtoList.add(new PopularTrackDto.Builder()
                                            .stirngId("Test_id" + i)
                                            .title("Test_title" + i)
                                            .artists("Test_artist" + i)
                                            .popularity(i + 20).build()
                                    );
        }
        when(spotifyFacade.fetchPopularTracks(10)).thenReturn(popularTrackDtoList);

        //When/Then
        mockMvc.perform(get("/v1/gpsy/tracks/spotify/popular")
                        .param("qty", "10"))
                        .andExpect(status().is(200))
                        .andExpect(jsonPath("$.[6].title", is("Test_title7")))
                .andExpect(jsonPath("$.[2].artists", is("Test_artist3")))
                .andExpect(jsonPath("$.[9].trackStringId", is("Test_id10")))
                .andExpect(jsonPath("$.[5].popularity", is(26)));
    }

    @Test
    public void fetchRecentTracks() throws Exception {
        //Given
        List<RecentPlayedTrackDto> recentPlayedTrackDtoList = new ArrayList<>();
        for(int i = 1; i <= 10; i++) {
            recentPlayedTrackDtoList.add(new RecentPlayedTrackDto.Builder()
                    .stringId("Test_id" + i)
                    .title("Test_title" + i)
                    .artists("Test_artist" + i)
                    .playDate(LocalDateTime.of(LocalDate.of(2000+i,1+i,1), LocalTime.of(i,0)))
                    .build()
            );
        }
        when(spotifyFacade.fetchRecentTracks(10)).thenReturn(recentPlayedTrackDtoList);

        //When/Then
        mockMvc.perform(get("/v1/gpsy/tracks/recent")
                .param("qty", "10"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[1].title", is("Test_title2")))
                .andExpect(jsonPath("$.[5].artists", is("Test_artist6")))
                .andExpect(jsonPath("$.[8].trackStringId", is("Test_id9")))
                .andExpect(jsonPath("$.[2].playDate", is("03:00:00 04-01")));

    }

    @Test
    public void fetchCurrentUserPlaylists() throws Exception {
        //Given
        List<PlaylistTrackDto> playlistTrackDtoList = new ArrayList<>();
        List<UserPlaylistDto> userPlaylistDtoList = new ArrayList<>();

        for (int j = 1; j <= 10; j++) {
            playlistTrackDtoList.add(new PlaylistTrackDto.Builder()
                    .title("T" + j)
                    .artists("A" + j)
                    .stringId("I" + j)
                    .build());
        }

        for (int i = 1; i <= 10; i++) {
            userPlaylistDtoList.add(new UserPlaylistDto.Builder()
                    .name("Playlist_test" + i)
                    .stringId("Pl_id" + i)
                    .tracks(playlistTrackDtoList)
                    .build());
        }

        when(spotifyFacade.fetchCurrentUserPlaylists()).thenReturn(userPlaylistDtoList);

        //When/then
        mockMvc.perform(get("/v1/gpsy/playlists/current"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[5].name", is("Playlist_test6")))
                .andExpect(jsonPath("$.[6].playlistStringId", is("Pl_id7")))
                .andExpect(jsonPath("$.[2].tracks.[3].title", is("T4")))
                .andExpect(jsonPath("$.[2].tracks.[5].artists", is("A6")));
    }

    @Test
    public void fetchMostFrequentTracks() throws Exception {
        //Given
        List<MostFrequentTrackDto> mostFrequentTrackDtoList = new ArrayList<>();
        for(int i = 1; i <= 10; i++) {
            mostFrequentTrackDtoList.add(new MostFrequentTrackDto.Builder()
                    .stringId("Test_id" + i)
                    .title("Test_title" + i)
                    .artists("Test_artist" + i)
                    .popularity(i)
                    .build()
            );
        }
        when(spotifyFacade.fetchMostFrequentTracks(10)).thenReturn(mostFrequentTrackDtoList);

        //When/Then
        mockMvc.perform(get("/v1/gpsy/tracks/frequent")
                .param("qty", "10"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[1].title", is("Test_title2")))
                .andExpect(jsonPath("$.[5].artists", is("Test_artist6")))
                .andExpect(jsonPath("$.[8].trackStringId", is("Test_id9")))
                .andExpect(jsonPath("$.[2].popularity", is(3)));
    }

    @Test
    public void schouldNoFetchRecommendedTracks() throws Exception {
        //Given
        when(spotifyFacade.fetchMostFrequentTracks(10)).thenReturn(new ArrayList<>());

        //When/Then
        mockMvc.perform(get("/v1/gpsy/tracks/recommended")
                .param("qty", "10"))
                .andExpect(status().is(200))
                .andExpect(content().string("[]"));
    }

    @Test
    public void shouldReturnEmptyListTryingFetchRecommended() throws Exception {
        //Given
            //no values
        //When/Then
        mockMvc.perform(get("/v1/gpsy/tracks/frequent")
                .param("qty", "-1"))
                .andExpect(status().is(400));
        verify(spotifyFacade, times(0)).fetchRecommendedTracks(-1);
    }

    @Test
    public void fetchRecommendedPlaylist() throws Exception {
        //Given

        List<RecommendedTrackForPlaylistDto> playlistTrackDtoList = new ArrayList<>();

        for (int j = 1; j <= 10; j++) {
            playlistTrackDtoList.add(new RecommendedTrackForPlaylistDto.Builder()
                    .title("T" + j)
                    .artists("A" + j)
                    .stringId("I" + j)
                    .sample("https://track" + j + ".com")
                    .build());
        }

        RecommendedPlaylistDto recommendedPlaylistDto = new RecommendedPlaylistDto.Builder()
                .name("Playlist_test" )
                .stringId("Pl_id")
                .playlistTracks(playlistTrackDtoList)
                .isActual(false)
                .numberOfTracks(playlistTrackDtoList.size())
                .build();

        when(spotifyFacade.fetchRecommendedPlaylist()).thenReturn(recommendedPlaylistDto);

        //When/Then
        mockMvc.perform(get("/v1/gpsy/playlists/recommended"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Playlist_test")))
                .andExpect(jsonPath("$.playlistStringId", is("Pl_id")))
                .andExpect(jsonPath("$.playlistTracks.[8].sample", is("https://track9.com")))
                .andExpect(jsonPath("$.playlistTracks.[6].trackStringId", is("I7")));

    }

    @Test
    public void shouldAddTracksToUserPlaylist() throws Exception {
        //Given
        List<PlaylistTrackDto> playlistTrackDtoList = new ArrayList<>();
        PlaylistTrackDto playlistTrackDto = new PlaylistTrackDto.Builder()
                .title("T_Test")
                .artists("A_Test")
                .stringId("Id_test").build();
        playlistTrackDtoList.add(playlistTrackDto);

        UserPlaylistDto playlistWithNewTrack = new UserPlaylistDto.Builder()
                .name("Playlistest")
                .stringId("ObiO")
                .tracks(playlistTrackDtoList).build();


        Gson gson = new Gson();
        String jsonPlaylist = gson.toJson(playlistWithNewTrack);

        //When/Then
        mockMvc.perform(post("/v1/gpsy/playlists/tracks/add")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonPlaylist))
                .andExpect(status().isOk());

        verify(spotifyFacade, times(1)).addTracksToUserPlaylist(playlistWithNewTrack);
    }

    @Test
    public void createNewPlaylist() throws Exception{
        //Given
        UserPlaylistDto newPlaylist = new UserPlaylistDto.Builder()
                .name("New_Playlist")
                .stringId("Test_id")
                .tracks(new ArrayList<>()).build();


        Gson gson = new Gson();
        String jsonPlaylist = gson.toJson(newPlaylist);

        //When/Then
        mockMvc.perform(post("/v1/gpsy/playlists/new")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonPlaylist))
                .andExpect(status().isOk());

        verify(spotifyFacade, times(1)).createNewPlaylist(newPlaylist);
    }

    @Test
    public void shouldUpdateFetchRecommendedPlaylist() throws Exception {

        List<RecommendedTrackForPlaylistDto> recommendedTracks = new ArrayList<>();
        for (int j = 1; j <= 15; j++) {
                recommendedTracks.add(new RecommendedTrackForPlaylistDto.Builder()
                        .title("T" + j)
                        .artists("A" + j)
                        .stringId("I" + j)
                        .sample("https://track" + j + ".com")
                        .build());
        }
        RecommendedPlaylistDto recommendedPlaylistDto = new RecommendedPlaylistDto.Builder()
                .name("Same_playlist")
                .playlistTracks(recommendedTracks)
                .numberOfTracks(15)
                .stringId("Test_id")
                .isActual(true).build();


        when(spotifyFacade.updateFetchRecommendedPlaylist(15)).thenReturn(recommendedPlaylistDto);

        //When/Then
        mockMvc.perform(put("/v1/gpsy/playlists/recommended/new")
                .param("qty", "15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Same_playlist")))
                .andExpect(jsonPath("$.playlistStringId", is("Test_id")))
                .andExpect(jsonPath("$.playlistTracks.[13].artists", is("A14")));

    }

    @Test
    public void changeQuantityOfRecommendedTracks() throws Exception {

        //When/Then
        mockMvc.perform(put("/v1/gpsy/playlists/recommended/change")
                .param("qty", "-1"))
                .andExpect(status().is(400));

        verify(spotifyFacade, times(0)).changeQuantityOfRecommendedTracks(-1);

    }

    @Test
    public void updatePlaylistDetails() throws Exception {
        //Given
        UserPlaylistDto updatedPlaylist = new UserPlaylistDto.Builder()
                .name("Updated_playlist")
                .stringId("Test_id")
                .tracks(new ArrayList<>()).build();

        when(spotifyFacade.updatePlaylistDetails(updatedPlaylist)).thenReturn(updatedPlaylist);

        Gson gson = new Gson();
        String jsonPlaylist = gson.toJson(updatedPlaylist);

        //When/Then
        mockMvc.perform(put("/v1/gpsy/playlists/update")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonPlaylist))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated_playlist")));

    }

    @Test
    public void deleteUserPlaylistTrack() throws Exception {
        //Given
        List<PlaylistTrackDto> playlistTrackDtoList = new ArrayList<>();
        PlaylistTrackDto playlistTrackDto = new PlaylistTrackDto.Builder()
                .title("T_Test")
                .artists("A_Test")
                .stringId("Id_test").build();
        playlistTrackDtoList.add(playlistTrackDto);

        UserPlaylistDto playlistWithTrackToDelete = new UserPlaylistDto.Builder()
                .name("PlaylistestToDelete")
                .stringId("Del_id")
                .tracks(playlistTrackDtoList).build();


        Gson gson = new Gson();
        String jsonPlaylist = gson.toJson(playlistWithTrackToDelete);

        //When/Then
        mockMvc.perform(delete("/v1/gpsy/playlists/tracks/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonPlaylist))
                .andExpect(status().isOk());

        verify(spotifyFacade, times(1)).deleteUserPlaylistTrack(playlistWithTrackToDelete);
    }
}