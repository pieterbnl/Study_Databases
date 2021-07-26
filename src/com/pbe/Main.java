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
//   NOTE: JOIN is a shortcut for INNER JOIN. It's recommended to use INNER JOIN, as that's more common (with other databases).
//
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
