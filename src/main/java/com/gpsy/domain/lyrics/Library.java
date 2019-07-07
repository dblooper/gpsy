package com.gpsy.domain.lyrics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Library library = (Library) o;

        if (id != library.id) return false;
        return libraryName.equals(library.libraryName);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + libraryName.hashCode();
        return result;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }
}
