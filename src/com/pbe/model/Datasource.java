package com.pbe.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Datasource {

    // Set constants for the database
    public static final String DB_NAME = "music.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Coding\\Java Projects\\Java Masterclass\\Databases\\" + DB_NAME;

    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";

    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "_id";
    public static final String COLUMN_ARTIST_NAME = "name";

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_TRACK = "track";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ALBUM = "album";

    private Connection conn;

    // Method to open database connection
    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Method to close database connection
    public void close() {
        try {
            if(conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Artist> queryArtists() {

        // Create a 'statement' for sending SQL statements to the database
        // Execute query, returning all artist records with all column values
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_ARTISTS);
        ) {
            // Create list of artists objects and loop through
            // For each artist, create an artist object
            // Using getter methods to get the values from the record and send them to the artist instance
            // Finally, add artist instance to the list
            List<Artist> artists = new ArrayList<>();
            while (results.next()) {
                Artist artist = new Artist();
                artist.setId(results.getInt(COLUMN_ARTIST_ID));
                artist.setName(results.getString(COLUMN_ARTIST_NAME));
                artists.add(artist);
            }

            // Return list to caller
            return artists;

        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            e.printStackTrace();
            return null;
        }

//        //  not needed with try-with-resources - as it will close the connections automatically
//        finally {
//            // Two closures in finally block.
//            // Potentially throwing 2 exceptions, thus 2 try-catch blocks.
//            try {
//                if(results != null) {
//                    results.close();
//                }
//            } catch (SQLException e) {
//                System.out.println("Error closing ResultSet" + e.getMessage());
//                e.printStackTrace();
//            }
//            try {
//                if(statement != null) {
//                    statement.close();
//                }
//            } catch (SQLException e) {
//                System.out.println("Error closing statement" + e.getMessage());
//                e.printStackTrace();
//            }
//        }
    }
}