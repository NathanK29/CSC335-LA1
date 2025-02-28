package test.model;

import main.model.Playlist;
import main.database.Song;
import main.database.Album;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlaylistTest {

    @Test
    public void testAddSong() {
        Playlist playlist = new Playlist("My Playlist");
        Album album = new Album("Test Album", "Test Artist", "Test Genre", 2023);
        Song song = new Song("Test Song", album);

        playlist.addSong(song);
        assertEquals(1, playlist.getSongs().size());
        assertTrue(playlist.getSongs().contains(song));
    }

    @Test
    public void testRemoveSong() {
        Playlist playlist = new Playlist("My Playlist");
        Album album = new Album("Test Album", "Test Artist", "Test Genre", 2023);
        Song song = new Song("Test Song", album);

        playlist.addSong(song);
        playlist.removeSong(song);
        assertEquals(0, playlist.getSongs().size());
        assertFalse(playlist.getSongs().contains(song));
    }

    @Test
    public void testGetName() {
        Playlist playlist = new Playlist("My Playlist");
        assertEquals("My Playlist", playlist.getName());
    }

    @Test
    public void testToString() {
        Playlist playlist = new Playlist("My Playlist");
        Album album = new Album("Test Album", "Test Artist", "Test Genre", 2023);
        Song song = new Song("Test Song", album);

        playlist.addSong(song);
        String expected = "Playlist: My Playlist (1 songs)";
        assertEquals(expected, playlist.toString());
    }
}