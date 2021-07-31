package com.pbe;

import com.pbe.model.Artist;
import com.pbe.model.Datasource;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Datasource datasource = new Datasource();
        if(!datasource.open()) {
            System.out.println("Can't open datasource");
            return;
        }

        // Create list with artists via datasource queryArtists() method
        List<Artist> artists = datasource.queryArtists();

        // Check if there are any artists
        if(artists == null) {
            System.out.println("No artists!");
        } else {
            // Loop through all elements
            for (Artist artist : artists) {
                System.out.println("ID = " + artist.getId() + ", Name = " + artist.getName());
            }
        }
        datasource.close();
    }
}