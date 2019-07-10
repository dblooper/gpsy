package com.gpsy.domain.spotify;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="popular_tracks")
@NoArgsConstructor
@Getter
public class PopularTrack implements Comparable<PopularTrack> {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private long popularTrackId;

    @Column(name = "track_string_id")
    private String track_string_id;

    private String title;

    private String artists;

    private Integer popularity;

    public PopularTrack(String track_string_id, String title, String artists, int popularity) {
        this.track_string_id = track_string_id;
        this.title = title;
        this.artists = artists;
        this.popularity = popularity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PopularTrack popularTrack = (PopularTrack) o;

        return track_string_id.equals(popularTrack.track_string_id);
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(track_string_id);
    }

    @Override
    public int compareTo(PopularTrack popularTrack) {
        return this.getPopularity().compareTo(popularTrack.getPopularity());
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }
}
