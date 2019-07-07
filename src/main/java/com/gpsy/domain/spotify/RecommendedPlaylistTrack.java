package com.gpsy.domain.spotify;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recommended_for_playlist")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecommendedPlaylistTrack {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "track_id", unique = true)
    private Long id;

    @Column(name = "tracks_sting_id")
    private String stringId;

    @Column(name = "titles")
    private String titles;

    @Column(name = "authors")
    private String authors;

    @Column(name = "short_plays")
    private String url;

    @ManyToMany(
            cascade = CascadeType.ALL,
            mappedBy = "recommendedPlaylistTracks"
    )
    private List<RecommendedPlaylist> recommendedPlaylist = new ArrayList<>();

    public RecommendedPlaylistTrack(String stringId, String titles, String authors, String url) {
        this.stringId = stringId;
        this.titles = titles;
        this.authors = authors;
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecommendedPlaylistTrack that = (RecommendedPlaylistTrack) o;

        return stringId.equals(that.stringId);
    }

    @Override
    public int hashCode() {
        return stringId.hashCode();
    }
}
