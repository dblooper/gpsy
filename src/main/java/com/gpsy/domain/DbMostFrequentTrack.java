package com.gpsy.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "frequent_tracks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DbMostFrequentTrack implements Comparable<DbMostFrequentTrack> {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String trackId;

    private String title;

    private String authors;

    private Integer popularity;

    public DbMostFrequentTrack(String trackId, String title, String authors, Integer popularity) {
        this.trackId = trackId;
        this.title = title;
        this.authors = authors;
        this.popularity = popularity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbMostFrequentTrack that = (DbMostFrequentTrack) o;

        return trackId.equals(that.trackId);

    }

    @Override
    public int hashCode() {
        return trackId.hashCode();
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    @Override
    public int compareTo(DbMostFrequentTrack dbMostFrequentTrack) {
        return this.popularity.compareTo(dbMostFrequentTrack.getPopularity());
    }
}
