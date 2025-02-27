package main.database;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private final String title;
    private final String artist;
    private final String genre;
    private final int year;
    private final List<Song> songs;

    public Album(String title, String artist, String genre, int year) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.year = year;
        this.songs = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }
}
