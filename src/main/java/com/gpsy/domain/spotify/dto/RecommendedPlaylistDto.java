package com.gpsy.domain.spotify.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class RecommendedPlaylistDto {

    @JsonProperty(value = "playlistStringId")
    private String playlistStringId;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "playlistTracks")
    private List<RecommendedTrackForPlaylistDto> playlistTracks;

    @JsonProperty(value = "numberOfTracks")
    private Integer numberOfTracks;

    @JsonProperty(value = "actual")
    private boolean actual;
}
