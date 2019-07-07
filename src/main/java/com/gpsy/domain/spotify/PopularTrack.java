package com.gpsy.domain.spotify;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="popular_tracks")
@NoArgsConstructor
@Getter
public class PopularTrack implements Comparable<PopularTrack> {

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

    public PopularTrack(String trackId, String title, String authors, int popularity) {
        this.trackId = trackId;
        this.title = title;
        this.authors = authors;
        this.popularity = popularity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PopularTrack popularTrack = (PopularTrack) o;

        return trackId.equals(popularTrack.trackId);
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(trackId);
    }

    @Override
    public int compareTo(PopularTrack popularTrack) {
        return this.getPopularity().compareTo(popularTrack.getPopularity());
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }
}
