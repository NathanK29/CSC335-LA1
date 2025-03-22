package main.model;

import main.database.*;
import java.io.Serializable;
import java.util.*;

public class LibraryModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Song> userLibrary;
    private Map<String, Playlist> playlists;
    private List<Song> favoriteSongs;
    private transient MusicStore musicStore;
    private LinkedList<Song> recentPlays; //  Added for tracking recent plays (A.D)
    private Map<Song, Integer> playCounts; // Added for tracking play counts (A.D)

    public LibraryModel(MusicStore musicStore) {
        this.musicStore = musicStore;
        this.userLibrary = new ArrayList<>();
        this.playlists = new HashMap<>();
        this.favoriteSongs = new ArrayList<>();
        this.recentPlays = new LinkedList<>();
        this.playCounts = new HashMap<>();
    }

    public void setMusicStore(MusicStore musicStore) {
        this.musicStore = musicStore;
    }

    public void addSongToLibrary(Song song) {
        if (musicStore.getAllSongs().contains(song)) {
            System.out.println("added " + song);
            userLibrary.add(song);
        }
    }

    public void addAlbumToLibrary(Album album) {
        for (Song song : album.getSongs()) {
            userLibrary.add(song);
        }
    }

    public boolean createPlaylist(String name) {
        if (playlists.containsKey(name)) {
            return false;
        }
        playlists.put(name, new Playlist(name));
        return true;
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

    public void removeSongFromPlaylist(String playlistName, Song song) {
        Playlist p = findPlaylistByName(playlistName);
        if (p != null) {
            p.removeSong(song);
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

    // Return songs sorted by title asc (NK)
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

    // Return songs sorted by rating ascending (NK)
    public List<Song> getSongsSortedByRating() {
        List<Song> sortedSongs = new ArrayList<>(userLibrary);
        Collections.sort(sortedSongs, Comparator.comparingInt(Song::getRating));
        return sortedSongs;
    }

    public void playSong(Song song) {
        if (recentPlays.contains(song)) {
            recentPlays.remove(song); // If song is already in recent plays remove it(A.D)
        }
        recentPlays.addFirst(song);
        if (recentPlays.size() > 10) { // Ensure only last 10 are kept(A.D)
            recentPlays.removeLast();
        }
        playCounts.put(song, playCounts.getOrDefault(song, 0) + 1);
        // Update automatic play list for recent plays(A.D)
        Playlist recentPlaylist = new Playlist("Recent Plays");
        for (Song s : recentPlays) {
            recentPlaylist.addSong(s);
        }
        playlists.put("Recent Plays", recentPlaylist);
        // Update automatic playlist for top plays(A.D)
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
}
