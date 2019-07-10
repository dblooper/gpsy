package com.gpsy.domain.spotify;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlist_tracks")
@NoArgsConstructor
@Getter
public class PlaylistTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long trackId;

    @Column(name = "track_string_id")
    private String trackStringId;

    private String title;

    private String artists;

    @Column(name = "playlist")
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "tracks")
    private List<UserPlaylist> playlists = new ArrayList<>();

    private PlaylistTrack(String trackStringId, String title, String artists) {
        this.trackStringId = trackStringId;
        this.title = title;
        this.artists = artists;
    }

    public static class PlaylistTrackBuilder {

        private String trackStringId;
        private String title;
        private String artists;

        public PlaylistTrackBuilder stringId(String trackStringId) {
            this.trackStringId = trackStringId;
            return this;
        }

        public PlaylistTrackBuilder title(String title) {
            this.title = title;
            return this;
        }

        public PlaylistTrackBuilder artists(String artists) {
            this.artists = artists;
            return this;
        }

        public PlaylistTrack build() {
            return new PlaylistTrack(trackStringId, title, artists);
        }
    }

    private void setPlaylists(List<UserPlaylist> playlists) {
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
