package com.gpsy.domain.spotify.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class PopularTrackDto {

    private String trackId;

    private String title;

    private String authors;

    private Integer popularity;
}
