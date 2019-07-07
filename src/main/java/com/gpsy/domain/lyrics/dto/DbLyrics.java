package com.gpsy.domain.lyrics.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "lyrics")
@NoArgsConstructor
@Getter
public class DbLyrics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Long id;

    @Column(name = "titles")
    private String title;

    @Column(name = "artists")
    private String artist;

    @Column(name = "body", columnDefinition = "TEXT")
    private String lyrics;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbLyrics dbLyrics = (DbLyrics) o;

        return lyrics.equals(dbLyrics.lyrics);
    }

    @Override
    public int hashCode() {
        return lyrics.hashCode();
    }

    public DbLyrics(String title, String artist, String lyrics) {
        this.title = title;
        this.artist = artist;
        this.lyrics = lyrics;
    }
}
