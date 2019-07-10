package com.gpsy.domain.lyrics;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "lyrics")
@NoArgsConstructor
@Getter
public class DbLyrics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long lyricsId;

    private String title;

    private String artists;

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

    public DbLyrics(String title, String artists, String lyrics) {
        this.title = title;
        this.artists = artists;
        this.lyrics = lyrics;
    }
}
