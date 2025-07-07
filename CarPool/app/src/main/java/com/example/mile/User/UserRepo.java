package com.example.mile.User;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepo {
    private UserDao userDao;
    private static final Executor databaseReadExecutor = Executors.newSingleThreadExecutor();

    private UserRepo(UserDao userDao) {
        this.userDao = userDao;
    }
    private static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(1);
    public UserRepo(Context context) {
        UserDatabase database = UserDatabase.getInstance(context);
        userDao = database.userDao();
    }

    public static UserRepo getInstance(UserDao userDao) {
        return new UserRepo(userDao);
    }

    public User getUserById(String userId) {
        return userDao.getUserById(userId);
    }

    public void insert(User user) {
        databaseWriteExecutor.execute(() -> userDao.insert(user));
    }

    public void insertPassword(Password password) {
        databaseWriteExecutor.execute(() -> userDao.insertPassword(password));
    }
    public interface GetUserCallback {
        void onUserLoaded(User user, Password existingPass);
    }

    public interface GetPassCallback {
        void onReturn(boolean value,User user);
    }
    public void login( User newUser, String password,GetUserCallback callback) {
        databaseReadExecutor.execute(() -> {
            User existingUser = userDao.getUserById(newUser.getUserId());
            Password existingPass = userDao.getPasswordById(newUser.getUserId());


        if (existingUser == null) {
            insert(newUser);
            existingPass=new Password(newUser.getUserId(),password);
            insertPassword(existingPass);
        }
        else {
        }
        callback.onUserLoaded(newUser,existingPass);


        });
    }
public boolean v;
    public void verifyUserCredentials(String email, String password,GetPassCallback callback) {
        databaseReadExecutor.execute(() -> {
        User user = userDao.getUserByEmail(email);

        if (user != null) {
            Password storedPassword = userDao.getPasswordById(user.getUserId());
            if (storedPassword != null) {
                v= Password.verifyPassword(storedPassword.getPassword(), password);
            }
        }
            callback.onReturn(v,user);
        });
    }

}
