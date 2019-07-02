package com.gpsy.domain.audd;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

//@Component
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackInfoForLyricsDto {

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "authors")
    private String authors;
}
