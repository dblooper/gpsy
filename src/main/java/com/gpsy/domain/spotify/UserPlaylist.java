package com.gpsy.domain.spotify;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_playlists")
@NoArgsConstructor
@Getter
public class UserPlaylist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long playlistId;

    private String name;

    @Column(name = "playlist_string_id")
    private String playlistStringId;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "JOIN_PLAYLIST_TRACK",
            joinColumns = {@JoinColumn(name = "playlist_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "track_id", referencedColumnName = "id")}
    )
    private List<PlaylistTrack> tracks = new ArrayList<>();

    private UserPlaylist(String name, String playlistStringId, List<PlaylistTrack> playlistTracks) {
        this.name = name;
        this.playlistStringId = playlistStringId;
        this.tracks = playlistTracks;
    }

    public static class UserPlaylistBuilder {
        private String name;
        private String playlistStringId;
        private List<PlaylistTrack> playlistTracks;

        public UserPlaylistBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserPlaylistBuilder stringId(String playlistStringId) {
            this.playlistStringId = playlistStringId;
            return this;
        }

        public UserPlaylistBuilder tracks(List<PlaylistTrack> playlistTracks) {
            this.playlistTracks = playlistTracks;
            return this;
        }

        public UserPlaylist build() {
            return new UserPlaylist(name, playlistStringId, playlistTracks);
        }
    }

    private void setPlaylistTracks(List<PlaylistTrack> playlistTracks) {
        this.tracks = playlistTracks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPlaylist that = (UserPlaylist) o;

        return playlistStringId.equals(that.playlistStringId);
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(this.playlistStringId);
    }

    public void setTracks(List<PlaylistTrack> tracks) {
        this.tracks = tracks;
    }

    public void setName(String name) {
        this.name = name;
    }
}
