package main.model;

import main.database.*;
import java.util.*;

public class LibraryModel {
    private List<Song> userLibrary;
    private Map<String, Playlist> playlists;
    private List<Song> favoriteSongs;
    private MusicStore musicStore;

    public LibraryModel(MusicStore musicStore) {
        this.musicStore = musicStore;
        this.userLibrary = new ArrayList<>();
        this.playlists = new HashMap<>();
        this.favoriteSongs = new ArrayList<>();
    }

    public void addSongToLibrary(Song song) {
        if (musicStore.getAllSongs().contains(song)) {
            System.out.println("added " + song);
            userLibrary.add(song);
        }
    }

    public void addAlbumToLibrary(Album album) {
        for (Song song : album.getSongs()) {
            if (musicStore.getAllSongs().contains(song)) {
                userLibrary.add(song);
            }
        }
    }

    public void createPlaylist(String name) {
        playlists.putIfAbsent(name, new Playlist(name));
    }


    public void addSongToPlaylist(String playlistName, Song song) {
        if (playlists.containsKey(playlistName)) {
            playlists.get(playlistName).addSong(song);
        }
    }


    public void markSongAsFavorite(Song song) {
        if (!favoriteSongs.contains(song)) {
            favoriteSongs.add(song);
        }
    }


    public void rateSong(Song song, int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        song.setRating(rating);
        if (rating == 5) {
            markSongAsFavorite(song);
        }
    }

    public List<Song> searchSongsByTitle(String title) {
        List<Song> results = new ArrayList<>();
        for (Song song : userLibrary) {
            if (song.getTitle().equalsIgnoreCase(title)) {
                results.add(song);
            }
        }
        return results;
    }

    public List<Album> searchAlbumsByTitle(String title) {
        List<Album> results = new ArrayList<>();
        for (Song song : userLibrary) {
            Album album = song.getAlbum();
            if (album.getTitle().equalsIgnoreCase(title) && !results.contains(album)) {
                results.add(album);
            }
        }
        return results;
    }

    public List<Song> getAllSongs() {
        return new ArrayList<>(userLibrary);
    }

    public List<Playlist> getAllPlaylists() {
        return new ArrayList<>(playlists.values());
    }

    public Playlist findPlaylistByName(String name) {
        return playlists.get(name);
    }

    public List<Song> getFavoriteSongs() {
        return new ArrayList<>(favoriteSongs);
    }

    public List<Song> searchSongsByArtist(String artist) {
        List<Song> results = new ArrayList<>();
        for (Song song : userLibrary) {
            if (song.getAlbum().getArtist().equalsIgnoreCase(artist)) {
                results.add(song);
            }
        }
        return results;
    }

    public List<Album> searchAlbumsByArtist(String artist) {
        // We only store songs in userLibrary, so gather distinct albums
        List<Album> results = new ArrayList<>();
        for (Song song : userLibrary) {
            Album album = song.getAlbum();
            if (album.getArtist().equalsIgnoreCase(artist) && !results.contains(album)) {
                results.add(album);
            }
        }
        return results;
    }

    public List<Album> getAllAlbumsInLibrary() {
        Set<Album> albumSet = new HashSet<>();
        for (Song s : userLibrary) {
            albumSet.add(s.getAlbum());
        }
        return new ArrayList<>(albumSet);
    }

    public List<String> getAllArtistsInLibrary() {
        Set<String> artistSet = new HashSet<>();
        for (Song s : userLibrary) {
            artistSet.add(s.getAlbum().getArtist());
        }
        return new ArrayList<>(artistSet);
    }
}