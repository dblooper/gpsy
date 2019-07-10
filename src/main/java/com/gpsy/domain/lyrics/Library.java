package com.gpsy.domain.lyrics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "library")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long libraryId;

    private String libraryName;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "JOIN_LIBRARY_LYRICS",
            joinColumns = {@JoinColumn(name = "library_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "lyrics_id", referencedColumnName = "id")}
    )
    private List<LyricsInLibrary> lyrics = new ArrayList<>();

    public Library(String libraryName, List<LyricsInLibrary> lyrics) {
        this.libraryName = libraryName;
        this.lyrics = lyrics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Library library = (Library) o;

        if (libraryId != library.libraryId) return false;
        return libraryName.equals(library.libraryName);
    }

    @Override
    public int hashCode() {
        int result = (int) (libraryId ^ (libraryId >>> 32));
        result = 31 * result + libraryName.hashCode();
        return result;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }
}
