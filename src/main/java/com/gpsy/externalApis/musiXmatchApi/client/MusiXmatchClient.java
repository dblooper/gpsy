package com.gpsy.externalApis.musiXmatchApi.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpsy.domain.lyrics.dto.LyricsBaseDto;
import com.gpsy.domain.lyrics.dto.LyricsDto;
import com.gpsy.domain.lyrics.dto.TrackInfoForLyricsDto;
import com.gpsy.exceptions.MusiXmatchServerResponseException;
import com.gpsy.externalApis.musiXmatchApi.config.MusiXmatchConfig;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public final static String RESPONSE_NULL_STATUS = "Lyrics not found, sorry :(";

    private static final Logger LOGGER = LoggerFactory.getLogger(MusiXmatchClient.class);

    @Autowired
    private MusiXmatchConfig musiXmatchConfig;

    @Autowired
    private RestTemplate restTemplate;

    public LyricsBaseDto fetchLyrics(final TrackInfoForLyricsDto trackInfoForLyricsDto) {

        URI uri = UriComponentsBuilder.fromHttpUrl(musiXmatchConfig.getApiEndpointRoot() + "matcher.lyrics.get")
                .queryParam("format", "json" )
                .queryParam("q_track", trackInfoForLyricsDto.getTitle())
                .queryParam("q_artist", trackInfoForLyricsDto.getArtists())
                .queryParam("apikey", musiXmatchConfig.getApiKey())
                .build().encode().toUri();


        try {
            String responseValue = Optional.ofNullable(restTemplate.getForObject(uri, String.class)).orElseThrow(MusiXmatchServerResponseException::new);
            JsonNode lyricsBaseNode = new ObjectMapper().readTree(responseValue);
            int serverResponse = lyricsBaseNode.get("message").get("header").get("status_code").asInt();
            if(lyricsBaseNode.get("message").get("body").size() != 0 && serverResponse == 200) {
                String lyricsBodyResponse = Optional.ofNullable(lyricsBaseNode.get("message").get("body").get("lyrics").get("lyrics_body").textValue())
                        .orElse(RESPONSE_NULL_STATUS);

                return new LyricsBaseDto(serverResponse,
                                         new LyricsDto(trackInfoForLyricsDto.getTitle(),
                                                        trackInfoForLyricsDto.getArtists(),
                                                         lyricsBodyResponse));
            } else {
                return new LyricsBaseDto(serverResponse, new LyricsDto(trackInfoForLyricsDto.getTitle(),
                        trackInfoForLyricsDto.getArtists(),
                        RESPONSE_NULL_STATUS));
            }

        } catch(IOException | MusiXmatchServerResponseException e) {
            LOGGER.error(e.getMessage(), e);
            return new LyricsBaseDto(404 ,new LyricsDto(trackInfoForLyricsDto.getTitle(),
                                                                     trackInfoForLyricsDto.getArtists(),
                                                                  RESPONSE_NULL_STATUS));
        }
    }
}
