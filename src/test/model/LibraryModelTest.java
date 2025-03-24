package test.model;

import main.model.LibraryModel;
import main.model.Playlist;
import main.database.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryModelTest {
    private MusicStore musicStore;
    private LibraryModel libraryModel;
    private Album testAlbum;
    private Song song1;
    private Song song2;

    @BeforeEach
    void setUp() {
        musicStore = new MusicStore();
        testAlbum = new Album("Test Album", "Test Artist", "Test Genre", 2023);
        song1 = new Song("Song 1", testAlbum);
        song2 = new Song("Song 2", testAlbum);
        testAlbum.addSong(song1);
        testAlbum.addSong(song2);

        musicStore.addAlbum(testAlbum);
        musicStore.addSong(song1);
        musicStore.addSong(song2);
        libraryModel = new LibraryModel(musicStore);
    }
    @Test
    void testAddSongToLibrary_Success() {
        libraryModel.addSongToLibrary(song1);
        assertTrue(libraryModel.getAllSongs().contains(song1));
    }

    @Test
    void testAddSongToLibrary_NotInStore() {
        Song invalidSong = new Song("Invalid Song", testAlbum);
        libraryModel.addSongToLibrary(invalidSong);
        assertFalse(libraryModel.getAllSongs().contains(invalidSong));
    }

    @Test
    void testAddAlbumToLibrary() {
        libraryModel.addAlbumToLibrary(testAlbum);
        assertTrue(libraryModel.getAllSongs().containsAll(testAlbum.getSongs()));
    }

    @Test
    void testCreatePlaylist() {
        libraryModel.createPlaylist("My Playlist");
        assertEquals(1, libraryModel.getAllPlaylists().size());
        assertEquals("My Playlist", libraryModel.getAllPlaylists().get(0).getName());
    }

    @Test
    void testAddSongToPlaylist() {
        libraryModel.createPlaylist("My Playlist");
        libraryModel.addSongToPlaylist("My Playlist", song1);
        assertEquals(1, libraryModel.getAllPlaylists().get(0).getSongs().size());
        assertTrue(libraryModel.getAllPlaylists().get(0).getSongs().contains(song1));
    }

    @Test
    void testMarkSongAsFavorite() {
        libraryModel.addSongToLibrary(song1);
        libraryModel.markSongAsFavorite(song1);
        assertTrue(libraryModel.getFavoriteSongs().contains(song1));
    }

    @Test
    void testRateSong_Valid() {
        libraryModel.addSongToLibrary(song1);
        libraryModel.rateSong(song1, 5);
        assertEquals(5, song1.getRating());
        assertTrue(libraryModel.getFavoriteSongs().contains(song1));
    }

    @Test
    void testRateSong_Invalid() {
        libraryModel.addSongToLibrary(song1);
        assertThrows(IllegalArgumentException.class, () -> libraryModel.rateSong(song1, 6));
    }


    @Test
    void testSearchSongsByTitle() {
        libraryModel.addSongToLibrary(song1);
        List<Song> results = libraryModel.searchSongsByTitle("Song 1");
        assertEquals(1, results.size());
        assertEquals(song1, results.get(0));
    }

 
    @Test
    void testSearchAlbumsByTitle() {
        libraryModel.addAlbumToLibrary(testAlbum);
        List<Album> results = libraryModel.searchAlbumsByTitle("Test Album");
        System.out.println("Search results: " + results);
        assertEquals(1, results.size());
        assertEquals(testAlbum, results.get(0));
    }


    @Test
    void testGetFavoriteSongs() {
        libraryModel.addSongToLibrary(song1);
        libraryModel.markSongAsFavorite(song1);
        assertEquals(1, libraryModel.getFavoriteSongs().size());
        assertTrue(libraryModel.getFavoriteSongs().contains(song1));
    }
    @Test
    void testPlaySongUpdatesRecentPlays() {
        libraryModel.addSongToLibrary(song1);
        libraryModel.addSongToLibrary(song2);

        libraryModel.playSong(song1);
        libraryModel.playSong(song2);
        libraryModel.playSong(song1);

        Playlist recentPlaylist = libraryModel.findPlaylistByName("Recent Plays");
        assertNotNull(recentPlaylist, "Recent Plays playlist should exist.");

        List<Song> recentSongs = recentPlaylist.getSongs();
        assertEquals(2, recentSongs.size(), "There should be two songs in Recent Plays.");
        assertEquals(song1, recentSongs.get(0), "Most recent song should be Song 1.");
        assertEquals(song2, recentSongs.get(1), "Second song should be Song 2.");
    }

    @Test
    void testPlaySongUpdatesTopPlays() {
        libraryModel.addSongToLibrary(song1);
        libraryModel.addSongToLibrary(song2);

        libraryModel.playSong(song1);
        libraryModel.playSong(song2);
        libraryModel.playSong(song1);
        libraryModel.playSong(song2);
        libraryModel.playSong(song1);

        Playlist topPlaylist = libraryModel.findPlaylistByName("Top Plays");
        assertNotNull(topPlaylist, "Top Plays playlist should exist.");

        List<Song> topSongs = topPlaylist.getSongs();
        assertEquals(2, topSongs.size(), "There should be 2 songs in Top Plays.");
        assertEquals(song1, topSongs.get(0), "Top song should be Song 1.");
        assertEquals(song2, topSongs.get(1), "Seond top song should be Song 2.");
    }

    @Test
    void testRecentPlaysLimit() {
        Album extraAlbum = new Album("Extra Album", "Extra Artist", "Pop", 2023);
        int extraCount = 11;
        Song[] extraSongs = new Song[extraCount];
        for (int i = 0; i < extraCount; i++) {
            extraSongs[i] = new Song("Extra Song " + i, extraAlbum);
            extraAlbum.addSong(extraSongs[i]);
            musicStore.addSong(extraSongs[i]);
            libraryModel.addSongToLibrary(extraSongs[i]);
        }

        for (int i = 0; i < extraCount; i++) {
            libraryModel.playSong(extraSongs[i]);
        }

        Playlist recentPlaylist = libraryModel.findPlaylistByName("Recent Plays");
        assertNotNull(recentPlaylist, "Recent Plays playlist should exist.");
        List<Song> recentSongs = recentPlaylist.getSongs();
        assertEquals(10, recentSongs.size(), "Recent Plays playlist should only keep ten songs.");
        assertEquals("Extra Song 10", recentSongs.get(0).getTitle(), "Most recent song should be Extra Song 10.");
        assertEquals("Extra Song 1", recentSongs.get(9).getTitle(), "Oldest song in recent plays should be Extra Song 1.");
    }

    @Test
    void testSongsSortedByTitle() {
        Song sA = new Song("Alpha", testAlbum);
        Song sC = new Song("Charlie", testAlbum);
        Song sB = new Song("Bravo", testAlbum);
        libraryModel.addSongToLibrary(sA);
        libraryModel.addSongToLibrary(sC);
        libraryModel.addSongToLibrary(sB);
        List<Song> sorted = libraryModel.getSongsSortedByTitle();
        assertEquals("Alpha", sorted.get(0).getTitle());
        assertEquals("Bravo", sorted.get(1).getTitle());
        assertEquals("Charlie", sorted.get(2).getTitle());
    }

    @Test
    void testSongsSortedByArtist() {
        Album albumA = new Album("Album A", "Artist A", "Genre", 2023);
        Album albumB = new Album("Album B", "Artist B", "Genre", 2023);
        Song songA = new Song("Song", albumB);
        Song songB = new Song("Song", albumA);
        libraryModel.addSongToLibrary(songA);
        libraryModel.addSongToLibrary(songB);
        List<Song> sorted = libraryModel.getSongsSortedByArtist();
        assertEquals("Artist A", sorted.get(0).getAlbum().getArtist());
        assertEquals("Artist B", sorted.get(1).getAlbum().getArtist());
    }

    @Test
    void testSongsSortedByRating() {
        Song songLow = new Song("Low Rated", testAlbum);
        Song songHigh = new Song("High Rated", testAlbum);
        libraryModel.addSongToLibrary(songLow);
        libraryModel.addSongToLibrary(songHigh);
        songLow.setRating(2);
        songHigh.setRating(5);
        List<Song> sorted = libraryModel.getSongsSortedByRating();
        assertEquals(2, sorted.get(0).getRating());
        assertEquals(5, sorted.get(1).getRating());
    }

    @Test
    void testShuffleSongsLibrary() {
        libraryModel.addSongToLibrary(song1);
        libraryModel.addSongToLibrary(song2);
        Iterable<Song> shuffledIterable = libraryModel.getShuffledSongs();
        List<Song> shuffled = new ArrayList<>();
        for (Song s : shuffledIterable) {
            shuffled.add(s);
        }
        assertEquals(2, shuffled.size());
        assertTrue(shuffled.contains(song1));
        assertTrue(shuffled.contains(song2));
    }

    @Test
    void testRemoveSongFromLibrary() {
        libraryModel.addSongToLibrary(song1);
        libraryModel.removeSongFromLibrary(song1);
        assertFalse(libraryModel.getAllSongs().contains(song1));
    }

    @Test
    void testRemoveAlbumFromLibrary() {
        libraryModel.addAlbumToLibrary(testAlbum);
        libraryModel.removeAlbumFromLibrary(testAlbum);
        for (Song s : testAlbum.getSongs()) {
            assertFalse(libraryModel.getAllSongs().contains(s));
        }
    }
}