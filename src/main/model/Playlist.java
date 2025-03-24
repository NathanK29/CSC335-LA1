package main.model;

import main.database.Song;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Playlist implements Serializable {
    private static final long serialVersionUID = 1L;
    // The name of the playlist.
    private String name;
    // The list of songs contained in this playlist.
    private List<Song> songs;

    // Constructor: Initializes a new Playlist with the specified name.
    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>(); // Initialize the songs list.
    }

    // Adds a song to the playlist if it is not already present.
    public void addSong(Song song) {
        if (!songs.contains(song)) {
            songs.add(song);
        }
    }

    // Removes the specified song from the playlist.
    public void removeSong(Song song) {
        songs.remove(song);
    }

    // Returns a new list containing all songs in the playlist.
    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    // Returns an Iterable of the songs in a random order.
    // Uses Collections.shuffle to randomize the order.
    public Iterable<Song> getShuffledSongs() {
        List<Song> copy = new ArrayList<>(songs);
        Collections.shuffle(copy);
        return copy;
    }

    // Returns the name of the playlist.
    public String getName() {
        return name;
    }

    // Returns a formatted string representation of the playlist (name and number of songs).
    @Override
    public String toString() {
        return String.format("Playlist: %s (%d songs)", name, songs.size());
    }
}
