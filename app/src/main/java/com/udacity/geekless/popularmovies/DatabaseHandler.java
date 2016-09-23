package com.udacity.geekless.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yahya on 23/09/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Movies";

    // Contacts table name
    private static final String TABLE_FAVURITE = "favourite";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_MV_ID = "movie_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_RATE = "rate";
    private static final String KEY_POSTER = "poster";
    private static final String KEY_BACKPATH = "backpath";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAVOURITE_TABLE = "CREATE TABLE " + TABLE_FAVURITE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_MV_ID + " INTEGER ,"
                + KEY_TITLE + " TEXT,"
                + KEY_OVERVIEW + " TEXT,"
                + KEY_RELEASE_DATE + " TEXT,"
                + KEY_RATE + " TEXT,"
                + KEY_POSTER + " TEXT,"
                + KEY_BACKPATH + " TEXT" + ")";
        db.execSQL(CREATE_FAVOURITE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVURITE);

        // Create tables again
        onCreate(db);
    }
    // Adding new movie
    public void addMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MV_ID, movie.getID()); // movie id
        values.put(KEY_TITLE, movie.getTitle()); // movie title
        values.put(KEY_OVERVIEW, movie.getOverview()); // movie overview
        values.put(KEY_RELEASE_DATE, movie.getReleasedate()); // movie release date
        values.put(KEY_RATE, movie.getRate()); // movie rate
        values.put(KEY_POSTER, movie.getPoster()); // movie poster
        values.put(KEY_BACKPATH, movie.getBackpath()); // movie backpath


        // Inserting Row
        db.insert(TABLE_FAVURITE, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public Movie getMovie(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVURITE, new String[] { KEY_MV_ID,
                        KEY_TITLE,
                        KEY_OVERVIEW,
                        KEY_RELEASE_DATE,
                        KEY_RATE,
                        KEY_POSTER,
                        KEY_BACKPATH }, KEY_MV_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if(cursor.getCount()==0)
        {
            return null;
        }
        Movie movie = new Movie();
        movie.setID(Integer.parseInt(cursor.getString(0)));
        movie.setTitle(cursor.getString(1));
        movie.setOverview(cursor.getString(2));
        movie.setReleaseDate(cursor.getString(3));
        movie.setRate(cursor.getString(5));
        movie.setPoster(cursor.getString(4));
        movie.setBackpath(cursor.getString(5));
        // return contact
        return movie;
    }

    // Getting All Movies
    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> MovieList = new ArrayList<Movie>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FAVURITE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setID(Integer.parseInt(cursor.getString(1)));
                movie.setTitle(cursor.getString(2));
                movie.setOverview(cursor.getString(3));
                movie.setReleaseDate(cursor.getString(4));
                movie.setRate(cursor.getString(5));
                movie.setPoster(cursor.getString(6));
                movie.setBackpath(cursor.getString(7));
                // Adding contact to list
                MovieList.add(movie);
            } while (cursor.moveToNext());
        }

        // return movies list
        return MovieList;
    }

    // Getting movies Count
    public int getMoviesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FAVURITE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Deleting single movie
    public void deleteContact(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVURITE, KEY_MV_ID + " = ?",
                new String[] { String.valueOf(movie.getID()) });
        db.close();
    }
}