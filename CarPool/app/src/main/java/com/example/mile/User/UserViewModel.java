package com.example.mile.User;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mile.User.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class UserViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private ListenerRegistration listenerRegistration;

    public LiveData<User> getUserLiveData(String userId) {
        // Remove previous listener if any
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }

        // Set up a new listener for the specified user ID
        listenerRegistration = db.collection("users").document(userId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        // Handle error
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        // Convert the DocumentSnapshot to a User object
                        User user = snapshot.toObject(User.class);
                        userLiveData.setValue(user);
                    } else {
                        // Document does not exist
                        userLiveData.setValue(null);
                    }
                });

        return userLiveData;
    }

    @Override
    protected void onCleared() {
        // Remove the listener when the ViewModel is cleared
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }
}
