package com.gpsy.domain.lyrics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "library_lyrics")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LyricsInLibrary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "id")
    private Long lyricsId;

    private String title;

    private String artists;

    @Column(name = "body", columnDefinition = "TEXT")
    private String lyrics;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "lyrics")
    private List<Library> library = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LyricsInLibrary lyricsInLibrary = (LyricsInLibrary) o;

        return lyrics.equals(lyricsInLibrary.getLyrics());
    }

    @Override
    public int hashCode() {
        return lyrics.hashCode();
    }

    public LyricsInLibrary(String title, String artists, String lyrics) {
        this.title = title;
        this.artists = artists;
        this.lyrics = lyrics;
    }

    private void setLibrary(List<Library> library) {
        this.library = library;
    }
}
