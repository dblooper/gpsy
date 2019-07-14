package com.gpsy.service.dbApiServices.lyrics;

import com.gpsy.domain.lyrics.Library;
import com.gpsy.domain.lyrics.LyricsInLibrary;
import com.gpsy.exceptions.LibraryNotFoundException;
import com.gpsy.repository.audd.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LyricsLibraryService {

    @Autowired
    private LibraryRepository libraryRepository;

    public List<Library> fetchLibrariesFromDb() {
        return libraryRepository.findAll();
    }

    public Library saveLibrary(Library library) {
        return libraryRepository.save(library);
    }

    public LyricsInLibrary saveLyricsInLibrary(Library library) throws LibraryNotFoundException {
        Optional<Library> libraryToModify = Optional.ofNullable(libraryRepository.findById(library.getLibraryId())).orElseThrow(LibraryNotFoundException::new);
        libraryToModify.ifPresent(libraryToAdd -> {
            if(library.getLyrics().size() > 0) {
                libraryToAdd.getLyrics().add(library.getLyrics().get(0));
                libraryRepository.save(libraryToAdd);
            }});
        return library.getLyrics().get(0);
    }

    public Library updateLibrary(Library library) throws LibraryNotFoundException{
        Library libraryToEdit = libraryRepository.findById(library.getLibraryId()).orElseThrow(LibraryNotFoundException::new);
        libraryToEdit.setLibraryName(library.getLibraryName());

           return libraryRepository.save(libraryToEdit);
    }

    public void deleteLibrary(Long libraryId) {
        libraryRepository.deleteById(libraryId);
    }

    public void deleteLyricsFromLibrary(Library library) throws LibraryNotFoundException {
        Optional<Library> libraryToModify = Optional.ofNullable(libraryRepository.findById(library.getLibraryId())).orElseThrow(LibraryNotFoundException::new);
        libraryToModify.ifPresent(libraryPresent -> {
            libraryPresent.getLyrics().remove(library.getLyrics().get(0));
            libraryRepository.save(libraryPresent);
        });
    }
 }
