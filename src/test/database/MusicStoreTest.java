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
}