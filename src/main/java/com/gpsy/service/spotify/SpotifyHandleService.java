package com.gpsy.service.spotify;

import com.gpsy.config.InitialLimitValues;
import com.gpsy.config.MailMessageConfiguration;
import com.gpsy.domain.spotify.PlaylistTrack;
import com.gpsy.domain.spotify.RecommendedPlaylist;
import com.gpsy.domain.spotify.RecommendedTrack;
import com.gpsy.domain.spotify.UserPlaylist;
import com.gpsy.domain.spotify.dto.SearchTrackDto;
import com.gpsy.externalApis.spotify.client.SpotifyClient;
import com.gpsy.mapper.spotify.DbPlaylistMapper;
import com.gpsy.mapper.spotify.SpotifyPlaylistMapper;
import com.gpsy.mapper.spotify.TrackMapper;
import com.gpsy.mapper.spotify.UniversalMappingMethods;
import com.gpsy.service.mailService.EmailService;
import com.gpsy.service.mailService.Mail;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class SpotifyHandleService {

    @Autowired
    private SpotifyClient spotifyClient;

    @Autowired
    private SaveSpotifyDataToDbService saveSpotifyDataToDbService;

    @Autowired
    private FetchDataFromDbService fetchDataFromDbService;

    @Autowired
    private TrackMapper trackMapper;

    @Autowired
    private DbPlaylistMapper dbPlaylistMapper;

    @Autowired
    private SpotifyPlaylistMapper spotifyPlaylistMapper;

    @Autowired
    private EmailService emailService;

    public List<RecommendedTrack> returnRecommendedTracks() {
        List<RecommendedTrack> recommendedTracks = new ArrayList<>();
        List<TrackSimplified> tracksSimplified = spotifyClient.getRecommendedTracks();
        recommendedTracks = tracksSimplified.stream()
                .limit(InitialLimitValues.LIMIT_RECOMMENDED_TRACKS_ON_BOARD)
                .map(track -> new RecommendedTrack(track.getId(), track.getName(), UniversalMappingMethods.simplifyArtist(track.getArtists()).toString(), track.getPreviewUrl()))
                .collect(Collectors.toList());
        return recommendedTracks;
    }

    public UserPlaylist updatePlaylistTracks(UserPlaylist userPlaylist) {
        spotifyClient.updatePlaylistTracks(userPlaylist);
        return userPlaylist;
    }

    public UserPlaylist deletePlaylistTrack(UserPlaylist userPlaylist) {
        spotifyClient.deletePlaylistTrack(userPlaylist);
        return userPlaylist;
    }

    public UserPlaylist updatePlaylistName(UserPlaylist newUserPlaylist) {
        if(!newUserPlaylist.getPlaylistStringId().equals("2ptqwasYqv1677gL4OEkIL")) {
            spotifyClient.updatePlaylistDetails(newUserPlaylist);
        }
        return newUserPlaylist;
    }

    public UserPlaylist createPlaylist(UserPlaylist userPlaylist) {
        spotifyClient.createPlaylist(userPlaylist);
        return saveSpotifyDataToDbService.saveUserPlaylists().get(0);
    }

    public List<SearchTrackDto> searchForTracks(String searchedItem) {
        return trackMapper.mapToSearchTrackDto(spotifyClient.searchForTrack(searchedItem));
    }

//    @Scheduled(cron = MailMessageConfiguration.SCHEDULING_CRON)//every monday at 7:00
//    @Scheduled(fixedRate = 60000)
//    public void saveRecommendedPlaylistToSpotify() {
//        RecommendedPlaylist recommendedPlaylistToSave = fetchDataFromDbService.fetchRecommendedPlaylist();
//        List<PlaylistTrack> playlistToDeleteTracks = spotifyPlaylistMapper.mapToPlaylistTracks(recommendedPlaylistToSave.getStringId());
//        spotifyClient.deletePlaylistTrack(new UserPlaylist(recommendedPlaylistToSave.getName(), recommendedPlaylistToSave.getStringId(), playlistToDeleteTracks));
//        spotifyClient.updatePlaylistTracks(dbPlaylistMapper.mapRecommendedPlaylistToUserPlaylist(recommendedPlaylistToSave));
//
//        emailService.send(new Mail(MailMessageConfiguration.EMAIL_ADDRESS_TO_SEND,
//                                    MailMessageConfiguration.SUBJECT,
//                                        "A new set of tracks based on your frequent tracks has been saved!\n" +
//                                                "Total quantity of tracks: " + recommendedPlaylistToSave.getRecommendedPlaylistTracks().size()));
//    }
}
