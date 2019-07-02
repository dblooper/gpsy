package com.gpsy.domain.dto;

import com.gpsy.domain.DbRecommendedTrack;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import java.util.List;

@AllArgsConstructor
@Getter
public class RecommendedPlaylistDto {

    private String playlistStringId;

    private String name;

    private List<RecommendedTrackForPlaylistDto> playlistTracks;

    private Integer numberOfTracks;

    private boolean actual;
}
