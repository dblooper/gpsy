package com.gpsy.externalApis.auddApi.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpsy.domain.audd.LyricsBaseDto;
import com.gpsy.domain.audd.LyricsDto;
import com.gpsy.domain.audd.LyricsHeader;
import com.gpsy.domain.audd.TrackInfoForLyricsDto;
import com.gpsy.exceptions.LyricsServerResponseException;
import com.gpsy.externalApis.auddApi.config.AuddConfig;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.JsonPath;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Component
@Getter
public class AuddClient {

    private final static String RESPONSE_NULL_STATUS = "error-no object retrieved";

    @Autowired
    private AuddConfig auddConfig;

    @Autowired
    private RestTemplate restTemplate;

    public LyricsBaseDto fetchLyrics(final TrackInfoForLyricsDto trackInfoForLyricsDto) throws LyricsServerResponseException {

        URI uri = UriComponentsBuilder.fromHttpUrl(auddConfig.getAuddApiEndpointRoot() + "matcher.lyrics.get")
                .queryParam("format", "json" )
                .queryParam("q_track", trackInfoForLyricsDto.getTitle())
                .queryParam("q_artist", trackInfoForLyricsDto.getAuthors())
                .queryParam("apikey", "923518c70a4a01092f1989644c9642e3")
//                .queryParam("q",trackInfoForLyricsDto.getTitle())
                .build().encode().toUri();
        System.out.println(uri);
//        try {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        String responseValue = Optional.ofNullable(restTemplate.getForObject(uri, String.class)).orElseThrow(LyricsServerResponseException::new);
//        LyricsBaseDto responseValue = Optional.ofNullable(res.getBody()).orElse(new LyricsBaseDto(new LyricsHeader(404)));
        System.out.println(responseValue);

        try {
            JsonNode lyricsBaseNode = new ObjectMapper().readTree(responseValue);
            LyricsBaseDto lyricsResponse = new LyricsBaseDto(lyricsBaseNode.get("message").get("header").get("status_code").asInt(),
                                                                new LyricsDto(trackInfoForLyricsDto.getTitle(),
                                                                        trackInfoForLyricsDto.getAuthors(),
                                                                        lyricsBaseNode.get("message").get("body").get("lyrics").get("lyrics_body").textValue()));


            if( lyricsResponse.getStatusCode() != 200) {
                lyricsResponse.setBody(new LyricsDto(trackInfoForLyricsDto.getTitle(), trackInfoForLyricsDto.getAuthors(), "n/a"));
            }

            return lyricsResponse;

        } catch(IOException e) {
            System.out.println(e.getMessage());
            return new LyricsBaseDto(404 ,new LyricsDto(trackInfoForLyricsDto.getTitle(), trackInfoForLyricsDto.getAuthors(), "n/a"));
        }

    }

}
