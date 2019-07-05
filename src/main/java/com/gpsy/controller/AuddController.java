package com.gpsy.controller;

import com.gpsy.domain.audd.LyricsDto;
import com.gpsy.exceptions.LyricsServerResponseException;
import com.gpsy.mapper.audd.LyricsMapper;
import com.gpsy.service.audd.AuddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/gpsy")
public class AuddController {

    @Autowired
    private AuddService auddService;

    @Autowired
    private LyricsMapper lyricsMapper;

    @GetMapping(value = "/audd/getLyrics")
    public LyricsDto getLyricsFromApi(@RequestParam String title, @RequestParam String author) throws LyricsServerResponseException {
        return lyricsMapper.mapToLyricsDto(auddService.fetchLirycs(title, author));
    }
}
