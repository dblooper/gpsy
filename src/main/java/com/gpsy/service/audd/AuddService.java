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
import java.util.Optional;

@Service
public class AuddService {

    @Autowired
    private AuddClient auddClient;

    @Autowired
    private LyricsDbRepository lyricsDbRepository;

    @Autowired
    private LyricsMapper lyricsMapper;

    public DbLyrics fetchLirycs(String title, String author) {

        TrackInfoForLyricsDto trackInfoForLyricsDto = new TrackInfoForLyricsDto(title, author);

        List<DbLyrics> dbLyricsInDatabase = lyricsDbRepository.findAll();

        if(findLyrics(trackInfoForLyricsDto).isPresent()) {
            return findLyrics(trackInfoForLyricsDto).get();
        }

        LyricsBaseDto lyricsReceived = auddClient.fetchLyrics(trackInfoForLyricsDto);
        System.out.println(lyricsReceived);
        if(lyricsReceived.getLyrics().size() > 0) {
            DbLyrics lyricsToFetch = lyricsMapper.mapToDbLyrics(lyricsReceived.getLyrics().get(0));
            System.out.println(lyricsToFetch);

            if(lyricsReceived.getStatus().equals("success")) {
                if(!dbLyricsInDatabase.contains(lyricsToFetch)) {
                    return lyricsDbRepository.save(lyricsToFetch);
                } else {
                    return dbLyricsInDatabase.get(dbLyricsInDatabase.indexOf(lyricsToFetch));
                }
            }
        }
        return new DbLyrics(trackInfoForLyricsDto.getTitle(), trackInfoForLyricsDto.getAuthors(), "Not found in database . Limit exceeded for searching. Try tomorrow!");
    }

    private Optional<DbLyrics> findLyrics(TrackInfoForLyricsDto trackInfoForLyricsDto) {
        System.out.println(trackInfoForLyricsDto.getAuthors() + trackInfoForLyricsDto.getTitle());
        return Optional.ofNullable(lyricsDbRepository.findByTitleAndArtist(trackInfoForLyricsDto.getTitle(), trackInfoForLyricsDto.getAuthors()));
    }
}
