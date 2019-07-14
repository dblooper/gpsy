package com.gpsy.domain.lyrics.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LibraryDto {

    @JsonProperty(value = "id")
    private long id;

    @JsonProperty(value = "libraryName")
    private String libraryName;

    @JsonProperty(value = "lyrics")

    private List<LyricsInLibraryDto> lyrics;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LibraryDto that = (LibraryDto) o;

        if (id != that.id) return false;
        return libraryName.equals(that.libraryName);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + libraryName.hashCode();
        return result;
    }
}
