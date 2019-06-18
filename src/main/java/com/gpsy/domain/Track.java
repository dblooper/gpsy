package com.gpsy.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="tracks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Track {

    @Id
    @NotNull
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @NotNull
    @Column(name = "names")
    private String name;

    @Column(name = "authors")
    private String author;
}
