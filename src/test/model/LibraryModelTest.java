package test.model;

import main.model.LibraryModel;
import main.database.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
}