package com.gpsy.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class RecommendedTrackDto {

    private String stringId;

    private String titles;

    private String authors;

    private String sample;
}
