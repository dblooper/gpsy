package com.gpsy.service.dbApiServices.spotify;

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
import com.gpsy.service.mailService.EmailService;
import com.gpsy.service.mailService.Mail;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
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

    public List<RecommendedTrack> returnRecommendedTracks(int qty) {
        List<TrackSimplified> tracksSimplified = spotifyClient.getRecommendedTracks(qty);
        return trackMapper.mapToRecommendedTracks(tracksSimplified);
    }

    public void updatePlaylistTracks(UserPlaylist userPlaylist) {
        spotifyClient.updatePlaylistTracks(userPlaylist);
    }

    public void deletePlaylistTrack(UserPlaylist userPlaylist) {
        spotifyClient.deletePlaylistTrack(userPlaylist);
    }

    public UserPlaylist updatePlaylistName(UserPlaylist newUserPlaylist) {
        if(!newUserPlaylist.getPlaylistStringId().equals(InitialLimitValues.RECOMMENDED_PLAYLIST_ID)) {
            return spotifyClient.updatePlaylistDetails(newUserPlaylist);
        }
        return new UserPlaylist.Builder().tracks(new ArrayList<>()).name("Not changed, not allowed id").stringId(newUserPlaylist.getPlaylistStringId()).build();
    }

    public void createPlaylist(UserPlaylist userPlaylist) {
        spotifyClient.createPlaylist(userPlaylist);
        saveSpotifyDataToDbService.saveUserPlaylists();
    }

    public List<SearchTrackDto> searchForTracks(String searchedItem) {
        return trackMapper.mapToSearchTrackDto(spotifyClient.searchForTrack(searchedItem));
    }

    public void saveRecommendedPlaylistToSpotify() {
        RecommendedPlaylist recommendedPlaylistToSave = fetchDataFromDbService.fetchRecommendedPlaylist();
        List<PlaylistTrack> tracksToDelete = spotifyPlaylistMapper.mapToPlaylistTracks(recommendedPlaylistToSave.getPlaylistStringId());
        spotifyClient.deletePlaylistTrack(new UserPlaylist.Builder()
                                                            .name(recommendedPlaylistToSave.getName())
                                                            .stringId(recommendedPlaylistToSave.getPlaylistStringId())
                                                            .tracks(tracksToDelete)
                                                            .build());
        spotifyClient.updatePlaylistTracks(dbPlaylistMapper.mapRecommendedPlaylistToUserPlaylist(recommendedPlaylistToSave));
        emailService.send(new Mail(MailMessageConfiguration.EMAIL_ADDRESS_TO_SEND,
                                    MailMessageConfiguration.SUBJECT,
                                        "A new set of tracks based on your frequent tracks has been saved on your Spotify playlist " + InitialLimitValues.RECOMMENDED_PLAYLIST_NAME + "!\n" +
                                                "Total quantity of tracks: " + recommendedPlaylistToSave.getRecommendedPlaylistTracks().size()));
    }
}
