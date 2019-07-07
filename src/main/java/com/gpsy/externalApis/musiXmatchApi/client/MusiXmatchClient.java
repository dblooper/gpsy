package com.gpsy.externalApis.musiXmatchApi.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpsy.domain.lyrics.LyricsBaseDto;
import com.gpsy.domain.lyrics.dto.LyricsDto;
import com.gpsy.domain.lyrics.dto.TrackInfoForLyricsDto;
import com.gpsy.exceptions.MusiXmatchServerResponseException;
import com.gpsy.externalApis.musiXmatchApi.config.MusiXmatchConfig;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
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
                .queryParam("q_artist", trackInfoForLyricsDto.getAuthors())
                .queryParam("apikey", musiXmatchConfig.getApiKey())
                .build().encode().toUri();
        System.out.println(uri);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        String responseValue = Optional.ofNullable(restTemplate.getForObject(uri, String.class)).orElseThrow(MusiXmatchServerResponseException::new);
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
