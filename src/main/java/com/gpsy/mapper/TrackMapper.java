package com.gpsy.mapper;

import com.gpsy.domain.DbPopularTrack;
import com.gpsy.domain.DbRecentPlayedTrack;
import com.gpsy.domain.DbRecommendedTrack;
import com.gpsy.domain.dto.RecentPlayedTrackDto;
import com.wrapper.spotify.model_objects.specification.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrackMapper {

    public DbPopularTrack mapSpotifyTrackToDbPopularTrack(Track spotifyDbTrack) {
        ArtistSimplified[] artists = spotifyDbTrack.getArtists();
        return new DbPopularTrack(spotifyDbTrack.getId(), spotifyDbTrack.getName(), UniversalMethods.simplifyArtist(artists).toString(), spotifyDbTrack.getPopularity());
    }

    public DbRecentPlayedTrack mapSpotifyTrackToDbRecentPlayedTrack(PlayHistory playHistory) {
        TrackSimplified recentTrack = playHistory.getTrack();
        ArtistSimplified[] artistSimplifieds = recentTrack.getArtists();

        return new DbRecentPlayedTrack(recentTrack.getId(), recentTrack.getName(), UniversalMethods.simplifyArtist(artistSimplifieds).toString() , playHistory.getPlayedAt());
    }

    public List<RecentPlayedTrackDto> mapToRecentPlayedTrackDtos(List<DbRecentPlayedTrack> dbRecentPlayedTracks) {

        return dbRecentPlayedTracks.stream()
                .map(track -> new RecentPlayedTrackDto(track.getTrackId(), track.getTitle(), track.getAuthors(), track.getPlayDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()))
                .collect(Collectors.toList());
    }

//    public DbRecommendedTrack mapSpotifyRecommendedTrackToDbRecommendedTrack() {
//
//    }


}
