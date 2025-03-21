package main.auth;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.Base64;

import main.database.MusicStore;

public class UserManager {
    private Map<String, User> users;
    private MusicStore musicStore;
    private final String userFilePath = "users.txt";

    public UserManager(MusicStore musicStore) {
        this.musicStore = musicStore;
        users = new HashMap<>();
        loadUsers();
    }

    private String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((salt + password).getBytes());
            byte[] hashedBytes = md.digest();
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    public boolean signUp(String username, String password) {
        if(users.containsKey(username)) {
            return false;
        }
        String salt = generateSalt();
        String hashed = hashPassword(password, salt);
        User newUser = new User(username, salt, hashed, musicStore);
        users.put(username, newUser);
        saveUsers();
        return true;
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if(user == null) {
            return null;
        }
        String hashed = hashPassword(password, user.getSalt());
        if(hashed.equals(user.getHashedPassword())) {
            return user;
        }
        return null;
    }

    private void loadUsers() {
        File file = new File(userFilePath);
        if(!file.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts.length != 3) continue;
                String username = parts[0];
                String salt = parts[1];
                String hashed = parts[2];
                User user = new User(username, salt, hashed, musicStore);
                users.put(username, user);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFilePath))) {
            for(User user : users.values()) {
                String line = user.getUsername() + "," + user.getSalt() + "," + user.getHashedPassword();
                bw.write(line);
                bw.newLine();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
