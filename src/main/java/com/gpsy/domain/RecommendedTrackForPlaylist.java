package com.gpsy.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "recommended_for_playlist")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RecommendedTrackForPlaylist {

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

    @ManyToOne
    @JoinColumn(name = "playlists_ids")
    private RecommendedPlaylist recommendedPlaylist;

    public RecommendedTrackForPlaylist(String stringId, String titles, String authors, String url) {
        this.stringId = stringId;
        this.titles = titles;
        this.authors = authors;
        this.url = url;
    }

    public void setRecommendedPlaylist(RecommendedPlaylist recommendedPlaylist) {
        this.recommendedPlaylist = recommendedPlaylist;
    }
}
