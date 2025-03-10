package main.view;

import main.database.Album;
import main.database.MusicStore;
import main.database.Song;
import main.model.LibraryModel;
import main.model.Playlist;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/*
    AI-GENERATED SKELETON:
    We used an AI tool to quickly create a basic command-driven structure for this View class,
    including methods for searching the store, adding songs/albums, creating playlists,
    and listing favorites. Afterward, we reviewed and customized it to align with our
    assignment’s requirements, ensuring the View only handles user interaction
    and delegates data operations to the Model.
*/
public class View {

    private final LibraryModel libraryModel;
    private final MusicStore musicStore;
    private final Scanner scanner;

    public View(LibraryModel libraryModel, MusicStore musicStore) {
        this.libraryModel = libraryModel;
        this.musicStore = musicStore;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to Your Music Library!");
        String command;
        do {
            printMenu();
            command = scanner.nextLine().trim().toLowerCase();
            handleCommand(command);
        } while (!command.equals("exit") && !command.equals("14"));
    }

    private void printMenu() {
        System.out.println("\nCommands:");
        System.out.println("  1) searchstore     - Search the store (song/album by title or artist)");
        System.out.println("  2) searchlibrary   - Search songs/albums in your library (title or artist)");
        System.out.println("  3) addsong         - Add a song from the store to your library");
        System.out.println("  4) addalbum        - Add an album from the store to your library");
        System.out.println("  5) createplaylist  - Create a new playlist");
        System.out.println("  6) addtoplaylist   - Add a library song to a playlist");
        System.out.println("  7) favorite        - Mark a library song as favorite");
        System.out.println("  8) rate            - Rate a library song (1-5)");
        System.out.println("  9) listlibrary     - List all songs in your library");
        System.out.println(" 10) listplaylists   - List all playlists");
        System.out.println(" 11) listartists     - List all artists in your library");
        System.out.println(" 12) listalbums      - List all albums in your library");
        System.out.println(" 13) listfavorites   - List favorite songs");
        System.out.println(" 14) exit            - Quit");
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
            case "favorite":
                favoriteSong();
                break;
            case "8":
            case "rate":
                rateSong();
                break;
            case "9":
            case "listlibrary":
                listLibrary();
                break;
            case "10":
            case "listplaylists":
                listPlaylists();
                break;
            case "11":
            case "listartists":
                listArtists();
                break;
            case "12":
            case "listalbums":
                listAlbums();
                break;
            case "13":
            case "listfavorites":
                listFavorites();
                break;
            case "14":
            case "exit":
                System.out.println("Exiting. Goodbye!");
                break;
            default:
                System.out.println("Unrecognized command.");
        }
    }

    private void goBackToMainMenu() {
        System.out.println("\nWhat would you like to do next?");
        System.out.println("  1) Go back to the main menu");
        System.out.println("  2) Exit");
        System.out.print("Enter choice (1 or 2): ");
        String choice = scanner.nextLine().trim();
        if (choice.equals("2")) {
            System.out.println("Exiting. Goodbye!");
            System.exit(0); // Exit the program
        }
        // If choice is 1, the loop in `start()` will return to the main menu
    }

    private void searchStore() {
        System.out.println("Search the store by: ");
        System.out.println(" 1) Song title");
        System.out.println(" 2) Song artist");
        System.out.println(" 3) Album title");
        System.out.println(" 4) Album artist");
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
        System.out.println(" 1) Song title");
        System.out.println(" 2) Song artist");
        System.out.println(" 3) Album title");
        System.out.println(" 4) Album artist");
        System.out.println(" 5) Playlist name");
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
                System.out.println("  - " + s.getTitle()
                        + " by " + s.getAlbum().getArtist());
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
            System.out.println(" - " + s.getTitle() + " by " + s.getAlbum().getArtist()
                    + " (Album: " + s.getAlbum().getTitle() + ")");
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

    public static void main(String[] args) {
        MusicStore store = new MusicStore();
        try {
            store.loadAllAlbums("src/test/albums/albums.txt");
        } catch (IOException e) {
            System.out.println("Could not load albums from store: " + e.getMessage());
        }

        LibraryModel model = new LibraryModel(store);
        View view = new View(model, store);
        view.start();
    }
}
