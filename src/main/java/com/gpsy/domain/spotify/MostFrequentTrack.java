package com.gpsy.domain.spotify;

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
public class MostFrequentTrack implements Comparable<MostFrequentTrack> {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String trackId;

    private String title;

    private String authors;

    private Integer popularity;

    public MostFrequentTrack(String trackId, String title, String authors, Integer popularity) {
        this.trackId = trackId;
        this.title = title;
        this.authors = authors;
        this.popularity = popularity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MostFrequentTrack that = (MostFrequentTrack) o;

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
    public int compareTo(MostFrequentTrack mostFrequentTrack) {
        return this.popularity.compareTo(mostFrequentTrack.getPopularity());
    }
}
