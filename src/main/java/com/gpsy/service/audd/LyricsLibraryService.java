package com.gpsy.service.audd;

import com.gpsy.domain.audd.Library;
import com.gpsy.domain.audd.LyricsInLibrary;
import com.gpsy.domain.dto.LibraryDto;
import com.gpsy.mapper.audd.LibraryMapper;
import com.gpsy.repository.audd.LibraryRepository;
import com.gpsy.repository.audd.LyricsInLibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LyricsLibraryService {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private AuddService auddService;

    public List<Library> fetchLibrariesFromDb() {
        return libraryRepository.findAll();
    }

    public Library saveLyrics(Library library) {
        return libraryRepository.save(library);
    }

    public Library updateLibrary(Library library) {
        List<Library> libraries = libraryRepository.findAll();
        if(libraries.contains(library)) {
           Library libraryToSave = libraries.get(libraries.indexOf(library));
           libraryToSave.setLibraryName(library.getLibraryName());
           return libraryRepository.save(library);
        }
        return library;
    }

    public void deleteLibrary(Library library) {
        libraryRepository.delete(library);
    }

    public void deleteLyricsFromLibrary(Library library) {
    }
 }
