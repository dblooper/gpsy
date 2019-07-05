package com.gpsy.domain.audd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Component
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LyricsBaseDto {

    @JsonProperty(value = "statusCode")
    private int statusCode;

    @JsonProperty(value = "result")
    private LyricsDto body;

    public LyricsBaseDto(int statusCode, LyricsDto body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    @Override
    public String toString() {
        return "LyricsBaseDto{" +
                "statusCode='" + statusCode + '\'' +
                ", body=" + body +
                '}';
    }


}
