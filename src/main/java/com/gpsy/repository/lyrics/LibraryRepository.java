package com.gpsy.repository.lyrics;

import com.gpsy.domain.lyrics.Library;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibraryRepository extends JpaRepository<Library, Long> {

    @Override
    void deleteById(Long id);

    Optional<Library> findById(long libraryId);

}
