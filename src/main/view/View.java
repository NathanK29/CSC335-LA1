package main.view;

import main.auth.User;
import main.auth.UserManager;
import main.database.Album;
import main.database.MusicStore;
import main.database.Song;
import main.model.LibraryModel;
import main.model.Playlist;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class View {

    private MusicStore musicStore;
    private LibraryModel libraryModel;
    private final Scanner scanner;
    private final UserManager userManager;
    private User currentUser;

    public View(MusicStore musicStore, UserManager userManager) {
        this.musicStore = musicStore;
        this.userManager = userManager;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to Your Music Library!");
        boolean authenticated = false;
        while (!authenticated) {
            System.out.println("Please choose an option:");
            System.out.println("  1) Log In");
            System.out.println("  2) Sign Up");
            System.out.println("  3) Exit");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    authenticated = login();
                    break;
                case "2":
                    authenticated = signUp();
                    break;
                case "3":
                    System.out.println("Exiting. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
        libraryModel = currentUser.getLibrary();
        mainMenu();
    }

    private boolean login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        User user = userManager.login(username, password);
        if (user == null) {
            System.out.println("Login failed. Incorrect username or password.");
            return false;
        } else {
            currentUser = user;
            System.out.println("Welcome back, " + currentUser.getUsername() + "!");
            return true;
        }
    }

    // Sign-up flow: log in or create new user (NK)
    private boolean signUp() {
        System.out.print("Choose a username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Choose a password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Confirm password: ");
        String confirmPassword = scanner.nextLine().trim();
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return false;
        }
        boolean success = userManager.signUp(username, password);
        if (!success) {
            System.out.println("Username already exists.");
            return false;
        } else {
            // Automatically log in the new users
            currentUser = userManager.login(username, password);
            System.out.println("Account created. Welcome, " + currentUser.getUsername() + "!");
            return true;
        }
    }

    private void mainMenu() {
        String command;
        do {
            printMenu();
            command = scanner.nextLine().trim().toLowerCase();
            handleCommand(command);
        } while (!command.equals("15") && !command.equals("exit"));
    }

    private void printMenu() {
        System.out.println("\nCommands:");
        System.out.println("  1) searchstore           - Search the store (song/album by title or artist)");
        System.out.println("  2) searchlibrary         - Search songs/albums/playlists in your library (title, artist, or playlist name)");
        System.out.println("  3) addsong               - Add a song from the store to your library");
        System.out.println("  4) addalbum              - Add an album from the store to your library");
        System.out.println("  5) createplaylist        - Create a new playlist");
        System.out.println("  6) addtoplaylist         - Add a library song to a playlist");
        System.out.println("  7) removefromplaylist    - Remove a song from a playlist");
        System.out.println("  8) listplaylists         - List all playlists");
        System.out.println("  9) favorite              - Mark a library song as favorite");
        System.out.println(" 10) rate                  - Rate a library song (1-5)");
        System.out.println(" 11) listlibrary           - List all songs in your library");
        System.out.println(" 12) listartists           - List all artists in your library");
        System.out.println(" 13) listalbums            - List all albums in your library");
        System.out.println(" 14) listfavorites         - List favorite songs");
        System.out.println(" 15) exit                  - Quit");
        System.out.print("Enter command (number or name): ");
    }

    private void handleCommand(String command) {
        switch (command) {
            case "1":
            case "searchstore":
                searchStore();
                break;
            case "2":
            case "searchlibrary":
                searchLibrary();
                break;
            case "3":
            case "addsong":
                addSongFromStore();
                break;
            case "4":
            case "addalbum":
                addAlbumFromStore();
                break;
            case "5":
            case "createplaylist":
                createPlaylist();
                break;
            case "6":
            case "addtoplaylist":
                addSongToPlaylist();
                break;
            case "7":
            case "removefromplaylist":
                removeFromPlaylist();
                break;
            case "8":
            case "listplaylists":
                listPlaylists();
                break;
            case "9":
            case "favorite":
                favoriteSong();
                break;
            case "10":
            case "rate":
                rateSong();
                break;
            case "11":
            case "listlibrary":
                listLibrary();
                break;
            case "12":
            case "listartists":
                listArtists();
                break;
            case "13":
            case "listalbums":
                listAlbums();
                break;
            case "14":
            case "listfavorites":
                listFavorites();
                break;
            case "15":
            case "exit":
                currentUser.saveLibraryData();
                System.out.println("Exiting. Goodbye!");
                break;
            default:
                System.out.println("Unrecognized command.");
        }
    }

    private void searchStore() {
        System.out.println("Search the store by: ");
        System.out.println("  1) Song title");
        System.out.println("  2) Song artist");
        System.out.println("  3) Album title");
        System.out.println("  4) Album artist");
        System.out.print("Enter choice (1-4): ");
        String choice = scanner.nextLine().trim();

        System.out.print("Enter your search string: ");
        String query = scanner.nextLine();

        switch (choice) {
            case "1":
                List<Song> songsByTitle = musicStore.findSongsByTitle(query);
                printSongsFromStore(songsByTitle);
                break;
            case "2":
                List<Song> songsByArtist = musicStore.findSongsByArtist(query);
                printSongsFromStore(songsByArtist);
                break;
            case "3":
                List<Album> albumsByTitle = musicStore.findAlbumsByTitle(query);
                printAlbumsFromStore(albumsByTitle);
                break;
            case "4":
                List<Album> albumsByArtist = musicStore.findAlbumsByArtist(query);
                printAlbumsFromStore(albumsByArtist);
                break;
            default:
                System.out.println("Invalid choice.");
        }
        goBackToMainMenu();
    }

    private void searchLibrary() {
        System.out.println("Search your library by: ");
        System.out.println("  1) Song title");
        System.out.println("  2) Song artist");
        System.out.println("  3) Album title");
        System.out.println("  4) Album artist");
        System.out.println("  5) Playlist name");
        System.out.print("Enter choice (1-5): ");
        String choice = scanner.nextLine().trim();

        System.out.print("Enter your search string: ");
        String query = scanner.nextLine();

        switch (choice) {
            case "1":
                List<Song> songsTitle = libraryModel.searchSongsByTitle(query);
                printLibrarySongs(songsTitle);
                break;
            case "2":
                List<Song> songsArtist = libraryModel.searchSongsByArtist(query);
                printLibrarySongs(songsArtist);
                break;
            case "3":
                List<Album> albumsTitle = libraryModel.searchAlbumsByTitle(query);
                printLibraryAlbums(albumsTitle);
                break;
            case "4":
                List<Album> albumsArtist = libraryModel.searchAlbumsByArtist(query);
                printLibraryAlbums(albumsArtist);
                break;
            case "5":
                Playlist p = libraryModel.findPlaylistByName(query);
                if (p == null) {
                    System.out.println("No playlist found with that name.");
                } else {
                    printPlaylistDetails(p);
                }
                break;
            default:
                System.out.println("Invalid choice.");
        }
        goBackToMainMenu();
    }

    private void addSongFromStore() {
        System.out.print("Enter song title to find in store: ");
        String title = scanner.nextLine();
        List<Song> found = musicStore.findSongsByTitle(title);
        if (found.isEmpty()) {
            System.out.println("No song with that title in the store.");
        } else {
            if (found.size() > 1) {
                System.out.println("Multiple songs found:");
                for (int i = 0; i < found.size(); i++) {
                    Song s = found.get(i);
                    System.out.println(i + ") " + s.getTitle() + " by " + s.getAlbum().getArtist()
                            + " (Album: " + s.getAlbum().getTitle() + ")");
                }
                System.out.print("Choose a song index: ");
                int index = Integer.parseInt(scanner.nextLine());
                if (index < 0 || index >= found.size()) {
                    System.out.println("Invalid choice.");
                } else {
                    Song chosen = found.get(index);
                    libraryModel.addSongToLibrary(chosen);
                    System.out.println("Added '" + chosen.getTitle() + "' to your library.");
                }
            } else {
                Song chosen = found.get(0);
                libraryModel.addSongToLibrary(chosen);
                System.out.println("Added '" + chosen.getTitle() + "' to your library.");
            }
        }
        goBackToMainMenu();
    }

    private void addAlbumFromStore() {
        System.out.print("Enter album title to find in store: ");
        String title = scanner.nextLine();
        List<Album> found = musicStore.findAlbumsByTitle(title);
        if (found.isEmpty()) {
            System.out.println("No album with that title in the store.");
        } else {
            if (found.size() > 1) {
                System.out.println("Multiple albums found:");
                for (int i = 0; i < found.size(); i++) {
                    Album a = found.get(i);
                    System.out.println(i + ") " + a.getTitle() + " by " + a.getArtist());
                }
                System.out.print("Choose an album index: ");
                int index = Integer.parseInt(scanner.nextLine());
                if (index < 0 || index >= found.size()) {
                    System.out.println("Invalid choice.");
                } else {
                    Album chosen = found.get(index);
                    libraryModel.addAlbumToLibrary(chosen);
                    System.out.println("Added album '" + chosen.getTitle() + "' to your library.");
                }
            } else {
                Album chosen = found.get(0);
                libraryModel.addAlbumToLibrary(chosen);
                System.out.println("Added album '" + chosen.getTitle() + "' to your library.");
            }
        }
        goBackToMainMenu();
    }

    private void createPlaylist() {
        System.out.print("Enter playlist name: ");
        String playlistName = scanner.nextLine().trim();

        boolean success = libraryModel.createPlaylist(playlistName);
        if (!success) {
            System.out.println("This playlist already exists. Please choose a different name.");
        } else {
            System.out.println("Created playlist: " + playlistName);
        }
        goBackToMainMenu();
    }

    private void addSongToPlaylist() {
        System.out.print("Enter playlist name: ");
        String playlistName = scanner.nextLine();
        System.out.print("Enter song title (must already be in library): ");
        String songTitle = scanner.nextLine();
        Song found = pickSongFromLibrary(songTitle);
        if (found == null) {
            System.out.println("That song isn't in your library.");
        } else {
            libraryModel.addSongToPlaylist(playlistName, found);
            System.out.println("Added '" + found.getTitle() + "' to playlist '" + playlistName + "'.");
        }
        goBackToMainMenu();
    }

    private void removeFromPlaylist() {
        System.out.print("Enter playlist name: ");
        String playlistName = scanner.nextLine().trim();
        if (libraryModel.findPlaylistByName(playlistName) == null) {
            System.out.println("No playlist found with that name.");
            goBackToMainMenu();
            return;
        }
        System.out.print("Enter song title to remove: ");
        String songTitle = scanner.nextLine();
        Song found = pickSongFromLibrary(songTitle);
        if (found == null) {
            System.out.println("That song isn't in your library.");
        } else {
            libraryModel.removeSongFromPlaylist(playlistName, found);
            System.out.println("Removed '" + found.getTitle() + "' from playlist '" + playlistName + "'.");
        }
        goBackToMainMenu();
    }

    private void favoriteSong() {
        System.out.print("Enter song title to favorite: ");
        String title = scanner.nextLine();
        Song found = pickSongFromLibrary(title);
        if (found == null) {
            System.out.println("Song not found in your library.");
        } else {
            libraryModel.markSongAsFavorite(found);
            System.out.println("Marked '" + found.getTitle() + "' as favorite.");
        }
        goBackToMainMenu();
    }

    private void rateSong() {
        System.out.print("Enter song title to rate: ");
        String title = scanner.nextLine();
        Song found = pickSongFromLibrary(title);
        if (found == null) {
            System.out.println("Song not found in your library.");
        } else {
            System.out.print("Enter rating (1-5): ");
            int rating;
            try {
                rating = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
                return;
            }
            try {
                libraryModel.rateSong(found, rating);
                System.out.println("Rated '" + found.getTitle() + "' with " + rating + ".");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        goBackToMainMenu();
    }

    private void listLibrary() {
        List<Song> songs = libraryModel.getAllSongs();
        if (songs.isEmpty()) {
            System.out.println("Your library is empty.");
        } else {
            System.out.println("Your library songs:");
            for (Song s : songs) {
                System.out.println(" - " + s.getTitle() + " (Album: " + s.getAlbum().getTitle() + ")");
            }
        }
        goBackToMainMenu();
    }

    private void listArtists() {
        List<String> artists = libraryModel.getAllArtistsInLibrary();
        if (artists.isEmpty()) {
            System.out.println("No artists in your library.");
        } else {
            System.out.println("All artists in your library:");
            for (String artist : artists) {
                System.out.println(" - " + artist);
            }
        }
        goBackToMainMenu();
    }

    private void listAlbums() {
        List<Album> albums = libraryModel.getAllAlbumsInLibrary();
        if (albums.isEmpty()) {
            System.out.println("No albums in your library.");
        } else {
            System.out.println("All albums in your library:");
            for (Album alb : albums) {
                System.out.println(" - " + alb.getTitle() + " by " + alb.getArtist());
            }
        }
        goBackToMainMenu();
    }

    private void listPlaylists() {
        List<Playlist> pls = libraryModel.getAllPlaylists();
        if (pls.isEmpty()) {
            System.out.println("No playlists created yet.");
        } else {
            System.out.println("Your playlists:");
            for (Playlist p : pls) {
                System.out.println(" - " + p.getName() + " (" + p.getSongs().size() + " songs)");
            }
        }
        goBackToMainMenu();
    }

    private void listFavorites() {
        List<Song> favs = libraryModel.getFavoriteSongs();
        if (favs.isEmpty()) {
            System.out.println("No favorite songs yet.");
        } else {
            System.out.println("Favorite songs:");
            for (Song s : favs) {
                System.out.println(" - " + s.getTitle() + " (Album: " + s.getAlbum().getTitle() + ")");
            }
        }
        goBackToMainMenu();
    }

    private Song pickSongFromLibrary(String title) {
        List<Song> found = libraryModel.searchSongsByTitle(title);
        return found.isEmpty() ? null : found.get(0);
    }

    private void printPlaylistDetails(Playlist p) {
        System.out.println("Playlist: " + p.getName());
        List<Song> songs = p.getSongs();
        if (songs.isEmpty()) {
            System.out.println("  (No songs in this playlist.)");
        } else {
            for (Song s : songs) {
                System.out.println("  - " + s.getTitle() + " by " + s.getAlbum().getArtist());
            }
        }
    }

    private void printSongsFromStore(List<Song> songs) {
        if (songs.isEmpty()) {
            System.out.println("No matching songs found in the store.");
            return;
        }
        System.out.println("Songs in store:");
        for (Song s : songs) {
            System.out.println(" - " + s.getTitle() + " by " + s.getAlbum().getArtist() +
                    " (Album: " + s.getAlbum().getTitle() + ")");
        }
    }

    private void printAlbumsFromStore(List<Album> albums) {
        if (albums.isEmpty()) {
            System.out.println("No matching albums found in the store.");
            return;
        }
        System.out.println("Albums in store:");
        for (Album a : albums) {
            System.out.println(" - " + a.getTitle() + " by " + a.getArtist());
            System.out.println("   Tracks:");
            for (Song s : a.getSongs()) {
                System.out.println("     * " + s.getTitle());
            }
        }
    }

    private void printLibrarySongs(List<Song> songs) {
        if (songs.isEmpty()) {
            System.out.println("No matching songs found in your library.");
            return;
        }
        System.out.println("Songs in your library:");
        for (Song s : songs) {
            System.out.println(" - " + s.getTitle() + " (Album: " + s.getAlbum().getTitle() + ")");
        }
    }

    private void printLibraryAlbums(List<Album> albums) {
        if (albums.isEmpty()) {
            System.out.println("No matching albums found in your library.");
            return;
        }
        System.out.println("Albums in your library:");
        for (Album a : albums) {
            System.out.println(" - " + a.getTitle() + " by " + a.getArtist());
        }
    }

    private void goBackToMainMenu() {
        System.out.println("\nWhat would you like to do next?");
        System.out.println("  1) Go back to the main menu");
        System.out.println("  2) Exit");
        System.out.print("Enter choice (1 or 2): ");
        String choice = scanner.nextLine().trim();
        if (choice.equals("2")) {
            currentUser.saveLibraryData();
            System.out.println("Exiting. Goodbye!");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        MusicStore store = new MusicStore();
        try {
            store.loadAllAlbums("src/test/albums/albums.txt");
        } catch (IOException e) {
            System.out.println("Could not load albums from store: " + e.getMessage());
        }

        UserManager userManager = new UserManager(store);
        View view = new View(store, userManager);
        view.start();
    }
}
