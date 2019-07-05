package com.gpsy.domain.audd;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;

@AllArgsConstructor
@Getter
public class LyricsInLibraryDto {

    private String title;

    private String artist;

    private String lyrics;

}
