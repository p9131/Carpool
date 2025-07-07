package com.example.mile.User;

import android.text.Layout;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Entity(tableName = "password")
public class Password {
    @PrimaryKey
    @NonNull
    private String userId;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "isLoggedIn")
    private boolean isLoggedIn;

    public Password() {
    }

    public Password(String userId, String password) {
        this.userId = userId;
        this.password = hashPassword(password);
        this.isLoggedIn=true;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }




    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert bytes to hexadecimal representation
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte hashByte : hashBytes) {
                hexStringBuilder.append(String.format("%02x", hashByte));
            }

            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verifyPassword(String hashedPassword, String inputPassword) {
        String hashedInput = hashPassword(inputPassword);
        Log.d("hasedPass",inputPassword);
        Log.d("hasedPass",hashedPassword);
        return hashedInput != null && hashedInput.equals(hashedPassword);
    }


}
