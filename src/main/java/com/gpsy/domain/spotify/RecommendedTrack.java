package com.gpsy.domain.spotify;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "recommended_tracks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RecommendedTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long recommendedTrackId;

    @Column(name = "track_string_id")
    private String trackStringId;

    private String title;

    private String authors;

    private String sample;

    public RecommendedTrack(String trackStringId, String title, String authors, String sample) {
        this.trackStringId = trackStringId;
        this.title = title;
        this.authors = authors;
        this.sample = sample;
    }
}
