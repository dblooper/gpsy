package com.gpsy.mapper.spotify;

import com.gpsy.domain.spotify.*;
import com.gpsy.domain.spotify.PlaylistTrack;
import com.gpsy.domain.spotify.dto.*;
import com.wrapper.spotify.model_objects.specification.*;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrackMapper {

    public PopularTrack mapSpotifyTrackToDbPopularTrack(Track spotifyDbTrack) {
        return new PopularTrack.Buiilder()
                                .stringId(spotifyDbTrack.getId())
                                .title(spotifyDbTrack.getName())
                                .artists(UniversalMappingMethods.simplifyArtist(spotifyDbTrack.getArtists()))
                                .popularity(spotifyDbTrack.getPopularity())
                                .build();
    }

    public RecentPlayedTrack mapSpotifyTrackToDbRecentPlayedTrack(PlayHistory playHistory) {
        TrackSimplified recentTrack = playHistory.getTrack();
        return new RecentPlayedTrack.Builder()
                                      .stringId(recentTrack.getId())
                                      .title(recentTrack.getName())
                                      .artists(UniversalMappingMethods.simplifyArtist(recentTrack.getArtists()))
                                      .playDate(playHistory.getPlayedAt())
                                      .build();
    }

    public List<RecentPlayedTrackDto> mapToRecentPlayedTrackDtoList(List<RecentPlayedTrack> recentPlayedTracks) {
        return recentPlayedTracks.stream()
                .map(track -> new RecentPlayedTrackDto.Builder()
                                                    .stringId(track.getTrackStringId())
                                                    .artists(track.getArtists())
                                                    .title(track.getTitle())
                                                    .playDate(track.getPlayDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                                                    .build())
                .collect(Collectors.toList());
    }

    public List<MostFrequentTrackDto> mapToPopularTrackDtoList(List<MostFrequentTrack> dbPopularTracks) {
        return dbPopularTracks.stream()
                .map(track -> new MostFrequentTrackDto.Builder()
                                                        .stringId(track.getTrackStringId())
                                                        .artists(track.getArtists())
                                                        .title(track.getTitle())
                                                        .popularity(track.getPopularity())
                                                        .build())
                .collect(Collectors.toList());
    }

    public List<RecommendedTrackDto> mapToRecommendedTrackDtoList(List<RecommendedTrack> recommendedTracks) {
        return recommendedTracks.stream()
                .map(track -> new RecommendedTrackDto.Builder()
                                                        .stringId(track.getTrackStringId())
                                                        .title(track.getTitle())
                                                        .aritsts(track.getAuthors())
                                                        .sample(track.getSample())
                                                        .buidl())
                .collect(Collectors.toList());
    }

    public List<PlaylistTrack> mapToPlaylistTrack(List<PlaylistTrackDto> playlistTrackDtoList) {
        return playlistTrackDtoList.stream()
                .map(playlistTrackDto -> new PlaylistTrack.Builder()
                                                            .stringId(playlistTrackDto.getTrackStringId())
                                                            .artists(playlistTrackDto.getArtists())
                                                            .title(playlistTrackDto.getTitle())
                                                            .build()
                )
                .collect(Collectors.toList());
    }

    public List<RecommendedTrackForPlaylistDto> mapToRecommendedTrackForPlaylistDto(List<RecommendedPlaylistTrack> recommendedPlaylistTrack) {
        return recommendedPlaylistTrack.stream()
                .map(track -> new RecommendedTrackForPlaylistDto.Builder()
                                                                .stringId(track.getTrackStringId())
                                                                .title(track.getTitle())
                                                                .artists(track.getArtists())
                                                                .sample(track.getSample())
                                                                .build())
                .collect(Collectors.toList());
    }

    public List<PopularTrackDto> mapPopularTrackToPopularTrackDtoList(List<PopularTrack> popularTracks) {
        return popularTracks.stream()
                .map(track -> new PopularTrackDto.Builder()
                                                    .stirngId(track.getTrackStringId())
                                                    .artists(track.getArtists())
                                                    .title(track.getTitle())
                                                    .popularity(track.getPopularity())
                                                    .build())
                .collect(Collectors.toList());
    }

    public List<SearchTrackDto> mapToSearchTrackDto(List<Track> tracks) {
        return tracks.stream()
                .map(track -> new SearchTrackDto.Builder()
                                    .stringId(track.getId())
                                    .aritsts(UniversalMappingMethods.simplifyArtist(track.getArtists()))
                                    .title(track.getName())
                                    .sample(track.getPreviewUrl())
                                    .build())
                .collect(Collectors.toList());
    }

    public List<RecommendedPlaylistTrack> mapToRecommendedPlaylistTracks(List<TrackSimplified> simplifiedTracks, int numberOfTracks) {
        return simplifiedTracks.stream()
                .limit(numberOfTracks)
                .map(track -> new RecommendedPlaylistTrack.Builder()
                                                            .stringId(track.getId())
                                                            .title(track.getName())
                                                            .artists(UniversalMappingMethods.simplifyArtist(track.getArtists()))
                                                            .sample(track.getPreviewUrl())
                                                            .build())
                .collect(Collectors.toList());
    }

    public List<PlaylistTrack> mapRecommendedPlaylistTracksToPlaylistTracks(List<RecommendedPlaylistTrack> recommendedPlaylistTracks) {
        return recommendedPlaylistTracks.stream()
                .map(track -> new PlaylistTrack.Builder()
                                                .title(track.getTitle())
                                                .stringId(track.getTrackStringId())
                                                .artists(track.getArtists())
                                                .build())
                .collect(Collectors.toList());
    }

    public List<RecommendedTrack> mapToRecommendedTracks(List<TrackSimplified> recommendedTracks) {
        return recommendedTracks.stream()
                .map(track -> new RecommendedTrack.Builder()
                .stringId(track.getId())
                                .artists(UniversalMappingMethods.simplifyArtist(track.getArtists()))
                                .title(track.getName())
                                .sample(track.getPreviewUrl())
                                .build())
                .collect(Collectors.toList());
    }

    public UserPlaylist mapRecommendedPlaylistToUserPlaylist(RecommendedPlaylist recommendedPlaylist) {
        return new UserPlaylist.Builder()
                                .stringId(recommendedPlaylist.getPlaylistStringId())
                                .name(recommendedPlaylist.getName())
                                .tracks(mapRecommendedPlaylistTracksToPlaylistTracks(recommendedPlaylist.getRecommendedPlaylistTracks()))
                                .build();
    }
}
