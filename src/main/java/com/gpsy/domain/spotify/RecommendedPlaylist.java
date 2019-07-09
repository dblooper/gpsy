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
@Table(name = "recommended_playlists")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecommendedPlaylist {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "playlist_id", unique = true)
    private Long id;

    @Column(name = "playlists_ids")
    private String playlistStringId;

    @Column(name= "names")
    private String name;

    @Column(name = "number_of_tracks")
    private Integer numberOfTracks;

    @Column(name = "actual")
    private boolean actual;

    @ManyToMany(cascade = CascadeType.ALL,
                fetch = FetchType.EAGER)
    @JoinTable(
            name = "JOIN_PL_RECOMMENDED_TRACK",
            joinColumns = {@JoinColumn(name = "playlist_id", referencedColumnName = "playlist_id")},
                    inverseJoinColumns = {@JoinColumn(name = "track_id", referencedColumnName = "track_id")}
    )
    private List<RecommendedPlaylistTrack> recommendedPlaylistTracks = new ArrayList<>();

    public RecommendedPlaylist(String playlistStringId, String name, List<RecommendedPlaylistTrack> recommendedPlaylistTracks, boolean actual) {
        this.playlistStringId = playlistStringId;
        this.name = name;
        this.recommendedPlaylistTracks = recommendedPlaylistTracks;
        this.numberOfTracks = recommendedPlaylistTracks.size();
        this.actual = actual;
    }

    public void setNumberOfTracks() {
        this.numberOfTracks = recommendedPlaylistTracks.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecommendedPlaylist that = (RecommendedPlaylist) o;

        return playlistStringId.equals(that.playlistStringId);
    }

    @Override
    public int hashCode() {
        return playlistStringId.hashCode();
    }
}
