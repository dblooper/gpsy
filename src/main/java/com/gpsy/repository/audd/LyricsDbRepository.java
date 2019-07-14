package com.gpsy.repository.audd;

import com.gpsy.domain.lyrics.DbLyrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface LyricsDbRepository extends JpaRepository<DbLyrics, Long> {

    @Query(value = "SELECT * FROM gpsy.lyrics L " +
            "WHERE L.title LIKE CONCAT(:TITLE, '%') AND L.artists LIKE CONCAT(:ARTIST, '%')",
            nativeQuery = true)
    Optional<DbLyrics> findByTitleAndArtist(@Param("TITLE") String title, @Param("ARTIST") String artist);

}
