package com.gpsy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recommended_playlists")
@NoArgsConstructor
@Getter
public class RecommendedPlaylist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Long id;

    @Column(name = "playlists_ids")
    private String playlistStringId;

    @Column(name= "names")
    private String name;

    @Column
    private Integer numberOfTracks;

    @Column(name = "actual")
    private boolean actual;

    @OneToMany(
            targetEntity = RecommendedTrackForPlaylist.class,
            mappedBy = "recommendedPlaylist",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @Column(name = "recommended_racks")
    private List<RecommendedTrackForPlaylist> recommendedTracksForPlaylist = new ArrayList<>();

    public RecommendedPlaylist(String playlistStringId, String name, List<RecommendedTrackForPlaylist> playlistTracks, Integer numberOfTracks, boolean actual) {
        this.playlistStringId = playlistStringId;
        this.name = name;
        this.recommendedTracksForPlaylist = playlistTracks;
        this.numberOfTracks = numberOfTracks;
        this.actual = actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }

    private void setPlaylistTracks(List<RecommendedTrackForPlaylist> recommendedTrackForPlaylist) {
        this.recommendedTracksForPlaylist = recommendedTrackForPlaylist;
    }
}
