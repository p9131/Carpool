package com.example.mile.User;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mile.User.User;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPassword(Password password);


    @Query("SELECT * FROM user WHERE userId = :userId")
    User getUserById(String userId);

    @Query("SELECT * FROM user")
    User getUsers();

    @Query("SELECT * FROM password WHERE userId = :userId")
    Password getPasswordById(String userId);

    @Query("SELECT * FROM user WHERE email = :email")
    User getUserByEmail(String email);



}
