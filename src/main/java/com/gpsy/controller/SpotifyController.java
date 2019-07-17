package com.gpsy.controller;

import com.gpsy.domain.spotify.dto.*;
import com.gpsy.facade.SpotifyFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@Validated
@RequestMapping("/v1/gpsy")
@CrossOrigin("*")
public class SpotifyController {

    @Autowired
    private SpotifyFacade spotifyFacade;

    @GetMapping(value = "/tracks/search")
    public List<SearchTrackDto> fetchSearchedTracks(@RequestParam @Size(min = 1, max = 20) String searchedItem) {
        return spotifyFacade.fetchSearchedTracks(searchedItem);
    }

    @GetMapping(value = "/tracks/spotify/popular")
    public List<PopularTrackDto> fetchPopularTracks(@RequestParam @Max(20) @Min(1) int qty) {
        return spotifyFacade.fetchPopularTracks(qty);
    }

    @GetMapping(value = "/tracks/recent")
    public List<RecentPlayedTrackDto> fetchRecentTracks(@RequestParam @Max(50) @Min(1) int qty) {
        return spotifyFacade.fetchRecentTracks(qty);
    }

    @GetMapping(value = "/tracks/frequent")
    public List<MostFrequentTrackDto> fetchMostFrequentTracks(@RequestParam @Max(20) @Min(1) int qty) {
        return spotifyFacade.fetchMostFrequentTracks(qty);
    }

    @GetMapping(value = "/tracks/recommended")
    public List<RecommendedTrackDto> fetchRecommendedTracks(@RequestParam @Max(20) @Min(1) int qty) {
        return spotifyFacade.fetchRecommendedTracks(qty);
    }

    @GetMapping(value = "/playlists/current")
    public List<UserPlaylistDto> fetchCurrentUserPlaylists() {
        return spotifyFacade.fetchCurrentUserPlaylists();
    }

    @GetMapping(value = "/playlists/recommended")
    public RecommendedPlaylistDto fetchRecommendedPlaylist() {
        return spotifyFacade.fetchRecommendedPlaylist();
    }

    @PostMapping(value = "/playlists/tracks/add")
    public void addTracksToUserPlaylist(@RequestBody UserPlaylistDto playlistDto) {
        spotifyFacade.addTracksToUserPlaylist(playlistDto);
    }

    @PostMapping(value = "/playlists/new")
    public void createNewPlaylist(@RequestBody UserPlaylistDto playlistDto) {
        spotifyFacade.createNewPlaylist(playlistDto);
    }

    @PutMapping(value = "/playlists/recommended/new")
    public RecommendedPlaylistDto updateFetchRecommendedPlaylist(@RequestParam @Min(1) @Max(50) Integer qty) {
        return spotifyFacade.updateFetchRecommendedPlaylist(qty);
    }

    @PutMapping(value = "/playlists/recommended/change")
    public RecommendedPlaylistDto changeQuantityOfRecommendedTracks(@RequestParam @Min(1) @Max(50) Integer qty) {
       return spotifyFacade.changeQuantityOfRecommendedTracks(qty);
    }

    @PutMapping(value = "/playlists/update")
    public UserPlaylistDto updatePlaylistDetails(@RequestBody UserPlaylistDto userPlaylistDto) {
        return spotifyFacade.updatePlaylistDetails(userPlaylistDto);
    }

    @DeleteMapping(value = "/playlists/tracks/delete")
    public void deleteUserPlaylistTrack(@RequestBody UserPlaylistDto playlistDto) {
        spotifyFacade.deleteUserPlaylistTrack(playlistDto);
    }


}
