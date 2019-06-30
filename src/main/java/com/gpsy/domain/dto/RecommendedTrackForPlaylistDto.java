package com.gpsy.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecommendedTrackForPlaylistDto {

    private String stringId;

    private String titles;

    private String authors;

    private String url;
}
