package com.gpsy.domain.audd;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

//@Component
@AllArgsConstructor
@Getter
public class TrackInfoForLyricsDto {

    private String title;
    private String authors;
}
