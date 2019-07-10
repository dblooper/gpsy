package com.gpsy.domain.spotify;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NamedNativeQuery(
        name = "RecentPlayedTrack.retrieveWeekMostPopularTrack",
        query = "SELECT id, track_string_id, title, artists, COUNT(*) AS popularity " +
                "FROM recent_tracks " +
                "GROUP BY track_string_id " +
                "order by popularity desc",
        resultClass = DbMostFrequentTrackCalc.class
)

@Entity
@Table(name = "recent_tracks")
@NoArgsConstructor
@Getter
public class RecentPlayedTrack implements Comparable<RecentPlayedTrack> {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private long recentTrackId;

    @Column(name = "track_string_id")
    private String trackStringId;

    private String title;

    private String artists;

    @Column(name = "play_date")
    private Date playDate;

    private RecentPlayedTrack(String trackStringId, String title, String artists, Date playDate) {
        this.trackStringId = trackStringId;
        this.title = title;
        this.artists = artists;
        this.playDate = playDate;
    }

    public static class RecentPlayedTrackBuilder {

        private String trackStringId;
        private String title;
        private String artists;
        private Date playDate;

        public RecentPlayedTrackBuilder stringId(String trackStringId) {
            this.trackStringId = trackStringId;
            return this;
        }

        public RecentPlayedTrackBuilder title(String title) {
            this.title = title;
            return this;
        }

        public RecentPlayedTrackBuilder artists(String artists) {
            this.artists = artists;
            return this;
        }

        public RecentPlayedTrackBuilder playDate(Date playDate) {
            this.playDate = playDate;
            return this;
        }

        public RecentPlayedTrack build() {
            return new RecentPlayedTrack(trackStringId, title, artists, playDate);
        }
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

        return trackStringId.equals(that.trackStringId);

    }

    @Override
    public int hashCode() {
        return trackStringId.hashCode();
    }
}
