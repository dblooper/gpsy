package com.gpsy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "recent_tracks")
@NoArgsConstructor
@Getter
public class DbRecentPlayedTrack implements Comparable<DbRecentPlayedTrack> {

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

    public DbRecentPlayedTrack(String trackId, String title, String authors, Date playDate) {
        this.trackId = trackId;
        this.title = title;
        this.authors = authors;
        this.playDate = playDate;
    }

    @Override
    public int compareTo(DbRecentPlayedTrack dbRecentPlayedTrack) {
        return this.playDate.compareTo(dbRecentPlayedTrack.getPlayDate());
    }
}
