package com.gpsy.domain.dto;

import com.gpsy.domain.audd.LyricsInLibrary;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class LibraryDto {

    private String libraryName;

    private List<LyricsInLibrary> lyrics;


}
