package main.database;

import java.io.Serializable;

public class Song implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private Album album;
    private int rating;
    private boolean isFavorite;

    public Song(String title, Album album) {
        this.title = title;
        this.album = album;
        this.isFavorite = false;
    }

    public String getTitle() {
        return this.title;
    }

    public Album getAlbum() {
        return this.album;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
            if (rating == 5) {
                this.isFavorite = true;
            }
        } else {
            throw new IllegalArgumentException("Rating must be between 1-5.");
        }
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
