package com.gpsy.domain.audd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "library")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Library {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "library_id")
    private long id;

    private String libraryName;

    private String libraryCode;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "JOIN_LIBRARY_LYRICS",
            joinColumns = {@JoinColumn(name = "library_id", referencedColumnName = "library_id")},
            inverseJoinColumns = {@JoinColumn(name = "lyrics_id", referencedColumnName = "lyrics_id")}
    )
    private List<LyricsInLibrary> lyrics = new ArrayList<>();

    public Library(String libraryName, List<LyricsInLibrary> lyrics) {
        this.libraryName = libraryName;
        this.lyrics = lyrics;
        this.libraryCode = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Library library = (Library) o;

        if (!libraryName.equals(library.libraryName)) return false;
        return libraryCode.equals(library.libraryCode);
    }

    @Override
    public int hashCode() {
        int result = libraryName.hashCode();
        result = 31 * result + libraryCode.hashCode();
        return result;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }
}
