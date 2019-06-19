package com.gpsy.mapper;

import com.gpsy.domain.DbTrack;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;
import org.springframework.stereotype.Component;

@Component
public class TrackMapper {

    public DbTrack mapSpotifyTrackToDbTrack(Track spotifyDbTrack) {
        StringBuilder authors = new StringBuilder();
        for(ArtistSimplified author: spotifyDbTrack.getArtists()) {
            authors.append(author.getName());
            authors.append(", ");
        }

        return new DbTrack(spotifyDbTrack.getId(), spotifyDbTrack.getName(), authors.toString(), spotifyDbTrack.getPopularity());
    }


}
