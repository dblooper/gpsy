package com.gpsy.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlist_tracks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PlaylistTrack {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "track_id")
    private long trackId;

    @NotNull
    @Column(name = "track_ids")
    private String trackStringId;

    @NotNull
    @Column(name = "titles")
    private String title;

    @Column(name = "authors")
    private String authors;


    @Column(name = "playlists")
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "tracks")
    private List<DbUserPlaylist> playlists = new ArrayList<>();

    public PlaylistTrack(String trackStringId, String title, String authors) {
        this.trackStringId = trackStringId;
        this.title = title;
        this.authors = authors;
    }

    private void setPlaylists(List<DbUserPlaylist> playlists) {
        this.playlists = playlists;
    }

    public String getStringNameForAddingToPlaylist() {
        return "spotify:track:" + this.trackStringId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaylistTrack that = (PlaylistTrack) o;

        return trackStringId.equals(that.trackStringId);
    }

    @Override
    public int hashCode() {
        return trackStringId.hashCode();
    }
}
