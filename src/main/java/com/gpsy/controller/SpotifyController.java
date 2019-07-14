package com.gpsy.controller;

import com.gpsy.domain.spotify.dto.*;
import com.gpsy.facade.SpotifyFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/v1/gpsy")
@CrossOrigin("*")
public class SpotifyController {

    @Autowired
    private SpotifyFacade spotifyFacade;

    @GetMapping(value = "/tracks/search")
    public List<SearchTrackDto> fetchSearchedTracks(@RequestParam String searchedItem) {
        return spotifyFacade.fetchSearchedTracks(searchedItem);
    }

    @GetMapping(value = "/tracks/spotify/popular")
    public List<PopularTrackDto> fetchPopularTracks() {
        return spotifyFacade.fetchPopularTracks();
    }

    @GetMapping(value = "/tracks/recent")
    public List<RecentPlayedTrackDto> fetchRecentTracks() {
        return spotifyFacade.fetchRecentTracks();
    }

    @GetMapping(value = "/playlists/current")
    public List<UserPlaylistDto> fetchCurrentUserPlaylists() {
        return spotifyFacade.fetchCurrentUserPlaylists();
    }

    @GetMapping(value = "/tracks/frequent")
    public List<MostFrequentTrackDto> fetchMostFrequentTracks() {
        return spotifyFacade.fetchMostFrequentTracks();
    }

    @GetMapping(value = "/tracks/recommended")
    public List<RecommendedTrackDto> fetchRecommendedTracks() {
        return spotifyFacade.fetchRecommendedTracks();
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
    public RecommendedPlaylistDto updateFetchRecommendedPlaylist(@RequestParam @Min(1) @Max(50) int qty) {
        return spotifyFacade.updateFetchRecommendedPlaylist(qty);
    }

    @PutMapping(value = "/playlists/recommended/change")
    public RecommendedPlaylistDto changeQuantityOfRecommendedTracks(@RequestParam @Min(1) @Max(50) int qty) {
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
