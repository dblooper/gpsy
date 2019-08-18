package com.gpsy.scheduler;

import com.gpsy.config.ConstantsForConfiguration;
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

    @Scheduled(cron = ConstantsForConfiguration.SCHEDULING_CRON)
    private void sendNewPlaylistToSpotifyWithEmail() {
        spotifyHandleService.saveRecommendedPlaylistToSpotify();
    }

    @Scheduled(fixedRate = ConstantsForConfiguration.SCHEDULED_FETCHING_DATA_FROM_SPOTIFY_API)
    private void saveToDbRecentTracks() {
        saveSpotifyDataToDbService.saveRecentPlayedTracks();
    }
}
