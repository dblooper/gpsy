package com.gpsy.externalApis.auddApi.client;

import com.gpsy.domain.audd.LyricsBaseDto;
import com.gpsy.domain.audd.TrackInfoForLyricsDto;
import com.gpsy.externalApis.auddApi.config.AuddConfig;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

    public LyricsBaseDto fetchLyrics(final TrackInfoForLyricsDto trackInfoForLyricsDto) {

        URI uri = UriComponentsBuilder.fromHttpUrl(auddConfig.getAuddApiEndpointRoot() + "/findLyrics/")
                .queryParam("q", trackInfoForLyricsDto.getTitle() + " " + trackInfoForLyricsDto.getAuthors())
//                .queryParam("q",trackInfoForLyricsDto.getTitle())
                .build().encode().toUri();
        System.out.println(uri);
//        try {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<LyricsBaseDto> res = restTemplate.exchange(uri, HttpMethod.GET, entity, LyricsBaseDto.class);
        LyricsBaseDto responseValue = Optional.ofNullable(res.getBody()).orElse(new LyricsBaseDto(RESPONSE_NULL_STATUS));

        if(responseValue.getStatus().equals("error")) {
            responseValue.setStatus("Dayly limit exceeded. Visit us tomorrow!");
            responseValue.setLyrics(new ArrayList<>());
        } else if(responseValue.getStatus().equals(RESPONSE_NULL_STATUS)) {
            responseValue.setStatus("Null object received. Server doesn't work properly");
            responseValue.setLyrics(new ArrayList<>());
        }

//            LyricsBaseDto[] lyrics = restTemplate.getForObject(uri, LyricsBaseDto[].class);
//            return Optional.ofNullable(lyrics).map(Arrays::asList).orElse(new ArrayList<>());
//        }catch (RestClientException e) {
//            System.out.println(e.getMessage());
//            return new ArrayList<>();
//        }
        return responseValue;
    }

}
