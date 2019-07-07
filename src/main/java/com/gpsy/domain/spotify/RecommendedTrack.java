package com.gpsy.domain.spotify;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "recommended_tracks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RecommendedTrack {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "tracks_sting_id")
    private String stringId;

    @Column(name = "titles")
    private String titles;

    @Column(name = "authors")
    private String authors;

    @Column(name = "short_plays")
    private String url;

    public RecommendedTrack(String stringId, String titles, String authors, String url) {
        this.stringId = stringId;
        this.titles = titles;
        this.authors = authors;
        this.url = url;
    }
}
