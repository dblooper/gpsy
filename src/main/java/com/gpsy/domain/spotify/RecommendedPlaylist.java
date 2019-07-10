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
    @Column(name = "id")
    private long recommendedPlaylistId;

    @Column(name = "playlist_string_id")
    private String stringId;

    private String name;

    @Column(name = "number_of_tracks")
    private int numberOfTracks;

    private boolean actual;

    @ManyToMany(cascade = CascadeType.ALL,
                fetch = FetchType.EAGER)
    @JoinTable(
            name = "JOIN_PL_RECOMMENDED_TRACK",
            joinColumns = {@JoinColumn(name = "playlist_id", referencedColumnName = "id")},
                    inverseJoinColumns = {@JoinColumn(name = "track_id", referencedColumnName = "id")}
    )
    private List<RecommendedPlaylistTrack> recommendedPlaylistTracks = new ArrayList<>();

    public RecommendedPlaylist(String stringId, String name, List<RecommendedPlaylistTrack> recommendedPlaylistTracks, boolean actual) {
        this.stringId = stringId;
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

        return stringId.equals(that.stringId);
    }

    @Override
    public int hashCode() {
        return stringId.hashCode();
    }
}
