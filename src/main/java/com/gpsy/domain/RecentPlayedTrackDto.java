package com.gpsy.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecentPlayedTrackDto {

    private long id;

    private String trackId;

    private String title;

    private String authors;

    private String playDate;

    public RecentPlayedTrackDto(String trackId, String title, String authors, String playDate) {
        this.trackId = trackId;
        this.title = title;
        this.authors = authors;
        this.playDate = playDate;
    }
}
