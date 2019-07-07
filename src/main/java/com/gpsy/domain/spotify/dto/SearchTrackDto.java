package com.gpsy.domain.spotify.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Getter
public class SearchTrackDto extends RecommendedTrackDto {

    public SearchTrackDto(String stringId, String titles, String authors, String sample) {
        super(stringId, titles, authors, sample);
    }
}
