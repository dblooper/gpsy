package com.gpsy.domain.audd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//@Component
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LyricsBaseDto {

    @JsonProperty(value = "status")
    private String status;

    @JsonProperty(value = "result")
    private List<LyricsDto> lyrics;

    public LyricsBaseDto(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LyricsBaseDto{" +
                "status='" + status + '\'' +
                ", lyrics=" + lyrics +
                '}';
    }


}
