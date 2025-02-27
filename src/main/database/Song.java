package main.database;

public class Song {
    // This is all that's needed, the rest of the info can be retrieved from referencing the songs album
    private String title;
    private Album album;

    public Song(String title, Album album) {
        this.title = title;
        this.album = album;
    }

    public String getTitle() {
        return this.title;
    }

    public Album getAlbum() {
        return this.album;
    }
}
