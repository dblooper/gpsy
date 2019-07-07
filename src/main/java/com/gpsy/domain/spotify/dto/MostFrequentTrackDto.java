package com.gpsy.domain.spotify.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class MostFrequentTrackDto {

    private String trackId;

    private String title;

    private String authors;

    private Integer popularity;
}
