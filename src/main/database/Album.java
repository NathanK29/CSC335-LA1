package main.database;

import java.util.ArrayList;

public class Album {
    final private String title;
    final private String artist;
    final private String genre;
    final private Integer year;
    final private ArrayList<String> songs;

    public Album(String title, String artist, String genre, Integer year, ArrayList<String> songs) {
         this.title = title;
         this.artist = artist;
         this.genre = genre;
         this.year = year;
         // Pass in a reference to song names stored as strings in list, then those can be looked up for the objects
         this.songs = songs;
    }

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public String getGenre() {
        return this.genre;
    }

    public Integer getYear() {
        return this.year;
    }

    public ArrayList<String> getSongs() {
        // return deep copy of songs list so it can't be manipulated after creation
        return new ArrayList<>(this.songs);
    }

}
