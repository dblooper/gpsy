package com.gpsy.service.audd;

import com.gpsy.domain.audd.DbLyrics;
import com.gpsy.domain.audd.LyricsBaseDto;
import com.gpsy.domain.audd.TrackInfoForLyricsDto;
import com.gpsy.exceptions.LyricsNotFoundException;
import com.gpsy.externalApis.auddApi.client.AuddClient;
import com.gpsy.mapper.audd.LyricsMapper;
import com.gpsy.repository.audd.LyricsDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuddService {

    @Autowired
    private AuddClient auddClient;

    @Autowired
    private LyricsDbRepository lyricsDbRepository;

    @Autowired
    private LyricsMapper lyricsMapper;

    public DbLyrics fetchLirycs(TrackInfoForLyricsDto trackInfoForLyricsDto) throws LyricsNotFoundException {

        List<DbLyrics> dbLyricsInDatabase = lyricsDbRepository.findAll();
        LyricsBaseDto lyricsReceived = auddClient.fetchLyrics(trackInfoForLyricsDto);

        if(lyricsReceived.getLyrics().size() > 0) {
            DbLyrics lyricsToFetch = lyricsMapper.mapToDbLyrics(lyricsReceived.getLyrics().get(0));

            if(findLyrics(trackInfoForLyricsDto) != null) {
                return findLyrics(trackInfoForLyricsDto);
            }

            if(lyricsReceived.getStatus().equals("success")) {
                if(!dbLyricsInDatabase.contains(lyricsToFetch)) {
                    return lyricsDbRepository.save(lyricsToFetch);
                } else {
                    return dbLyricsInDatabase.get(dbLyricsInDatabase.indexOf(lyricsToFetch));
                }
            }
        }
        return new DbLyrics(trackInfoForLyricsDto.getTitle(), trackInfoForLyricsDto.getAuthors(), "Not found. Limit exceeded. Try tomorrow!");
    }

    private DbLyrics findLyrics(TrackInfoForLyricsDto trackInfoForLyricsDto) throws LyricsNotFoundException {

        return lyricsDbRepository.findByTitleAndArtist(trackInfoForLyricsDto.getTitle(), trackInfoForLyricsDto.getAuthors()).orElseThrow(LyricsNotFoundException::new);
    }
}
