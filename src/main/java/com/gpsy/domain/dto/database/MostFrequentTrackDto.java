package com.gpsy.domain.dto.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "temp")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MostFrequentTrackDto {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String track_ids;

    private int popularity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MostFrequentTrackDto that = (MostFrequentTrackDto) o;

        return track_ids.equals(that.track_ids);

    }

    @Override
    public int hashCode() {
        return track_ids.hashCode();
    }

    public MostFrequentTrackDto(String track_ids, int popularity) {
        this.track_ids = track_ids;
        this.popularity = popularity;
    }
}
