package com.pbe;

import com.pbe.model.Artist;
import com.pbe.model.Datasource;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Create a datasource object
        Datasource datasource = new Datasource();
        if(!datasource.open()) {
            System.out.println("Can't open datasource");
            return;
        }

        // Create list with artists via datasource queryArtists() method
        // And pass a default sorting direction
        List<Artist> artists = datasource.queryArtists(Datasource.ORDER_BY_ASC);

        // Check if there are any artists
        if(artists == null) {
            System.out.println("No artists!");
        } else {
            // Loop through all elements
            for (Artist artist : artists) {
                System.out.println("ID = " + artist.getId() + ", Name = " + artist.getName());
            }
        }

        List<String> albumsForArtist =
                datasource.queryAlbumsForArtist("Iron Maiden", Datasource.ORDER_BY_ASC);

        for(String album : albumsForArtist) {
            System.out.println(album);
        }

        datasource.close();
    }
}