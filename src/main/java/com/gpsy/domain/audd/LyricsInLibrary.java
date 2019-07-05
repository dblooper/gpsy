package com.gpsy.domain.audd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "library_lyrics")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LyricsInLibrary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "lyrics_id")
    private Long id;

    @Column(name = "titles")
    private String title;

    @Column(name = "artists")
    private String artist;

    @Column(name = "body", columnDefinition = "TEXT")
    private String lyrics;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "lyrics")
    private Library library;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbLyrics dbLyrics = (DbLyrics) o;

        return lyrics.equals(dbLyrics.getLyrics());
    }

    @Override
    public int hashCode() {
        return lyrics.hashCode();
    }

    public LyricsInLibrary(String title, String artist, String lyrics) {
        this.title = title;
        this.artist = artist;
        this.lyrics = lyrics;
    }

    private void setLibrary(Library library) {
        this.library = library;
    }
}
