package com.gpsy.controller;

import com.gpsy.domain.audd.LyricsBaseDto;
import com.gpsy.domain.audd.LyricsDto;
import com.gpsy.domain.audd.TrackInfoForLyricsDto;
import com.gpsy.exceptions.LyricsNotFoundException;
import com.gpsy.externalApis.auddApi.client.AuddClient;
import com.gpsy.mapper.audd.LyricsMapper;
import com.gpsy.service.audd.AuddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audd")
public class AuddController {

    @Autowired
    private AuddClient auddClient;

    @Autowired
    private AuddService auddService;

    @Autowired
    private LyricsMapper lyricsMapper;

    @GetMapping(value = "/v1/getLyrics")
    public LyricsDto getLyricsFromApi() throws LyricsNotFoundException {
        return lyricsMapper.mapToLyricsDto(auddService.fetchLirycs(new TrackInfoForLyricsDto("roling", "adele")));
    }
}
