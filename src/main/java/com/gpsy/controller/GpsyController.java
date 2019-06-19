package com.gpsy.controller;

import com.gpsy.repository.SpotifyRepository;
import com.gpsy.spotify.client.SpotifyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/gpsy")
public class GpsyController {

    @Autowired
    private SpotifyRepository spotifyRepository;

    @Autowired
    private SpotifyClient spotifyClient;

    @GetMapping(value = "/tracks")
    public List<String> getTracks() {
        return spotifyClient.getSpotifyTracks();
    }
}
