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
public class DbMostFrequentTrack {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long uniqueId;

    private String track_ids;

    private int popularity;

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

    public DbMostFrequentTrack(String track_ids, int popularity) {
        this.track_ids = track_ids;
        this.popularity = popularity;
    }

    public void setTrack_ids(String track_ids) {
        this.track_ids = track_ids;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
}
