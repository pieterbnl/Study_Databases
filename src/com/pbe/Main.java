package com.pbe;

import com.pbe.model.Artist;
import com.pbe.model.Datasource;
import com.pbe.model.SongArtist;

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
        if(artists.isEmpty()) {
            System.out.println("No artists!");
        } else {
            // Loop through all elements
            for (Artist artist : artists) {
                System.out.println("ID = " + artist.getId() + ", Name = " + artist.getName());
            }
        }

        // Check albums by artist name
        List<String> albumsForArtist =
                datasource.queryAlbumsForArtist("Pink Floyd", Datasource.ORDER_BY_ASC);

        for(String album : albumsForArtist) {
            System.out.println(album);
        }

        // Check which artist and album belong to a specific song
        List<SongArtist> songArtists =
                datasource.queryArtistForSong("She's On Fire", Datasource.ORDER_BY_ASC);

        if(songArtists.isEmpty()) {
            System.out.println("Couldn't find the artist for the song");
            return;
        }

        for (SongArtist artist : songArtists) {
            System.out.println(" Artist name = " + artist.getArtistName() +
                    "\n Album name = " + artist.getAlbumName() +
                    "\n Track = " + artist.getTrack());
        }

        //datasource.querySongsMetadata();

        int count = datasource.getCount(Datasource.TABLE_SONGS);
        System.out.println("Number of songs is: " + count);

        // Working with a view
        datasource.createViewForSongArtists();

        songArtists = datasource.querySongInfoView("Go Your Own Way");
        if(songArtists.isEmpty()) {
            System.out.println("Couldn't find the artist for the song");
            return;
        }

        for(SongArtist artist : songArtists) {
            System.out.println("FROM VIEW - Artist name = " + artist.getArtistName() +
                    " Album name = " + artist.getAlbumName() +
                    " Track number = " + artist.getTrack());
        }

        // Closing datasource
        datasource.close();
    }
}