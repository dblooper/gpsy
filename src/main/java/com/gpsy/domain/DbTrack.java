package com.gpsy.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name="tracks")
@NoArgsConstructor
@Getter
public class DbTrack {

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
    private int popularity;

    @Column(name = "play_date")
    private Date playDate;

    public DbTrack(String trackId, String title, String authors, int popularity) {
        this.trackId = trackId;
        this.title = title;
        this.authors = authors;
        this.popularity = popularity;
    }
}
