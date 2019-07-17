package com.gpsy.service.dbApiServices.musiXmatch;

import com.gpsy.domain.lyrics.DbLyrics;
import com.gpsy.domain.lyrics.dto.LyricsBaseDto;
import com.gpsy.domain.lyrics.dto.TrackInfoForLyricsDto;
import com.gpsy.externalApis.musiXmatchApi.client.MusiXmatchClient;
import com.gpsy.mapper.lyrics.LyricsMapper;
import com.gpsy.repository.lyrics.LyricsDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusiXmatchService {

    @Autowired
    private MusiXmatchClient musiXmatchClient;

    @Autowired
    private LyricsDbRepository lyricsDbRepository;

    @Autowired
    private LyricsMapper lyricsMapper;

    public DbLyrics fetchLirycs(String title, String author) {

        TrackInfoForLyricsDto trackInfoForLyricsDto = new TrackInfoForLyricsDto(title, author);
        return lyricsDbRepository.findByTitleAndArtist(trackInfoForLyricsDto.getTitle(), trackInfoForLyricsDto.getArtists())
                .orElseGet(() -> findLyricsByApi(trackInfoForLyricsDto));
    }

    private DbLyrics findLyricsByApi(TrackInfoForLyricsDto trackInfoForLyricsDto) {
            LyricsBaseDto lyricsBaseReceived = musiXmatchClient.fetchLyrics(trackInfoForLyricsDto);
            DbLyrics lyricsToFetch = lyricsMapper.mapToDbLyrics(lyricsBaseReceived.getBody());

            if(lyricsBaseReceived.getStatusCode() == 200) {
                return lyricsDbRepository.save(lyricsToFetch);
            }

            return lyricsToFetch;


    }
}
