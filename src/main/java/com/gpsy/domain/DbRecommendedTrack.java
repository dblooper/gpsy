package com.gpsy.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "recommended_tracks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DbRecommendedTrack {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "tracks_sting_id")
    private String stringId;

}
