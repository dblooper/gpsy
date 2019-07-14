package com.gpsy.scheduler;

import com.gpsy.externalApis.spotify.client.SpotifyAuthorizator;
import com.gpsy.service.dbApiServices.spotify.SaveSpotifyDataToDbService;
import com.gpsy.service.dbApiServices.spotify.SpotifyHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduledTasks {

    @Autowired
    private SpotifyHandleService spotifyHandleService;

    @Autowired
    private SaveSpotifyDataToDbService saveSpotifyDataToDbService;

//    @Scheduled(fixedRate = 60000)
    //    @Scheduled(cron = MailMessageConfiguration.SCHEDULING_CRON)//every monday at 7:00
    private void sendNewPlaylistToSpotifyWithEmail() {
        spotifyHandleService.saveRecommendedPlaylistToSpotify();
    }

    @Scheduled(fixedRate = 9000000) //save to db recently played tracks every 2,5 h(1 track 3 min, max tracks 50 -> 150min)
    private void saveToDbRecentTracks() {
        saveSpotifyDataToDbService.saveRecentPlayedTracks();
    }
}
