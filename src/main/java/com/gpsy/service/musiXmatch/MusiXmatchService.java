package com.gpsy.service.musiXmatch;

import com.gpsy.domain.lyrics.dto.DbLyrics;
import com.gpsy.domain.lyrics.LyricsBaseDto;
import com.gpsy.domain.lyrics.dto.TrackInfoForLyricsDto;
import com.gpsy.exceptions.MusiXmatchServerResponseException;
import com.gpsy.externalApis.musiXmatchApi.client.MusiXmatchClient;
import com.gpsy.mapper.lyrics.LyricsMapper;
import com.gpsy.repository.audd.LyricsDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MusiXmatchService {

    @Autowired
    private MusiXmatchClient musiXmatchClient;

    @Autowired
    private LyricsDbRepository lyricsDbRepository;

    @Autowired
    private LyricsMapper lyricsMapper;

    public DbLyrics fetchLirycs(String title, String author) throws MusiXmatchServerResponseException {

        TrackInfoForLyricsDto trackInfoForLyricsDto = new TrackInfoForLyricsDto(title, author);

        List<DbLyrics> dbLyricsInDatabase = lyricsDbRepository.findAll();

        if(findLyrics(trackInfoForLyricsDto).isPresent()) {
            return findLyrics(trackInfoForLyricsDto).get();
        }

        LyricsBaseDto lyricsReceived = musiXmatchClient.fetchLyrics(trackInfoForLyricsDto);
        System.out.println(lyricsReceived);

        DbLyrics lyricsToFetch = lyricsMapper.mapToDbLyrics(lyricsReceived.getBody());
        System.out.println(lyricsToFetch);

            if(lyricsReceived.getStatusCode() == 200) {
                if(!dbLyricsInDatabase.contains(lyricsToFetch)) {
                    return lyricsDbRepository.save(lyricsToFetch);
                } else {
                    return dbLyricsInDatabase.get(dbLyricsInDatabase.indexOf(lyricsToFetch));
                }
            }

        return new DbLyrics(trackInfoForLyricsDto.getTitle(), trackInfoForLyricsDto.getAuthors(), "Not found in database . Limit exceeded for searching. Try tomorrow!");
    }

    private Optional<DbLyrics> findLyrics(TrackInfoForLyricsDto trackInfoForLyricsDto) {
        System.out.println(trackInfoForLyricsDto.getAuthors() + trackInfoForLyricsDto.getTitle());
        return Optional.ofNullable(lyricsDbRepository.findByTitleAndArtist(trackInfoForLyricsDto.getTitle(), trackInfoForLyricsDto.getAuthors()));
    }
}
