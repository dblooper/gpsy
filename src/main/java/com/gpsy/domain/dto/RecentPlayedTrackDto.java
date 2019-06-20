package com.gpsy.domain.dto;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecentPlayedTrackDto that = (RecentPlayedTrackDto) o;

        return trackId.equals(that.trackId);

    }

    @Override
    public int hashCode() {
        return trackId.hashCode();
    }
}
