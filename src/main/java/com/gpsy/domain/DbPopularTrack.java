package com.gpsy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name="popular_tracks")
@NoArgsConstructor
@Getter
public class DbPopularTrack implements Comparable<DbPopularTrack> {

    @Id
    @NotNull
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(name = "track_IDs")
    private String trackId;

    @NotNull
    @Column(name = "titles")
    private String title;

    @Column(name = "authors")
    private String authors;

    @Column(name = "popularity")
    private Integer popularity;

    public DbPopularTrack(String trackId, String title, String authors, int popularity) {
        this.trackId = trackId;
        this.title = title;
        this.authors = authors;
        this.popularity = popularity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbPopularTrack dbPopularTrack = (DbPopularTrack) o;

        return trackId.equals(dbPopularTrack.trackId);
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(trackId);
    }

    @Override
    public int compareTo(DbPopularTrack dbPopularTrack) {
        return this.getPopularity().compareTo(dbPopularTrack.getPopularity());
    }
}
