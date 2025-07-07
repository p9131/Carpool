package com.example.mile.User;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "user")
public class User implements Serializable {
    @PrimaryKey
    @NonNull
    private String userId;
    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name="email")
    private String email;
    @ColumnInfo(name="isStudent")
    private boolean isStudent;


    public User() {
    }

    public User(String userId, String username, String email,boolean isStudent) {
        this.userId = userId;
        this.name = username;
        this.email = email;
        this.isStudent = isStudent;
    }


    public Map<String, Object> toMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userId", userId);
        userMap.put("name", name);
        userMap.put("email", email);
        userMap.put("isStudent", isStudent);

        return userMap;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @PropertyName(value="isStudent")
    public boolean isStudent() {
        return this.isStudent;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @PropertyName(value="isStudent")
    public void setStudent(boolean value) {
        this.isStudent = value;
    }

}