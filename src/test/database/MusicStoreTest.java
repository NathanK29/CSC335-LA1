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
        assertTrue(store.getAllAlbums().isEmpty(), "No albums expected at initialization.");
        assertTrue(store.getAllSongs().isEmpty(), "No songs expected at initialization.");
    }

    @Test
    public void testLoadMultipleAlbums() {
        MusicStore store = new MusicStore();
        try {
            store.loadAllAlbums("src/test/albums/albums.txt");
            assertTrue(store.getAllAlbums().size() >= 3, "Expected at least three albums.");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFindAlbumInMultipleAlbums() {
        MusicStore store = new MusicStore();
        try {
            store.loadAllAlbums("src/test/albums/albums.txt");
            Album rush = store.findAlbumByTitle("A Rush of Blood to the Head");
            assertNotNull(rush, "Album not found: A Rush of Blood to the Head.");
            assertEquals("Coldplay", rush.getArtist(), "Incorrect artist for A Rush of Blood to the Head.");
            Album begin = store.findAlbumByTitle("Begin Again");
            assertNotNull(begin, "Album not found: Begin Again.");
            assertEquals("Norah Jones", begin.getArtist(), "Incorrect artist for Begin Again.");
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
            assertFalse(scientist.isEmpty(), "Song not found: The Scientist.");
            List<Song> holdOn = store.findSongsByTitle("Hold On");
            assertFalse(holdOn.isEmpty(), "Song not found: Hold On.");
            List<Song> begin = store.findSongsByTitle("Begin Again");
            assertFalse(begin.isEmpty(), "Song not found: Begin Again.");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}
