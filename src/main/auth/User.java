package main.auth;

import main.model.LibraryModel;
import main.database.MusicStore;
import java.io.*;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String salt;
    private String hashedPassword;
    private LibraryModel library;

    public User(String username, String salt, String hashedPassword, MusicStore musicStore) {
        this.username = username;
        this.salt = salt;
        this.hashedPassword = hashedPassword;
        LibraryModel loadedLibrary = loadLibraryData(musicStore);
        if (loadedLibrary != null) {
            this.library = loadedLibrary;
        } else {
            this.library = new LibraryModel(musicStore);
        }
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

    public void saveLibraryData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("library_" + username + ".ser"))) {
            oos.writeObject(library);
        } catch (IOException e) {
            System.err.println("Error saving library data for user " + username + ": " + e.getMessage());
        }
    }

    private LibraryModel loadLibraryData(MusicStore musicStore) {
        File file = new File("library_" + username + ".ser");
        if (!file.exists()) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            LibraryModel loaded = (LibraryModel) ois.readObject();
            loaded.setMusicStore(musicStore);
            return loaded;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading library data for user " + username + ": " + e.getMessage());
            return null;
        }
    }
}
