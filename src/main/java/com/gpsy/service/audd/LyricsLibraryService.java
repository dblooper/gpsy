package com.gpsy.service.audd;

import com.gpsy.domain.audd.Library;
import com.gpsy.domain.audd.LyricsInLibrary;
import com.gpsy.domain.dto.LibraryDto;
import com.gpsy.exceptions.LibraryNotFoundException;
import com.gpsy.mapper.audd.LibraryMapper;
import com.gpsy.repository.audd.LibraryRepository;
import com.gpsy.repository.audd.LyricsInLibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LyricsLibraryService {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private AuddService auddService;

    public List<Library> fetchLibrariesFromDb() {
        return libraryRepository.findAll();
    }

    public Library saveLibrary(Library library) {
        return libraryRepository.save(library);
    }

    public LyricsInLibrary saveLyricsInLibrary(Library library) throws LibraryNotFoundException {
        Optional<Library> libraryToModify = Optional.ofNullable(libraryRepository.findByLibraryName(library.getLibraryName())).orElseThrow(LibraryNotFoundException::new);
        libraryToModify.ifPresent(libraryToAdd -> {
            if(library.getLyrics().size() > 0) {
                libraryToAdd.getLyrics().add(library.getLyrics().get(0));
                libraryRepository.save(libraryToAdd);
            }});
        return library.getLyrics().get(0);
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

    public void deleteLyricsFromLibrary(Library library) throws LibraryNotFoundException {
        Optional<Library> libraryToModify = Optional.ofNullable(libraryRepository.findByLibraryName(library.getLibraryName())).orElseThrow(LibraryNotFoundException::new);
        libraryToModify.ifPresent(libraryPresent -> {
            libraryPresent.getLyrics().remove(library.getLyrics().get(0));
            libraryRepository.save(libraryPresent);
        });
    }
 }
