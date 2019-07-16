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
    private String trackStringId;

    private String title;

    private String artists;

    private Integer popularity;

    private PopularTrack(String trackStringId, String title, String artists, int popularity) {
        this.trackStringId = trackStringId;
        this.title = title;
        this.artists = artists;
        this.popularity = popularity;
    }

    public static class Buiilder {

        private String trackStringId;
        private String title;
        private String artists;
        private Integer popularity;

        public Buiilder stringId(String trackStringId) {
            this.trackStringId = trackStringId;
            return this;
        }

        public Buiilder title(String title) {
            this.title = title;
            return this;
        }

        public Buiilder artists(String artists) {
            this.artists = artists;
            return this;
        }

        public Buiilder popularity(Integer popularity) {
            this.popularity = popularity;
            return this;
        }

        public PopularTrack build() {
            return new PopularTrack(trackStringId, title, artists, popularity);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PopularTrack popularTrack = (PopularTrack) o;

        return trackStringId.equals(popularTrack.trackStringId);
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(trackStringId);
    }

    @Override
    public int compareTo(PopularTrack popularTrack) {
        return this.getPopularity().compareTo(popularTrack.getPopularity());
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }
}
