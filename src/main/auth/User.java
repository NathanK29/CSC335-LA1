package main.auth;

import main.model.LibraryModel;
import main.database.MusicStore;

public class User {
    private String username;
    private String salt;
    private String hashedPassword;
    private LibraryModel library;

    public User(String username, String salt, String hashedPassword, MusicStore musicStore) {
        this.username = username;
        this.salt = salt;
        this.hashedPassword = hashedPassword;
        this.library = new LibraryModel(musicStore);
    }

    public String getUsername() {
        return username;
    }

    public String getSalt() {
        return salt;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public LibraryModel getLibrary() {
        return library;
    }
}
