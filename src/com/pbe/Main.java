package com.pbe;

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
// - Using wildcards
//   Use the LIKE operator in the WHERE clause to query data based on partial information.
//   Do so in combination with the wildcard % to match any sequence of zero or more characters
//   And the wildcard _ to match any single character. For example:
//      - LIKE WHERE songs.title LIKE "%doctor%"
//      - LIKE WHERE albums.artist LIKE "Jefferson%"
//
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
//      - DELETE FROM contacts WHERE phone=1234;
//
// - Making a backup:
//      - .backup contacts.db filename
//      - .backup filename
//      Note: if no database is specified, it will use the current database
//
// - Restoring a backup: .restore filename
//
// - Show all tables: .tables
// - Show table scheme: .schema
// - Show all SQL statements to create a table and its contents (a 'transaction'): .dump
// - Switching table headers on/off: .headers on
//
// NOTES on commands:
// - If a command works, no feedback is given. It a command doesn't work, sqlite will give an error.
// - Use of lowercase, uppercase does not influence sqlite. But it's common use to use uppercase for SQL commands.

public class Main {

    public static void main(String[] args) {
	// write your code here
    }
}