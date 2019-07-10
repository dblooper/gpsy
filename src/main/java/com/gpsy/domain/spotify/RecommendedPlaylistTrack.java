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
    @Column(name = "id")
    private long recommendedPlaylistTrackId;

    @Column(name = "track_string_id")
    private String trackStringId;

    private String title;

    private String artists;

    private String sample;

    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "recommendedPlaylistTracks"
    )
    private List<RecommendedPlaylist> recommendedPlaylist = new ArrayList<>();

    public RecommendedPlaylistTrack(String trackStringId, String title, String artists, String sample) {
        this.trackStringId = trackStringId;
        this.title = title;
        this.artists = artists;
        this.sample = sample;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecommendedPlaylistTrack that = (RecommendedPlaylistTrack) o;

        return trackStringId.equals(that.trackStringId);
    }

    @Override
    public int hashCode() {
        return trackStringId.hashCode();
    }
}
