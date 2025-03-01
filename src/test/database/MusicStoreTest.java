package test.database;

import main.database.MusicStore;
import main.database.Album;
import main.database.Song;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MusicStoreTest {

    @Test
    public void testEmptyInitialization() {
        MusicStore store = new MusicStore();
        assertTrue(store.getAllAlbums().isEmpty());
        assertTrue(store.getAllSongs().isEmpty());
    }

    @Test
    public void testLoadMultipleAlbums() {
        MusicStore store = new MusicStore();
        try {
            store.loadAllAlbums("src/test/albums/albums.txt");
            assertTrue(store.getAllAlbums().size() >= 3);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFindAlbumInMultipleAlbums() {
        MusicStore store = new MusicStore();
        try {
            store.loadAllAlbums("src/test/albums/albums.txt");
            List<Album> rushAlbums = store.findAlbumsByTitle("A Rush of Blood to the Head");
            assertFalse(rushAlbums.isEmpty());
            Album rush = rushAlbums.get(0);
            assertEquals("Coldplay", rush.getArtist());

            List<Album> beginAlbums = store.findAlbumsByTitle("Begin Again");
            assertFalse(beginAlbums.isEmpty());
            Album begin = beginAlbums.get(0);
            assertEquals("Norah Jones", begin.getArtist());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFindSongsInMultipleAlbums() {
        MusicStore store = new MusicStore();
        try {
            store.loadAllAlbums("src/test/albums/albums.txt");
            List<Song> scientist = store.findSongsByTitle("The Scientist");
            assertFalse(scientist.isEmpty());
            List<Song> holdOn = store.findSongsByTitle("Hold On");
            assertFalse(holdOn.isEmpty());
            List<Song> begin = store.findSongsByTitle("Begin Again");
            assertFalse(begin.isEmpty());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddAlbum() {
        MusicStore store = new MusicStore();
        Album album = new Album("Test Album", "Test Artist", "Rock", 2024);

        store.addAlbum(album);
        
        List<Album> foundAlbums = store.findAlbumsByTitle("Test Album");
        assertFalse(foundAlbums.isEmpty());
        assertEquals("Test Artist", foundAlbums.get(0).getArtist());
    }

    @Test
    public void testAddSong() {
        MusicStore store = new MusicStore();
        Album album = new Album("Test Album", "Test Artist", "Rock", 2024);
        Song song = new Song("Test Song", album);

        store.addSong(song);
        
        List<Song> foundSongs = store.findSongsByTitle("Test Song");
        assertFalse(foundSongs.isEmpty());
        assertEquals("Test Album", foundSongs.get(0).getAlbum().getTitle());
    }

    @Test
    public void testFindSongsByArtist() {
        MusicStore store = new MusicStore();
        Album album1 = new Album("Album One", "Test Artist", "Pop", 2020);
        Album album2 = new Album("Album Two", "Test Artist", "Pop", 2021);
        Song song1 = new Song("Song A", album1);
        Song song2 = new Song("Song B", album2);
        store.addSong(song1);
        store.addSong(song2);

        List<Song> songsByArtist = store.findSongsByArtist("Test Artist");
        assertEquals(2, songsByArtist.size());
    }

    @Test
    public void testFindAlbumsByArtist() {
        MusicStore store = new MusicStore();
        Album album1 = new Album("Album One", "Test Artist", "Pop", 2020);
        Album album2 = new Album("Album Two", "Test Artist", "Pop", 2021);
        store.addAlbum(album1);
        store.addAlbum(album2);

        List<Album> albumsByArtist = store.findAlbumsByArtist("Test Artist");
        assertEquals(2, albumsByArtist.size());
    }
}