package com.gpsy.domain.spotify;

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
@Getter
@Setter
public class RecommendedPlaylist {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long recommendedPlaylistId;

    @Column(name = "playlist_string_id")
    private String playlistStringId;

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

    private RecommendedPlaylist(String playlistStringId, String name, List<RecommendedPlaylistTrack> recommendedPlaylistTracks, boolean actual) {
        this.playlistStringId = playlistStringId;
        this.name = name;
        this.recommendedPlaylistTracks = recommendedPlaylistTracks;
        this.numberOfTracks = recommendedPlaylistTracks.size();
        this.actual = actual;
    }

    public static class RecommendedPlaylistBuilder {
        private String playlistStringId;
        private String name;
        private boolean actual;
        private List<RecommendedPlaylistTrack> recommendedPlaylistTracks = new ArrayList<>();

        public RecommendedPlaylistBuilder stringId(String playlistStringId) {
            this.playlistStringId = playlistStringId;
            return this;
        }

        public RecommendedPlaylistBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RecommendedPlaylistBuilder actual(boolean actual) {
            this.actual = actual;
            return this;
        }

        public RecommendedPlaylistBuilder playlistTracks(List<RecommendedPlaylistTrack> recommendedPlaylistTracks) {
            this.recommendedPlaylistTracks = recommendedPlaylistTracks;
            return this;
        }

        public RecommendedPlaylist build() {
            return new RecommendedPlaylist(playlistStringId, name, recommendedPlaylistTracks, actual);
        }
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
