package com.pbe.testdb;

import java.sql.*;

/** Study on Databases
 * Following Udemy Java programming masterclass for software developers Tim Buchalka.
 @author Pieter Beernink
 @version 1.0
 @since 1.0
 */

// *********************
// Databases
// *********************
// Terminology
// - Database: container for storage, for all data stored; referring to the entire data, structure, queries
// Note that SQLite stores all entire data in a single file, which is normally uncommon for a database
// - Database dictionary: a comprehensive list of the structure and types of data in the database
// - Table: collection of related data held in the database
// - Field: basic unit of data in a table (also referred to as "blobs", binary large objects)
// - Columns: another name for field (note: different from spreadsheet)
// - Row: single set of data containing all the columns in the table
// - Flat file database: stores all data in a single file (=spreadsheet), resulting in a lot of duplication
// Using a relation database, tables can be related to other tables
// - Database normalization is the process of structuring a database, usually a relational database,
// to reduce data redundancy and improve data integrity
// Normalization entails organizing the columns (attributes) and tables (relations) of a database
// to ensure that their dependencies are properly enforced by database integrity constraints.
// It is accomplished by applying some formal rules either by a process of synthesis
// (creating a new database design) or decomposition (improving an existing database design).
// - View: selection of rows and columns, possible from more than one table
// - Key: unique index of a row

// Notes on relational databases
// - A key in a table is an index, which provides a way to join columns and speed up searches.
// - The ordering of the rows in a relational database is undefined, similar to Java maps or sets.
// - By defining a key, the database is told that the data should be ordered on that (group of) column(s)
// - There can only be one primary key, which must be unique
// - Usually 'ID' is used as primary key. You can have keys that aren't unique, but if it's set as primary key, it must be
// - Columns can be set as 'NOT NULL', meaning that they must contain a value
// - Keeping data 'normalised' so that tables only contain information that relates to a single thing (song, album, artist)
//   is a fundamental part of relational databases. Doing so and then joining the tables back together, provides a great
//   deal of flexibility in querying and manipulation of data.

// SQLite
// SQLite is the most used database engine in the world
// It's a software library that provides a relational database management system.
// Lite as in lightweight in terms of setup, database administration, and required resources.
// It implements a small, fast, self-contained, high-reliability, full-featured, SQL database engine.
// Noticeable features: self-contained, serverless, zero-configuration, transactional.
// SQLite is handy to create/setup your database and to test queries to see all works well, before writing code.

// Notes on SQLite
// - SQLite does not have types for fields. Although specifying types, you can actually put any type of data in any column.
//   This however is not good practice and can cause errors.
// - SQLite lacks an alter table command for changing the type of a column.
//   Work around is creating a new tablet and migrating. But this is best to be avoided.
// - SQLite automatically generates a primary key if none is provided (https://www.sqlite.org/autoinc.html),
//   different from other databases like MS SQL server where you would have to set a column to autoincrement.
// - Joining tables in SQLite can be done with the command JOIN, which is a shorter alternative for INNER JOIN
//   It is however recommended to use INNER JOIN, as that's a more commonly use command (with other databases).
// - Note that it's possible to run a long command over multiple lines. SQLite will look for the ending ; to know when the command ends.
// - Major service client databases have stored procedures to store SQL procedures and execute them when needed.
//   SQLite does not have stored procedures. Reason for this is that SQLite is intended to be embedded in programs.
//   Rather than having a remote machine database that's connected to, to receive data.
// - SQLite views are read only, meaning they can be modified with INSERT/DELETE/UPDATE statements
//   to update data in the base tables through the view.

// SQLite (command line) commands:
// - To start: sqlite
// - Create a db: sqlite contacts.db
// - To quit: .quit
// - Help: .help
//
// - Create a table: CREATE TABLE contacts (name text, phone integer, email text);
//
// - Add an entry:
//      - INSERT INTO contacts (name, phone, email) VALUES('Name', 0123456789, 'name@provider.com');
//      - INSERT INTO contacts (name, phone) VALUES('Name', 0123456789);
//      - INSERT INTO contacts VALUES('Name', 0123456789, 'name@provider.com');
//
// - Query a table or entry:
//      - SELECT * FROM contacts;
//      - SELECT email FROM contacts;
//      - SELECT phone, email FROM contacts WHERE name="Name";
//      - SELECT * FROM artists WHERE _id = 133;
//      - SELECT name FROM albums WHERE _id = 367;
//
// - Query and order
//      - SELECT * FROM artists ORDER BY name; // note that lower case data will follow after upper case data
//      - SELECT * FROM albums ORDER BY name COLLATE NOCASE; // will ignore case in ordering
//      - SELECT * FROM albums ORDER BY name COLLATE NOCASE DESC; // descending order
//      - SELECT * FROM albums ORDER BY artist, name COLLATE NOCASE; // sorting first by artist id, then album name
//  NOTE: see below examples of returning multiple columns. You're free to return columns in any order.
//
//
// - Preventing duplicates
//   The DISTINCT clause is an optional clause of the  SELECT statement.
//   The DISTINCT clause allows you to remove the duplicate rows in the result set.
//   For example:
//      -
//
// - Updating an entry:
//      - UPDATE contacts SET email="name@provider.com";
//      Note: be very careful with commands like this, 'accidentally' updating thousands of records instead of a specific one.
//      - UPDATE contacts SET email="name@provider.com" WHERE name="Name";
//
// - Joining tables:
//      - SELECT track, title, name FROM songs JOIN albums ON songs.album = albums._id; // if there's no ambiguity in names, the table names can be left out
//      - SELECT songs.track, songs.title, albums.name FROM songs JOIN albums ON songs.album = albums._id; // but it's a good habit to specify the table name, especially in code
//      - SELECT songs.track, songs.title, albums.name FROM songs INNER JOIN albums ON songs.album = albums._id; // the ON part tells which album columns are part of the join
//      - SELECT songs.track, songs.title, albums.name FROM songs INNER JOIN albums ON songs.album = albums._id ORDER BY albums.name, songs.track; // the join can be followed by an ORDER BY command
//      - SELECT artists.name, albums.name FROM artists INNER JOIN albums ON artists._id = albums.artist ORDER BY artists.name;
//      - SELECT artists.name, albums.name FROM albums INNER JOIN artists ON albums.artist = artists._id ORDER BY artists.name;\
//
//    Inner joins can also be chained. See example below.
//      - SELECT artists.name, albums.name, songs.track, songs.title FROM songs
//        INNER JOIN albums ON songs.album = albums._id
//        INNER JOIN artists ON albums.artist = artists._id
//        ORDER BY artists.name, albums.name, songs.track;
//      - SELECT artists.name, albums.name, songs.track, songs.title FROM songs
//        INNER JOIN albums ON songs.album = albums._id
//        INNER JOIN artists ON albums.artist = artists._id
//        WHERE albums._id = 19
//        ORDER BY artists.name, albums.name, songs.track;
//
//   NOTES on joining tables:
//   - JOIN is a shortcut for INNER JOIN. It's recommended to use INNER JOIN, as that's more common (with other databases).
//   - It's possible to 'chain' multiple inner joins.
//
// - Using wildcards (https://www.sqlite.org/lang_expr.html)
//   Use the LIKE operator in the WHERE clause to query data based on partial information.
//   Do so in combination with the wildcard % to match any sequence of zero or more characters
//   And the wildcard _ to match any single character. For example:
//      - LIKE WHERE songs.title LIKE "%doctor%"
//      - LIKE WHERE albums.artist LIKE "Jefferson%"
//
//  - Using operators (https://www.sqlite.org/lang_expr.html)
//  SQLite comparison operators are similar to Java operators. For example:
//      - Equals can either be: = or ==
//        Alternatively: IS (note that if an operator value is NULL, this influences the evaluation)
//      - Non-equals can either be: != or <>
//        Alternatively: IS NOT (note that if an operator value is NULL, this influences the evaluation)
//      - Concatenate, joining two strings of its operands: ||
//      - Casting type to INTEGER and computing the remainder: %
//      - Between: >= and <=

// - Using views
//   A view is a result set of a stored query. It's a way to pack a query into a named object stored in the database.
//   A view can be used on one or more (joined) tables. Data of the underlying table(s) can be accessed through the view.
//   The tables that the query in the view definition refers to are called 'base tables'.
//   A view is useful to:
//   1. Provide an abstraction layer over tables, allowing to add/remove columns in the view without touching the schema of the underlying tables.
//   2. To encapsulate complex queries with joins to simplify the data access.
//   3. Offering security benefits. Via a view the (filtered) contents of a database can be viewed, but not modified.
//   Note that, in SQLite, you can't modify data (update, delete, insert) in a view.
//
//   To create a view:
//      - CREATE VIEW artists_list AS
//        SELECT artists.name, albums.name, songs.track, songs.title FROM songs
//        INNER JOIN albums ON songs.album = albums._id
//        INNER JOIN artists ON albums.artist = artists._id
//        ORDER BY artists.name, albums.name, songs.track;
//
//  When having created a list in SQLite, it will show under the .schema command.
//  To run a view:
//      - SELECT * FROM artists_list;
//
//  When having run a view, it's possible to filter the results, as if it was a table. For example:
//      - SELECT * FROM artists_list WHERE name LIKE "jefferson%";
//
//  Existing views cannot be overwritten. To delete one:
//      - DROP VIEW artists_list; // artists_list is the view name
//      - DROP VIEW IF EXISTS artists_list; // to delete, only if the scheme exists
//
//  Create a view with different column headers:
//  Use the command AS to given a certain field a different name.
//      - CREATE VIEW artists_list AS
//        SELECT artists.name AS artist, albums.name AS album, songs.track, songs.title FROM songs
//        INNER JOIN albums ON songs.album = albums._id
//        INNER JOIN artists ON albums.artist = artists._id
//        ORDER BY artists.name, albums.name, songs.track;
//  NOTE: you can now run a SELECT statement using these new column header names.

// - Deleting an entry:
//      - DELETE FROM contacts WHERE phone = 1234;
//      - DELETE FROM songs WHERE track < 50;
//      - DELETE FROM songs WHERE track <> 71; // <> means 'not equal to'
//
// - Counting records:
//      - SELECT count(*) FROM songs;
//
// - Making a backup:
//      - .backup contacts.db filename
//      - .backup filename
//      Note: if no database is specified, it will use the current database
//
//  SQLite commands:
//      - Restoring a backup: .restore filename
//      - Show all tables: .tables
//      - Show table scheme: .schema
//      - Show all SQL statements to create a table and its contents (a 'transaction'): .dump
//      - Switching table headers on/off: .headers on
//
//   NOTES on SQLite commands:
//      - SQLite commands do not have to be ended with ;
//      - If a command works, no feedback is given. It a command doesn't work, sqlite will give an error.
//      - Use of lowercase, uppercase does not influence sqlite. But it's common use to use uppercase for SQL commands.

// Practice example queries
// 1. All titles from album 'Forbidden':
//  - SELECT songs.title FROM songs INNER JOIN albums ON songs.album = albums._id WHERE albums.name = "Forbidden";
//
// 2. All titles from album 'Forbidden', in track order (including track number to see order):
//  - SELECT songs.track, songs.title FROM songs INNER JOIN albums ON songs.album = albums._id WHERE albums.name = "Forbidden" ORDER BY songs.track;
//
// 3. All songs from Deep Purple:
// - SELECT songs.title FROM songs INNER JOIN albums ON songs.album = albums._id INNER JOIN artists ON albums.artist = artists._id WHERE artists.name = "Deep Purple";
//
// 4. Renaming band "Mehitable" to "One Kitten":
// - UPDATE artists SET name="One Kitten" WHERE name="Mehitabel";
//
// 5. Checking for correct renaming
// - SELECT * FROM artists WHERE artists.name="Mehitabel";
//
// 6. Titles of all songs by Aerosmith in alphabetic order, including only the title in the output.
//  - SELECT artists.name, songs.title, albums.name FROM songs
//    INNER JOIN albums ON songs.album = albums._id
//    INNER JOIN artists ON albums.artist = artists._id
//    WHERE artists.name = "Aerosmith"
//    ORDER BY songs.title ASC;
//
// 7. Replacing the column as used just now, with count(title) to get a count of the number of songs.
//  - SELECT count(songs.title), artists.name, albums.name FROM songs INNER JOIN albums ON songs.album = albums._id
//    INNER JOIN artists ON albums.artist = artists._id
//    WHERE artists.name = "Aerosmith"
//    ORDER BY songs.title ASC;
//
// 8. Getting a list of the songs from 6, without duplicates.
//  - SELECT DISTINCT songs.title, artists.name, albums.name FROM songs INNER JOIN albums ON songs.album = albums._id
//    INNER JOIN artists ON albums.artist = artists._id
//    WHERE artists.name = "Aerosmith"
//    ORDER BY songs.title ASC;
//
// 9. Getting a count of the songs without duplicates.
//  - SELECT count(DISTINCT songs.title), artists.name FROM songs INNER JOIN albums ON songs.album = albums._id
//    INNER JOIN artists ON albums.artist = artists._id
//    WHERE artists.name = "Aerosmith";
//
// 10. Find the number of artists and the number of albums.
//  - SELECT count(DISTINCT album), artists.name FROM songs INNER JOIN albums ON songs.album = albums._id
//    INNER JOIN artists ON albums.artist = artists._id
//    WHERE artists.name = "Aerosmith";

// *********************
// Using JDBC (Java Database Connectivity)
// *********************
// Using JDBC allows to work not only with databases, but also spreadsheets and flat files.
// JDBC acts as a middleman between a Java application and a data source.
// To use a particular data source from an application, a suitable JDBC driver for that data source is required.
// For example for a SQLite database, an SQLite JDBC driver is required.
//
// Such driver is simply a Java library containing classes that implements the JDBC API.
// All JDBC drivers implement the same interfaces. This makes it easy to change the data source an application uses.
// For example changing from using a SQLite database to MySQL, requires the use of the MySQL JDBC driver instead.
// And of course migrating the data. 100% portability is never the case however.
// But when writing JDBC code, avoiding the use of database-specific SQL and behaviour, makes a possible future change easier.
//
// A JDBC driver has to be written in Java, but it can consist of a thin Java layer that calls code in other languages.
//
// For backwards compatibility, the JDBC API contains all methods that were in previous JDBC versions.
//
// JDBC consists of 2 packages:
// 1. Core JDBC: java.sql
// 2. Optional JDBC: javax.sql
// APIs in the javax.sql package are required when working with database servers.
//
// All popular databases provide JDBC drivers. The JDK ships with a database called 'derby'.
// Derby can be used for desktop applications or when prototyping. The Derby JDBC driver is included in the JDK.
//
// SQLite browser can be used as graphical interface to view and administrate databases.
//
// SQLite-JDBC driver: https://github.com/xerial/sqlite-jdbc
// SQLite browser: https://sqlitebrowser.org/
//
// To use SQLite in Java project:
// 1. Add SQLite JDBC driver to the project:
//    File -> Project Structure -> Libraries -> + button (new project library) and select Java
//    Finally, select the driver (.jar file) and confirm OK as module.
//    It will then show as library and be accessible by the application.
//
// 2. Create a database:
//    Have to try and connect. If it doesn't exist, SQLite will create it.
//    JDBC drivers need a connection string to connect to the database.
//    The exact format of the connection string will vary between the various types of databases.
//    One thing it has in common, is that it always starts with JDBC:
//    Other databases might require additional information, such as a username and password.
//    It's also possible to specify database attributes with the connection string.
//    For example, to specify that the SQLight database should be stored in memory.
//    To find out what's required for a certain database JDBC connection string: check documentation.
//
//    For SQLite:
//    To access a locally stored database on D drive:
//      - Connection conn = DriverManager.getConnection("jdbc:sqlite:D:\\databases\\testjava.db");
//
// 3. Establishing a connection with the database
//    A connection can be setup in 2 manners:
//      1. Using the driver manager
//      As just shown with the connection string.
//      2. Using datasource objects
//      Allows for advanced features like connection polling and distributed transactions.
//      It is also more portable, because of how the connections are established.
//      These features however are not relevant when using SQLite.
//      And they require the database administrator to enable these features.
//      It's also more complicated to use datasource objects and really only relevant
//      when working with Enterprise applications.
//
// 4. Create context table
//    Whenever you want to use SQL with JDBC, statement objects are used.
//    To create a table, connection.create statement method must be called.
//    And then use the statement.execute method to run a SQL statement.
//      - Statement statement = conn.createStatement();
//      - statement.execute("CREATE TABLE contacts (name TEXT, phone INTEGER, email TEXT)");

// Retrieve data with JDBC
// When retrieving data with JDBC, this will return a boolean. That is:
// - true if the executed statement returns an instance of the results set class
// - false if it returns an update count or no results

// ResultSet
// When querying a database, the method returns the records that match the query, as a ResultSet instance.
// The results can be retrieved by calling the .getResultSet method.
// It's then possible to loop through the results.
//
// If you reuse a statement object to do a query, any ResultsSet associated with that statement object
// is closed and a new one is created for the new query.
// So when working with several results set queries at the same time,
// it's imperative to use a different statement instance for each query.
// It's possible to reuse a statement instance.. but only when finishing processing one query, before executing the next.
// A statement object can ultimately only have on active ResultSet associated with it.
//
// Every ResultsSet has 'a cursor'.
// When a ResultSet is created, it's cursor is positioned before the first record.
// The first time calling ResultSet, the cursor will be moved to the first record.
// When calling it again, it will move to the second record in the ResultSet.
// When there are no more records,the next method record will return false.
//
// ResultSet is a resource and should be closed!

public class Main {

    // Set constants for the database
    public static final String DB_NAME = "testjava.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Coding\\Java Projects\\Java Masterclass\\Databases\\" + DB_NAME;
    public static final String TABLE_CONTACTS = "contacts"; // table name
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";

    public static void main(String[] args) {

//        // Use try with resources connection, making sure the resource will be closed when the try-catch block is exited
//        // Create a connection string and then a statement instance
//        try(Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\Coding\\Java Projects\\Java Masterclass\\Databases\\testjava.db");
//            Statement statement = conn.createStatement())
//        {
//            // Call execute method on statement instance to create a table by passing a SQL CREATE TABLE command
//            // Note that NO ; sign needs to be included to end the statement, because the driver understands
//            // that when execute is called, a complete statement is passed.
//            // Also note that statement is connected to the database when the connection instance was created.
//            // This means that the statement as such is connected with the statement and can only be run at that database.
//            statement.execute("CREATE TABLE contacts (name TEXT, phone INTEGER, email TEXT)");
//        } catch (SQLException e) {
//            // If the JDBC driver is not added to the project this will cause an exception
//            System.out.println("Something went wrong: " + e.getMessage());
//        }

        try {
            // Create a connection string
            // If the database doesn't exist at the given location, SQLite will create it
            Connection conn = DriverManager.getConnection(CONNECTION_STRING);
//            conn.setAutoCommit(false);

            // Create statement instance
            Statement statement = conn.createStatement();

            // Call execute method on statement instance to create a table by passing a SQL CREATE TABLE command
            // Note that NO ; sign needs to be included to end the statement, because the driver understands
            // that when execute is called, a complete statement is passed.
            // Also note that statement is connected to the database when the connection instance was created.
            // This means that the statement as such is connected with the statement and can only be run at that database.
            // Table will be created only, if not yet existing by use of "IF NOT EXISTS"
            statement.execute("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS +
                    "(" + COLUMN_NAME + " text, "
                        + COLUMN_PHONE + " integer, "
                        + COLUMN_EMAIL + " text)");

            // Insert a record
            // Note that changes are automatically committed to the database by the JDBC connection class
            // immediately after a statement is executed (this can be turned of with: conn.setAutoCommit(false);)
            // This is not the case with all types of databases. Some need to be explicitly instructed to commit
            // in order for the data to persist. In such cases, closing the connection before committing would result
            // in loss of the data.
//            statement.execute("INSERT INTO contacts (name, phone, email)" +
//                    "VALUES('PB', 123456789, 'pb@provides.com')");
//            statement.execute("INSERT INTO contacts (name, phone, email)" +
//                    "VALUES('Joe', 56564654, 'joe@regular.com')");
//            statement.execute("INSERT INTO contacts (name, phone, email)" +
//                    "VALUES('Leon', 7897887, 'leon@hitman.com')");
//            statement.execute("INSERT INTO contacts (name, phone, email)" +
//                    "VALUES('John', 999787, 'john@white.com')");

            insertContact(statement, "PB", 123456789, "pb@provides.com");
            insertContact(statement, "Joe", 56564654, "joe@regular.com");
            insertContact(statement, "Leon", 7897887, "leon@hitman.com");

            // Update a record
//            statement.execute("UPDATE contacts SET phone=1234 WHERE name='John'");

            statement.execute("UPDATE " + TABLE_CONTACTS + " SET " +
                    COLUMN_PHONE + "=1234" +
                    " WHERE " + COLUMN_NAME + "='John'");

            // Delete a record
//            statement.execute("DELETE FROM contacts WHERE name='John'");
            statement.execute("DELETE FROM " + TABLE_CONTACTS +
                    " WHERE " + COLUMN_NAME + "='John'");

            // Retrieve all data
            // This will return a boolean, that is:
            // - true if the executed statement returns an instance of the results set class
            // - false if it returns an update count or no results
            // When querying a database, the method returns the records that match the query, as a ResultSet instance.
            // The results can be retrieved by calling the .getResultSet method.
            // It's then possible to loop through the results.
//            statement.execute("SELECT * FROM contacts");
//            ResultSet results = statement.getResultSet();
            ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_CONTACTS); // shorter solution
            while (results.next()) {
                System.out.println(results.getString(COLUMN_NAME) + " " +
                        results.getInt(COLUMN_PHONE) + " " +
                        results.getString(COLUMN_EMAIL));
            }
            results.close();

            // First close any statement instances
            // Then close the connection
            // Note that if you would close the connection first.. the statement instance couldn't be closed anymore
            statement.close();
            conn.close();

        } catch (SQLException e) {

            // If the JDBC driver is not added to the project this will cause an exception
            System.out.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private static void insertContact(Statement statement, String name, int phone, String email) throws SQLException {
        statement.execute("INSERT INTO " + TABLE_CONTACTS +
                " (" + COLUMN_NAME + ", " +
                COLUMN_PHONE + ", " +
                COLUMN_EMAIL +
                ") " +
                "VALUES('" + name + "', " + phone + ", '" + email + "')");
    }
}