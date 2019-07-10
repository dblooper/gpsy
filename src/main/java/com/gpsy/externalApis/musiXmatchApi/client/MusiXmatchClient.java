package com.gpsy.externalApis.musiXmatchApi.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpsy.domain.lyrics.dto.LyricsBaseDto;
import com.gpsy.domain.lyrics.dto.LyricsDto;
import com.gpsy.domain.lyrics.dto.TrackInfoForLyricsDto;
import com.gpsy.exceptions.MusiXmatchServerResponseException;
import com.gpsy.externalApis.musiXmatchApi.config.MusiXmatchConfig;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Component
@Getter
public class MusiXmatchClient {

    private final static String RESPONSE_NULL_STATUS = "error-no object retrieved";

    @Autowired
    private MusiXmatchConfig musiXmatchConfig;

    @Autowired
    private RestTemplate restTemplate;

    public LyricsBaseDto fetchLyrics(final TrackInfoForLyricsDto trackInfoForLyricsDto) throws MusiXmatchServerResponseException {

        URI uri = UriComponentsBuilder.fromHttpUrl(musiXmatchConfig.getApiEndpointRoot() + "matcher.lyrics.get")
                .queryParam("format", "json" )
                .queryParam("q_track", trackInfoForLyricsDto.getTitle())
                .queryParam("q_artist", trackInfoForLyricsDto.getArtists())
                .queryParam("apikey", musiXmatchConfig.getApiKey())
                .build().encode().toUri();
        System.out.println(uri);

        String responseValue = Optional.ofNullable(restTemplate.getForObject(uri, String.class)).orElseThrow(MusiXmatchServerResponseException::new);
        System.out.println(responseValue);

        try {
            JsonNode lyricsBaseNode = new ObjectMapper().readTree(responseValue);
            if(lyricsBaseNode.get("message").get("body").size() != 0) {
                String lyricsBodyResponse = Optional.ofNullable(lyricsBaseNode.get("message").get("body").get("lyrics").get("lyrics_body").textValue())
                        .orElse("Lyrics not found, sorry :(");
                return new LyricsBaseDto(lyricsBaseNode.get("message").get("header").get("status_code").asInt(),
                                         new LyricsDto(trackInfoForLyricsDto.getTitle(),
                                                        trackInfoForLyricsDto.getArtists(),
                                                         lyricsBodyResponse));
            }
            return new LyricsBaseDto(404 ,new LyricsDto(trackInfoForLyricsDto.getTitle(),
                                                                     trackInfoForLyricsDto.getArtists(),
                                                                     "Lyrics not found, sorry :("));

        } catch(IOException e) {
            System.out.println(e.getMessage());
            return new LyricsBaseDto(404 ,new LyricsDto(trackInfoForLyricsDto.getTitle(),
                                                                     trackInfoForLyricsDto.getArtists(),
                                                                  "Lyrics not found, sorry :("));
        }
    }
}
