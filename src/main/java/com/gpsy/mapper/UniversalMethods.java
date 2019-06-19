package com.gpsy.mapper;

import com.wrapper.spotify.model_objects.specification.ArtistSimplified;

public class UniversalMethods {

    public static StringBuilder simplifyArtist(ArtistSimplified[] artistSimplifieds) {
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
