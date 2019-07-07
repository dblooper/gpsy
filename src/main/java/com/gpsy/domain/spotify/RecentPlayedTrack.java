package com.gpsy.domain.spotify;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NamedNativeQuery(
        name = "RecentPlayedTrack.retrieveWeekMostPopularTrack",
        query = "SELECT id, track_ids, titles, authors, COUNT(*) AS popularity " +
                "FROM recent_tracks " +
                "GROUP BY track_ids " +
                "order by popularity desc",
        resultClass = DbMostFrequentTrackDto.class
)

@Entity
@Table(name = "recent_tracks")
@NoArgsConstructor
@Getter
public class RecentPlayedTrack implements Comparable<RecentPlayedTrack> {

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

    @Column(name = "play_date")
    private Date playDate;

    public RecentPlayedTrack(String trackId, String title, String authors, Date playDate) {
        this.trackId = trackId;
        this.title = title;
        this.authors = authors;
        this.playDate = playDate;
    }

    @Override
    public int compareTo(RecentPlayedTrack recentPlayedTrack) {
        return this.playDate.compareTo(recentPlayedTrack.getPlayDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecentPlayedTrack that = (RecentPlayedTrack) o;

        return trackId.equals(that.trackId);

    }

    @Override
    public int hashCode() {
        return trackId.hashCode();
    }
}
