package main.model;

import main.database.*;
import java.io.Serializable;
import java.util.*;

public class LibraryModel implements Serializable {
    private static final long serialVersionUID = 1L;
    // List of songs that are in the user's library
    private List<Song> userLibrary;
    // Map of playlist names to Playlist objects (both user-created and automatic playlists)
    private Map<String, Playlist> playlists;
    // List of songs marked as favorites by the user
    private List<Song> favoriteSongs;
    // MusicStore reference is transient because it is not serialized (reassigned upon loading)
    private transient MusicStore musicStore;
    private LinkedList<Song> recentPlays; //  Added for tracking recent plays (A.D)
    private Map<Song, Integer> playCounts; // Added for tracking play counts (A.D)

    // Constructor that initializes all collections and stores the MusicStore reference.
    public LibraryModel(MusicStore musicStore) {
        this.musicStore = musicStore;
        this.userLibrary = new ArrayList<>(); // Holds songs added to the library
        this.playlists = new HashMap<>(); // Holds playlists (both user and automatic)
        this.favoriteSongs = new ArrayList<>(); // Holds favorite songs
        this.recentPlays = new LinkedList<>(); // For tracking the order of recently played songs
        this.playCounts = new HashMap<>(); // For counting how many times each song has been played
    }

    // Setter to update the MusicStore reference after deserialization
    public void setMusicStore(MusicStore musicStore) {
        this.musicStore = musicStore;
    }

    // Added for certain testcases
    private boolean isTestMode = false;

    public void enableTestMode() {
        this.isTestMode = true;
    }
    
    // Adds a song to the user's library if it exists in the MusicStore; then updates automatic playlists.
    public void addSongToLibrary(Song song) {
        if (isTestMode || (musicStore != null && musicStore.getAllSongs().contains(song))) {
            userLibrary.add(song);
            updateAutomaticPlaylists();
        }
    }

    // Adds all songs from the specified album into the library.
    public void addAlbumToLibrary(Album album) {
        for (Song song : album.getSongs()) {
            userLibrary.add(song);
        }
    }

    // Creates a new playlist with the given name if it doesn't already exist.
    public boolean createPlaylist(String name) {
        if (playlists.containsKey(name)) {
            return false;
        }
        playlists.put(name, new Playlist(name));
        return true;
    }

    // Adds a song to an existing playlist (by playlist name).
    public void addSongToPlaylist(String playlistName, Song song) {
        if (playlists.containsKey(playlistName)) {
            playlists.get(playlistName).addSong(song);
        }
    }

    // Marks a song as favorite; if not already marked, adds it to the favoriteSongs list and updates playlists.
    public void markSongAsFavorite(Song song) {
        if (!favoriteSongs.contains(song)) {
            favoriteSongs.add(song);
        }
        updateAutomaticPlaylists();
    }

    // Removes a song from the specified playlist if the playlist exists.
    public void removeSongFromPlaylist(String playlistName, Song song) {
        Playlist p = findPlaylistByName(playlistName);
        if (p != null) {
            p.removeSong(song);
        }
    }

    // Sets the rating for a song. Throws exception if rating is out of range.
    // Also, marks the song as favorite if rating is 5, then updates automatic playlists.
    public void rateSong(Song song, int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        song.setRating(rating);
        if (rating == 5) {
            markSongAsFavorite(song);
        }
        updateAutomaticPlaylists();
    }

    // Searches for songs by title (case-insensitive) and returns a list of matching songs.
    public List<Song> searchSongsByTitle(String title) {
        List<Song> results = new ArrayList<>();
        for (Song song : userLibrary) {
            if (song.getTitle().equalsIgnoreCase(title)) {
                results.add(song);
            }
        }
        return results;
    }

    // Searches for albums by title (case-insensitive) based on songs in the library.
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

    // Returns a copy of all songs in the library.
    public List<Song> getAllSongs() {
        return new ArrayList<>(userLibrary);
    }

    // Returns a copy of all playlists.
    public List<Playlist> getAllPlaylists() {
        return new ArrayList<>(playlists.values());
    }

    // Finds and returns a playlist by its name.
    public Playlist findPlaylistByName(String name) {
        return playlists.get(name);
    }

    // Returns a copy of the favorite songs list.
    public List<Song> getFavoriteSongs() {
        return new ArrayList<>(favoriteSongs);
    }

    // Searches for songs by artist (case-insensitive) and returns a list of matching songs.
    public List<Song> searchSongsByArtist(String artist) {
        List<Song> results = new ArrayList<>();
        for (Song song : userLibrary) {
            if (song.getAlbum().getArtist().equalsIgnoreCase(artist)) {
                results.add(song);
            }
        }
        return results;
    }

    // Searches for albums by artist (case-insensitive) and returns a list of matching albums.
    public List<Album> searchAlbumsByArtist(String artist) {
        List<Album> results = new ArrayList<>();
        for (Song song : userLibrary) {
            Album album = song.getAlbum();
            if (album.getArtist().equalsIgnoreCase(artist) && !results.contains(album)) {
                results.add(album);
            }
        }
        return results;
    }

    // Returns a list of unique albums from the library.
    public List<Album> getAllAlbumsInLibrary() {
        Set<Album> albumSet = new HashSet<>();
        for (Song s : userLibrary) {
            albumSet.add(s.getAlbum());
        }
        return new ArrayList<>(albumSet);
    }

    // Returns a list of unique artist names from the library.
    public List<String> getAllArtistsInLibrary() {
        Set<String> artistSet = new HashSet<>();
        for (Song s : userLibrary) {
            artistSet.add(s.getAlbum().getArtist());
        }
        return new ArrayList<>(artistSet);
    }

    // Return songs sorted by title ascending (NK)
    public List<Song> getSongsSortedByTitle() {
        List<Song> sortedSongs = new ArrayList<>(userLibrary);
        Collections.sort(sortedSongs, Comparator.comparing(Song::getTitle, String.CASE_INSENSITIVE_ORDER));
        return sortedSongs;
    }

    // Return songs sorted by artist ascending (NK)
    public List<Song> getSongsSortedByArtist() {
        List<Song> sortedSongs = new ArrayList<>(userLibrary);
        Collections.sort(sortedSongs, Comparator.comparing(song -> song.getAlbum().getArtist(), String.CASE_INSENSITIVE_ORDER));
        return sortedSongs;
    }

    // Return songs in a random order; uses Collections.shuffle to simulate shuffle.
    public Iterable<Song> getShuffledSongs() {
        List<Song> copy = new ArrayList<>(userLibrary);
        Collections.shuffle(copy);
        return copy;
    }

    // Removes the specified song from the library.
    public void removeSongFromLibrary(Song song) {
        if(userLibrary.contains(song)) {
            userLibrary.remove(song);
        }
        updateAutomaticPlaylists();
    }

    // Removes all songs from the library that belong to the specified album.
    // A new list is created to hold songs to be removed, then removed from the main library.
    public void removeAlbumFromLibrary(Album album) {
        List<Song> songsToRemove = new ArrayList<>();
        for (Song s : userLibrary) {
            if (s.getAlbum().getTitle().equalsIgnoreCase(album.getTitle()) &&
                    s.getAlbum().getArtist().equalsIgnoreCase(album.getArtist())) {
                songsToRemove.add(s);
            }
        }
        userLibrary.removeAll(songsToRemove);
        updateAutomaticPlaylists();
    }

    // Return songs sorted by rating ascending (NK)
    public List<Song> getSongsSortedByRating() {
        List<Song> sortedSongs = new ArrayList<>(userLibrary);
        Collections.sort(sortedSongs, Comparator.comparingInt(Song::getRating));
        return sortedSongs;
    }

    // Simulates playing a song. Updates recent plays and play count,
    // then updates automatic playlists ("Recent Plays" and "Top Plays").
    public void playSong(Song song) {
        if (recentPlays.contains(song)) {
            recentPlays.remove(song); // If song is already in recent plays remove it (A.D)
        }
        recentPlays.addFirst(song);
        if (recentPlays.size() > 10) { // Ensure only last 10 are kept (A.D)
            recentPlays.removeLast();
        }
        playCounts.put(song, playCounts.getOrDefault(song, 0) + 1);
        // Update automatic play list for recent plays (A.D)
        Playlist recentPlaylist = new Playlist("Recent Plays");
        for (Song s : recentPlays) {
            recentPlaylist.addSong(s);
        }
        playlists.put("Recent Plays", recentPlaylist);
        // Update automatic playlist for top plays (A.D)
        List<Map.Entry<Song, Integer>> entries = new ArrayList<>(playCounts.entrySet());
        entries.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        Playlist topPlaylist = new Playlist("Top Plays");
        int count = 0;
        for (Map.Entry<Song, Integer> entry : entries) {
            topPlaylist.addSong(entry.getKey());
            count++;
            if (count >= 10) {
                break;
            }
        }
        playlists.put("Top Plays", topPlaylist);
    }

    // Updates the "Favorite Songs" automatic playlist based on favorites and songs rated 5.
    private void updateFavoritePlaylist() {
        Playlist favorites = new Playlist("Favorite Songs");
        for (Song s : userLibrary) {
            if (favoriteSongs.contains(s) || s.getRating() == 5) {
                favorites.addSong(s);
            }
        }
        playlists.put("Favorite Songs", favorites);
    }

    // Updates the "Top Rated" automatic playlist by including songs with rating 4 or higher.
    private void updateTopRatedPlaylist() {
        Playlist topRated = new Playlist("Top Rated");
        for (Song s : userLibrary) {
            if (s.getRating() >= 4) {
                topRated.addSong(s);
            }
        }
        playlists.put("Top Rated", topRated);
    }

    // Updates genre-based automatic playlists for any genre that has at least 10 songs.
    private void updateGenrePlaylists() {
        Map<String, List<Song>> genreMap = new HashMap<>();
        for (Song s : userLibrary) {
            String genre = s.getAlbum().getGenre();
            genreMap.computeIfAbsent(genre, k -> new ArrayList<>()).add(s);
        }
        for (Map.Entry<String, List<Song>> entry : genreMap.entrySet()) {
            String genre = entry.getKey();
            List<Song> songs = entry.getValue();
            if (songs.size() >= 10) {
                Playlist genrePlaylist = new Playlist(genre + " Playlist");
                for (Song s : songs) {
                    genrePlaylist.addSong(s);
                }
                playlists.put(genre + " Playlist", genrePlaylist);
            }
        }
    }

    // Calls the update methods for automatic playlists (favorites, top rated, genre-based).
    @SuppressWarnings("unused")
    private void updateAutomaticPlaylists() {
        updateFavoritePlaylist();
        updateTopRatedPlaylist();
        updateGenrePlaylists();
    }
}
