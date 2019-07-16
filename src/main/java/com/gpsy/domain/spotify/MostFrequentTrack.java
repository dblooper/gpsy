package com.gpsy.domain.spotify;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "frequent_tracks")
@NoArgsConstructor
@Getter
public class MostFrequentTrack implements Comparable<MostFrequentTrack> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long mostFrequentTrackId;

    @Column(name = "track_string_id")
    private String trackStringId;

    private String title;

    private String artists;

    private Integer popularity;

    private MostFrequentTrack(String trackStringId, String title, String artists, Integer popularity) {
        this.trackStringId = trackStringId;
        this.title = title;
        this.artists = artists;
        this.popularity = popularity;
    }

    public static class Builder {

        private String trackStringId;
        private String title;
        private String artists;
        private Integer popularity;

        public Builder stringId(String trackStringId) {
            this.trackStringId = trackStringId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder artists(String artists) {
            this.artists = artists;
            return this;
        }

        public Builder popularity(Integer popularity) {
            this.popularity = popularity;
            return this;
        }

        public MostFrequentTrack build() {
            return new MostFrequentTrack(trackStringId, title, artists, popularity);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MostFrequentTrack that = (MostFrequentTrack) o;

        return trackStringId.equals(that.trackStringId);

    }

    @Override
    public int hashCode() {
        return trackStringId.hashCode();
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    @Override
    public int compareTo(MostFrequentTrack mostFrequentTrack) {
        return this.popularity.compareTo(mostFrequentTrack.getPopularity());
    }
}
