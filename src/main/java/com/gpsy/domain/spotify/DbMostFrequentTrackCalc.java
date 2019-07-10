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
public class DbMostFrequentTrackCalc {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long mostFrequentTracksCalcId;

    @Column(name = "track_string_id")
    private String trackStringId;

    private String title;

    private String artists;

    private int popularity;

    public DbMostFrequentTrackCalc(String trackStringId, String title, String artists, int popularity) {
        this.trackStringId = trackStringId;
        this.title = title;
        this.artists = artists;
        this.popularity = popularity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbMostFrequentTrackCalc that = (DbMostFrequentTrackCalc) o;

        return trackStringId.equals(that.trackStringId);

    }

    @Override
    public int hashCode() {
        return trackStringId.hashCode();
    }

}
