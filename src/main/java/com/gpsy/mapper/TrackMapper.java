package com.gpsy.mapper;

import com.gpsy.domain.DbPopularTrack;
import com.gpsy.domain.DbRecentPlayedTrack;
import com.gpsy.domain.RecentPlayedTrackDto;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.PlayHistory;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrackMapper {

    public DbPopularTrack mapSpotifyTrackToDbPopularTrack(Track spotifyDbTrack) {
        ArtistSimplified[] artists = spotifyDbTrack.getArtists();
        return new DbPopularTrack(spotifyDbTrack.getId(), spotifyDbTrack.getName(), simplifyArtist(artists).toString(), spotifyDbTrack.getPopularity());
    }

    public DbRecentPlayedTrack mapSpotifyTrackToDbRecentPlayedTrack(PlayHistory playHistory) {
        TrackSimplified recentTrack = playHistory.getTrack();
        ArtistSimplified[] artistSimplifieds = recentTrack.getArtists();

        return new DbRecentPlayedTrack(recentTrack.getId(), recentTrack.getName(), simplifyArtist(artistSimplifieds).toString() , playHistory.getPlayedAt());
    }

    public List<RecentPlayedTrackDto> mapDbRecentPlayedTrackToDto(List<DbRecentPlayedTrack> dbRecentPlayedTrack) {
        return dbRecentPlayedTrack.stream()
        .map(dbRecentPlayedTrack1 -> new RecentPlayedTrackDto(dbRecentPlayedTrack1.getTrackId(), dbRecentPlayedTrack1.getTitle(), dbRecentPlayedTrack1.getAuthors(), dbRecentPlayedTrack1.getPlayDate().toString()))
        .collect(Collectors.toList());
    }


    private StringBuilder simplifyArtist(ArtistSimplified[] artistSimplifieds) {
        StringBuilder authors = new StringBuilder();
        for(int i = 0; i < artistSimplifieds.length; i++) {
            if(artistSimplifieds.length == 1 || i == artistSimplifieds.length - 1) {
                authors.append(artistSimplifieds[i].getName());
            }else {
                authors.append(artistSimplifieds[i].getName());
                authors.append(", ");
            }
        }
        return authors;
    }
}
