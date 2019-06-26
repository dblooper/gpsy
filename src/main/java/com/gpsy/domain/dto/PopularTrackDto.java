package com.gpsy.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class PopularTrackDto {

    private String trackId;

    private String title;

    private String authors;

    private Integer popularity;
}
