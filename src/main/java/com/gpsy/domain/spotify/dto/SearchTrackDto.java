package com.gpsy.domain.spotify.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Getter
public class SearchTrackDto extends RecommendedTrackDto {

    public SearchTrackDto(String trackStringId, String title, String artists, String sample) {
        super(trackStringId, title, artists, sample);
    }
}
