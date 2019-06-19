package com.gpsy.service;

import com.gpsy.domain.DbTrack;
import com.gpsy.mapper.TrackMapper;
import com.gpsy.repository.SpotifyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DbService {

    @Autowired
    private SpotifyRepository spotifyRepository;

    @Autowired
    private TrackMapper trackMapper;

    public List<DbTrack> savePopularTracks() {
        return new ArrayList<>();
    }

}
