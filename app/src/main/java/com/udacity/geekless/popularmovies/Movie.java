package com.udacity.geekless.popularmovies;

/**
 * Created by yahya on 23/09/16.
 */
public class Movie {
    //private variables
    int _id;
    String _title;
    String _overview;
    String _relate_date ;
    String _backpath;
    String _poster;
    String _rate;

    // Empty constructor
    public Movie(){

    }

    public Movie(int id){
        this._id = id;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting titile
    public String getTitle(){
        return this._title;
    }

    public void  setTitle(String title){
        this._title = title;
    }

    // getting overview
    public String getOverview(){
        return this._overview;
    }

    // setting overview
    public void setOverview(String overview){
        this._overview = overview;
    }

    // getting release date
    public String getReleasedate(){
        return this._relate_date;
    }

    // setting release date
    public void setReleaseDate(String releasedate){
        this._relate_date = releasedate;
    }

    // getting backpath
    public String getBackpath(){
        return this._backpath;
    }

    // setting backpath
    public void setBackpath(String backpath){
        this._backpath = backpath;
    }

    // getting poster
    public String getPoster(){
        return this._poster;
    }

    // setting poster
    public void setPoster(String poster){
        this._poster = poster;
    }

    // getting rate
    public String getRate(){
        return this._rate;
    }

    // setting rate
    public void setRate(String rate){
        this._rate = rate;
    }

}
