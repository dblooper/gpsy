package com.gpsy.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CollectionId;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_playlists")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DbUserPlaylist {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "playlist_id")
    private long playlistId;

    @Column(name= "names")
    private String name;

    @Column(name= "playlists_ids")
    private String playlistStringId;

    @Column(name = "tracks")
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "JOIN_PLAYLIST_TRACK",
            joinColumns = {@JoinColumn(name = "playlist_id", referencedColumnName = "playlist_id")},
            inverseJoinColumns = {@JoinColumn(name = "track_id", referencedColumnName = "track_id")}
    )
    private List<PlaylistTrack> tracks = new ArrayList<>();

    public DbUserPlaylist(String name, String playlistStringId, List<PlaylistTrack> playlistTracks) {
        this.name = name;
        this.playlistStringId = playlistStringId;
        this.tracks = playlistTracks;
    }

    private void setPlaylistTracks(List<PlaylistTrack> playlistTracks) {
        this.tracks = playlistTracks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbUserPlaylist that = (DbUserPlaylist) o;

        return playlistStringId.equals(that.playlistStringId);

    }

    @Override
    public int hashCode() {
        return Integer.parseInt(this.playlistStringId);
    }
}
