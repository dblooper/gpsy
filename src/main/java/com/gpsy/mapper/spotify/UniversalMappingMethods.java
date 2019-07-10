package com.gpsy.mapper.spotify;

import com.wrapper.spotify.model_objects.specification.ArtistSimplified;

public class UniversalMappingMethods {

    public static String simplifyArtist(ArtistSimplified[] artistSimplifieds) {
        StringBuilder authors = new StringBuilder();
        for(int i = 0; i < artistSimplifieds.length; i++) {
            if(artistSimplifieds.length == 1 || i == artistSimplifieds.length - 1) {
                authors.append(artistSimplifieds[i].getName());
            }else {
                authors.append(artistSimplifieds[i].getName());
                authors.append(", ");
            }
        }
        return authors.toString();
    }
}
