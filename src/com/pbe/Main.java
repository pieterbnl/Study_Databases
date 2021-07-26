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
//
// - Updating an entry:
//      - UPDATE contacts SET email="name@provider.com";
//      Note: be very careful with commands like this, 'accidentally' updating thousands of records instead of a specific one.
//      - UPDATE contacts SET email="name@provider.com" WHERE name="Name";
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

// NOTE:
// - If a command works, no feedback is given. It a command doesn't work, sqlite will give an error.
// - Use of lowercase, uppercase does not influence sqlite. But it's common use to use uppercase for SQL commands.



public class Main {

    public static void main(String[] args) {
	// write your code here
    }
}
