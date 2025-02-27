package main.database;

public class Song {
    // This is all that's needed, the rest of the info can be retrieved from referencing the songs album
    private String title;
    private Album album;
    private int rating; // Rating from 1-5 (0 means unrated)(A.D)
    private boolean isFavorite;
   
    public Song(String title, Album album) {
        this.title = title;
        this.album = album;
        this.isFavorite = false; // Default to not a favorite (A.D)
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
            // Automatically mark as favorite if rating is 5 (A.D)
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
