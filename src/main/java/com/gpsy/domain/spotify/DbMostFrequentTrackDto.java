package com.gpsy.domain.spotify;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "temp")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DbMostFrequentTrackDto {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String track_ids;

    private String titles;

    private String authors;

    private int popularity;

    public DbMostFrequentTrackDto(String track_ids, String titles, String authors, int popularity) {
        this.track_ids = track_ids;
        this.titles = titles;
        this.authors = authors;
        this.popularity = popularity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbMostFrequentTrackDto that = (DbMostFrequentTrackDto) o;

        return track_ids.equals(that.track_ids);

    }

    @Override
    public int hashCode() {
        return track_ids.hashCode();
    }

}
