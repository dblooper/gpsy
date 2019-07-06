package com.gpsy.repository.audd;

import com.gpsy.domain.audd.Library;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibraryRepository extends JpaRepository<Library, Long> {

    Optional<Library> findByLibraryName(String libraryName);
}
