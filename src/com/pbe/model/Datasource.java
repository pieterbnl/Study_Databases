package com.pbe.model;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    // Set constants for the database
    public static final String DB_NAME = "music.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Coding\\Java Projects\\Java Masterclass\\Databases\\" + DB_NAME;

    // Define album table and it's columns
    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";

    // Add album table column index constants
    // It's more efficient to use the column index, because the getter methods
    // will know exactly where to go to get the value in the result set.
    // When using the column name, the method has to match the column name
    // against the column names in the result sets.
    // Looping through a large set, this can make a difference.
    public static final int INDEX_ALBUM_ID = 1;
    public static final int INDEX_ALBUM_NAME = 2;
    public static final int INDEX_ALBUM_ARTIST = 3;

    // Define artists table and it's columns
    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "_id";
    public static final String COLUMN_ARTIST_NAME = "name";

    // Add artist table column index constants
    public static final int INDEX_ARTIST_ID = 1;
    public static final int INDEX_ARTIST_NAME = 2;

    // Define songs table and it's columns
    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_ID = "_id";
    public static final String COLUMN_SONG_TRACK = "track";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ALBUM = "album";

    // Add song table column index constants
    public static final int INDEX_SONG_ID = 1;
    public static final int INDEX_SONG_TRACK = 2;
    public static final int INDEX_SONG_TITLE = 3;
    public static final int INDEX_SONG_ALBUM = 4;

    // Set order constants
    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    // Set a Connection declaration
    // Connection is a statement object for sending SQL statements to the database
    private Connection conn;

    // ******
    // Open database connection
    // ******
    public boolean open() {
        try {
            // Establish a connection with the database via DriverManager's getConnection()
            conn = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ******
    // Close database connection
    // ******
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

    // ******
    // Query artists table
    // ******
    public List<Artist> queryArtists(int sortOrder) {

        // Build a query string, based on a passed sorting order (sortOrder)
        StringBuilder sb = new StringBuilder("SELECT * FROM ");

        sb.append(TABLE_ARTISTS);
        if(sortOrder != ORDER_BY_NONE) { // ignore all, if no sorting order is set
            sb.append(" ORDER BY ");
            sb.append(COLUMN_ARTIST_NAME);
            sb.append(" COLLATE NOCASE "); // to do case-insensitive comparisons
            if(sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        // Create a 'statement' for sending SQL statements to the database
        // Execute query, returning all artist records with all column values
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString());
        ) {
            // Create list of artists objects and loop through
            // For each artist, create an artist object
            // Using getter methods to get the values from the record and send them to the artist instance
            // Finally, add artist instance to the list
            List<Artist> artists = new ArrayList<>();
            while (results.next()) {
                Artist artist = new Artist();
                artist.setId(results.getInt(INDEX_ARTIST_ID)); // COLUMN_ARTIST_ID
                artist.setName(results.getString(INDEX_ARTIST_NAME)); // COLUMN_ARTIST_NAME
                artists.add(artist);
            }

            // Return list to caller
            return artists;

        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    public List<String> queryAlbumsForArtist(String artistName, int sortOrder) {

        // Build query string
        // SELECT albums.name FROM albums INNER JOIN artists ON albums.artist = artists._id
        // WHERE artists.name = artistName ORDER BY sortOrder
        // COLLATE NOCASE ASC;
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(TABLE_ALBUMS);
        sb.append('.');
        sb.append(COLUMN_ARTIST_NAME);
        sb.append(" FROM ");
        sb.append(TABLE_ALBUMS);
        sb.append(" INNER JOIN ");
        sb.append(TABLE_ARTISTS);
        sb.append(" ON ");
        sb.append(TABLE_ALBUMS);
        sb.append('.');
        sb.append(COLUMN_ALBUM_ARTIST);
        sb.append(" = ");
        sb.append(TABLE_ARTISTS);
        sb.append('.');
        sb.append(COLUMN_ARTIST_ID);
        sb.append(" WHERE ");
        sb.append(TABLE_ARTISTS);
        sb.append('.');
        sb.append(COLUMN_ARTIST_NAME);
        sb.append(" = \"" ); // using \ to escape "
        sb.append(artistName);
        sb.append("\"" );

        if(sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(TABLE_ALBUMS);
            sb.append('.');
            sb.append(COLUMN_ALBUM_NAME);
            sb.append(" COLLATE NOCASE ");
            if(sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        // Check generated SQL string
        // System.out.println("SQL statement = " + sb.toString());

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<String> albums = new ArrayList<>();
            while(results.next()) {
                albums.add(results.getString(1));
            }
            return albums;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }


    }
}