package com.gpsy.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class RecentPlayedTrackDto {

    private String trackId;

    private String title;

    private String authors;

    private LocalDateTime playDate;

    public RecentPlayedTrackDto(String trackId, String title, String authors, LocalDateTime playDate) {
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
