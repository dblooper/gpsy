package com.gpsy.domain.lyrics.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LyricsBaseDto that = (LyricsBaseDto) o;

        if (statusCode != that.statusCode) return false;
        return body.equals(that.body);
    }

    @Override
    public int hashCode() {
        int result = statusCode;
        result = 31 * result + body.hashCode();
        return result;
    }
}
