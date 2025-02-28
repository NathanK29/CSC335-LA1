package main.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicStore {
    private Map<String, Album> albums;
    private Map<String, List<Song>> songs;

    public MusicStore() {
        this.albums = new HashMap<String, Album>();
        this.songs = new HashMap<String, List<Song>>();
    }

    public void loadAllAlbums(String indexFilePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(indexFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length < 2) {
                    continue;
                }
                String albumTitle = parts[0].trim();
                String artist = parts[1].trim();
                String albumFileName = albumTitle + "_" + artist + ".txt";
                loadSingleAlbum(albumFileName);
            }
        }
    }

    private void loadSingleAlbum(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                return;
            }
            String[] headerParts = headerLine.split(",", 4);
            if (headerParts.length < 4) {
                return;
            }
            String albumTitle = headerParts[0].trim();
            String artist = headerParts[1].trim();
            String genre = headerParts[2].trim();
            int year = Integer.parseInt(headerParts[3].trim());

            Album album = new Album(albumTitle, artist, genre, year);

            String songTitle;
            while ((songTitle = reader.readLine()) != null) {
                songTitle = songTitle.trim();
                if (!songTitle.isEmpty()) {
                    Song song = new Song(songTitle, album);
                    album.addSong(song);

                    if (!this.songs.containsKey(songTitle)) {
                        this.songs.put(songTitle, new ArrayList<Song>());
                    }
                    this.songs.get(songTitle).add(song);
                }
            }
            this.albums.put(albumTitle, album);
        }
    }

    public Album findAlbumByTitle(String title) {
        return this.albums.get(title);
    }

    public List<Song> findSongsByTitle(String title) {
        if (this.songs.containsKey(title)) {
            return this.songs.get(title);
        } else {
            return new ArrayList<Song>();
        }
    }

    public List<Album> getAllAlbums() {
        return new ArrayList<Album>(this.albums.values());
    }

    public List<Song> getAllSongs() {
        List<Song> all = new ArrayList<Song>();
        for (String key : this.songs.keySet()) {
            List<Song> list = this.songs.get(key);
            all.addAll(list);
        }
        return all;
  
    }
    // Added helper methods to update internal maps (A.D)
    public void addAlbum(Album album) {
        albums.put(album.getTitle(), album);
    }

    public void addSong(Song song) {
        String songTitle = song.getTitle();
        // Add song to the songs maps(A.D)
        songs.computeIfAbsent(songTitle, k -> new ArrayList<>()).add(song);
    }
}
