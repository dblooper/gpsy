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
    private long uniqueId;

    private String track_ids;

    private String titles;

    private String authors;

    private Integer popularity;

    public DbMostFrequentTrack(String track_ids, String titles, String authors, Integer popularity) {
        this.track_ids = track_ids;
        this.titles = titles;
        this.authors = authors;
        this.popularity = popularity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbMostFrequentTrack that = (DbMostFrequentTrack) o;

        return track_ids.equals(that.track_ids);

    }

    @Override
    public int hashCode() {
        return track_ids.hashCode();
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    @Override
    public int compareTo(DbMostFrequentTrack dbMostFrequentTrack) {
        return this.popularity.compareTo(dbMostFrequentTrack.getPopularity());
    }
}
