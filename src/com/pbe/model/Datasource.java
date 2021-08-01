package com.pbe.model;

import javax.xml.transform.Result;
import java.net.PortUnreachableException;
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

    // Setting constants for queries (in 2 part, of which the last the sorting part)
    // Query albums by artist
    public static final String QUERY_ALBUMS_BY_ARTIST_START =
            "SELECT " + TABLE_ALBUMS + '.' + COLUMN_ARTIST_NAME + " FROM " + TABLE_ALBUMS +
                    " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST +
                    " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
                    " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + " = \"";

    public static final String QUERY_ALBUMS_BY_ARTIST_SORT =
            " ORDER BY " + TABLE_ALBUMS + "." + COLUMN_ARTIST_NAME + " COLLATE NOCASE ";

    // Query artist from a song
    public static final String QUERY_ARTIST_FOR_SONG_START =
            "SELECT " + TABLE_ARTISTS + '.' + COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME + ", " +
                    TABLE_SONGS + '.' + COLUMN_SONG_TRACK + " FROM " + TABLE_SONGS +
                    " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + '.' + COLUMN_SONG_ALBUM +
                    " = " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_ID +
                    " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_ARTIST +
                    " = " + TABLE_ARTISTS + '.' + COLUMN_ARTIST_ID +
                    " WHERE " + TABLE_SONGS + '.' + COLUMN_SONG_TITLE + " = \"";

    public static final String QUERY_ARTIST_FOR_SONG_SORT =
            " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", "
                    + TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";

    // Setting a constant for a view
    public static final String TABLE_ARTIST_SONG_VIEW = "artist_list";

    // Query for viewing song info (via a view)
    public static final String QUERY_VIEW_SONG_INFO = "SELECT " + COLUMN_ARTIST_NAME + ", " +
            COLUMN_SONG_ALBUM + ", " + COLUMN_SONG_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW +
            " WHERE " + COLUMN_SONG_TITLE + " = \"";

    // Query artist from a song
    public static final String CREATE_ARTIST_FOR_SONG_VIEW = "CREATE VIEW IF NOT EXISTS " +
            TABLE_ARTIST_SONG_VIEW + " AS SELECT " + TABLE_ARTISTS + '.' + COLUMN_ARTIST_NAME + ", " +
            TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME + " AS " + COLUMN_SONG_ALBUM + ", " +
            TABLE_SONGS + '.' + COLUMN_SONG_TRACK + ", " + TABLE_SONGS + '.' + COLUMN_SONG_TITLE +
            " FROM " + TABLE_SONGS +
            " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS +
            '.' + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_ID +
            " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_ARTIST +
            " = " + TABLE_ARTISTS + '.' + COLUMN_ARTIST_ID +
            " ORDER BY " +
            TABLE_ARTISTS + '.' + COLUMN_ARTIST_NAME + ", " +
            TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME + ", " +
            TABLE_SONGS + '.' + COLUMN_SONG_TRACK;

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


    // ******
    // Query albums by artist
    // ******
    public List<String> queryAlbumsForArtist(String artistName, int sortOrder) {

        // Build query string
        StringBuilder sb = new StringBuilder(QUERY_ALBUMS_BY_ARTIST_START);
        sb.append(artistName);
        sb.append("\"");

        if (sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        // Check created SQL string
        // System.out.println("SQL statement = " + sb.toString());

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<String> albums = new ArrayList<>();
            while (results.next()) {
                albums.add(results.getString(1));
            }
            return albums;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // ******
    // Query albums Song
    // ******
    public List<SongArtist> queryArtistForSong(String songName, int sortOrder) {

        // Build query string
        StringBuilder sb = new StringBuilder(QUERY_ARTIST_FOR_SONG_START);
        sb.append(songName);
        sb.append("\"");

        // Add sorting part to query string
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ARTIST_FOR_SONG_SORT);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        // Check created SQL string
        // System.out.println("SQL statement = " + sb.toString());

        // Try by resources
        // Create a 'statement' for sending SQL statements to the database
        // Execute query, returning all artist records with all column values
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            // Create ArrayList songArtists, for objects of type SongArtist
            List<SongArtist> songArtists = new ArrayList<>();

            // Loop through query results
            // Create a songArtist object for each record and save the record specifics
            while (results.next()) {
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(results.getString(1));
                songArtist.setAlbumName(results.getString(2));
                songArtist.setTrack(results.getInt(3));

                // Add songArtist object to the songArtists ArrayList
                songArtists.add(songArtist);
            }

            // Return the songArtists ArrayList
            return songArtists;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    // ******
    // Method to get meta-data from table
    // ******
    public void querySongsMetadata() {

        // Performs query. Then calls ResultsSet.getMetaData to get the schema information from the table.
        // Next, gets the column count (note: starting from 1) and use a loop to print each column name.
        // The meta-data can be used to provide info such as column names and types and the attributes.
        String sql = "SELECT * FROM " + TABLE_SONGS;
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sql)) {
            ResultSetMetaData meta = results.getMetaData();
            int numColumns = meta.getColumnCount();
            for(int i=1; i<= numColumns; i++) {
                System.out.format("Column %d in the songs table is names %s\n",
                        i, meta.getColumnName(i));
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            e.getStackTrace();
        }
    }

    // ******
    // Method to count number of songs (using function)
    // ******
    public int getCount(String table) {

        // Note, applying "AS count" and "AS min_id" to provide column names to the columns
        // Referring to the column names later on is easier in maintenance than referring to an index
        // As the index can change when adding/removing columns
        // String sql = "SELECT COUNT(*) AS count, MIN(_id) AS min_id FROM " + table;
        String sql = "SELECT COUNT(*) AS count FROM " + table;
        try(Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(sql)) {
            int count = results.getInt("count"); // column name used as reference
            // int min = results.getInt("min_id"); // column name used as reference
            //System.out.format("Count = %d, Min = %d\n", count, min);
            System.out.format("Count = %d\n", count);
            return count;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            e.getStackTrace();
            return -1;
        }
    }

    // ******
    // Method to create a view for artists songs
    // ******
    public boolean createViewForSongArtists() {
        try(Statement statement = conn.createStatement()) {
            System.out.println(CREATE_ARTIST_FOR_SONG_VIEW);
            statement.execute(CREATE_ARTIST_FOR_SONG_VIEW);
            return true;
        } catch (SQLException e) {
            System.out.println("Create View failed: " + e.getMessage());
            e.getStackTrace();
            return false;
        }
    }

    public List<SongArtist> querySongInfoView(String title) {
        StringBuilder sb = new StringBuilder(QUERY_VIEW_SONG_INFO);
        sb.append(title);
        sb.append("\"");

        System.out.println(sb.toString());

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<SongArtist> songArtists = new ArrayList<>();
            while (results.next()) {
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(results.getString(1));
                songArtist.setAlbumName(results.getString(2));
                songArtist.setTrack(results.getInt(3));
                songArtists.add(songArtist);
            }
            return songArtists;
        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}