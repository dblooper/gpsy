package com.gpsy.mapper.lyrics;

import com.gpsy.domain.lyrics.dto.DbLyrics;
import com.gpsy.domain.lyrics.dto.LyricsDto;
import org.springframework.stereotype.Component;

@Component
public class LyricsMapper {

    public DbLyrics mapToDbLyrics(LyricsDto lyricsDto) {
        return new DbLyrics(lyricsDto.getTitle(), lyricsDto.getArtist(), lyricsDto.getLyrics());
    }

    public LyricsDto mapToLyricsDto(DbLyrics dbLyrics) {
        return new LyricsDto(dbLyrics.getTitle(), dbLyrics.getArtist(), dbLyrics.getLyrics());
    }
}
