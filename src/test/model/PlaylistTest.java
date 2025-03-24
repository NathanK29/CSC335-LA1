package test.model;

import main.model.Playlist;
import main.database.Song;
import main.database.Album;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void testPlaylistShuffle() {
        Playlist playlist = new Playlist("Shuffle Test");
        Album album = new Album("Shuffle Album", "Shuffle Artist", "Genre", 2023);
        Song s1 = new Song("Song 1", album);
        Song s2 = new Song("Song 2", album);
        Song s3 = new Song("Song 3", album);
        playlist.addSong(s1);
        playlist.addSong(s2);
        playlist.addSong(s3);
        Iterable<Song> shuffledIterable = playlist.getShuffledSongs();
        List<Song> shuffled = new ArrayList<>();
        for (Song s : shuffledIterable) {
            shuffled.add(s);
        }
        assertEquals(3, shuffled.size());
        assertTrue(shuffled.contains(s1));
        assertTrue(shuffled.contains(s2));
        assertTrue(shuffled.contains(s3));
    }
}