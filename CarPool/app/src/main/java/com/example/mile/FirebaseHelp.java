package com.example.mile;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mile.Route.RouteItem;
import com.example.mile.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FirebaseHelp {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static FirebaseAuth auth = FirebaseAuth.getInstance();
    User user;
    static FirebaseUser firebaseUser;
    private static FirebaseHelp instance;

    private FirebaseHelp() {
//        this.setUser();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
    }


    // Public static method to get the singleton instance
    public static FirebaseHelp getInstance() {
        // Lazy initialization: create the instance if it hasn't been created yet
        if (instance == null) {
            instance = new FirebaseHelp();
        }
        firebaseUser = auth.getCurrentUser();
        return instance;
    }
    static void set(FirebaseUser user){
        firebaseUser=user;
    }
    void addUser(User user){
        DocumentReference userRef = db.collection("users").document(user.getUserId());
        userRef.set(user)
                .addOnSuccessListener(aVoid -> Log.d("AddingUser", "DocumentSnapshot added with ID: " + userRef.getId()))
                .addOnFailureListener(e -> Log.w("AddingUser", "Error adding document", e));
    }

    public String getCurrentUserId(){
        return firebaseUser.getUid();
    }


    void addDriver(User user){
        DocumentReference userRef = db.collection("drivers").document(user.getUserId());

        userRef.set(user.toMap())
                .addOnSuccessListener(aVoid -> Log.d("AddUserSuccess", "DocumentSnapshot added with ID: " + userRef.getId()))
                .addOnFailureListener(e -> Log.w("AddUserError", "Error adding document", e));
    }

    public void addRoute(RouteItem routeItem){
        db.collection("routes")
                .document(routeItem.getid())
                .set(routeItem)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                });
    }

    public void updateUserStatus(String routeId, String userEmail, String userStatus) {
        db.runTransaction(transaction -> {
            DocumentReference routeRef = db.collection("routes").document(routeId);

            // Retrieve the current userRequestStatusMap
            DocumentSnapshot snapshot = transaction.get(routeRef);
            Map<String, String> userRequestStatusMap = snapshot.contains("userRequestStatusMap") ?
                    (Map<String, String>) snapshot.getData().get("userRequestStatusMap") : new HashMap<>();

            // Add or update the status for the user
            userRequestStatusMap.put(userEmail, userStatus);

            // Update the userRequestStatusMap in the document
            transaction.update(routeRef, "userRequestStatusMap", userRequestStatusMap);

            return null;
        }).addOnSuccessListener(result -> {
            Log.d("TAG", "User status updated successfully for user " + userEmail);
        }).addOnFailureListener(e -> {
            Log.e("TAG", "Error updating user status", e);
        });
    }



    CompletableFuture<User> getUserById(String type) {
        String userId= auth.getUid();
        return getUserById(userId,type);
    }



    CompletableFuture<User> getUserById(String userId,String type) {
        CompletableFuture<User> future = new CompletableFuture<>();

        DocumentReference userRef = db.collection(type).document(userId);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("FirestoreData", "DocumentSnapshot data: " + document.getData());
                        User user = document.toObject(User.class);
                        if (user != null) {
                            // Resolve the CompletableFuture with the user object
                            future.complete(user);
                        } else {
                            Log.w("RetrieveUser", "Error deserializing User");
                            // Complete exceptionally if user is null
                            future.completeExceptionally(new RuntimeException("Error deserializing User"));
                        }
                    } else {
                        Log.d("RetrieveUser", "No such document");
                        // Complete exceptionally if document doesn't exist
                        future.completeExceptionally(new RuntimeException("No such document"));
                    }
                } else {
                    Log.w("RetrieveUser", "Error getting document", task.getException());
                    // Complete exceptionally with the task exception
                    future.completeExceptionally(task.getException());
                }
            }
        });

        return future;

    }


    public CompletableFuture<List<RouteItem>> getAllDriversRoutes() {
        CompletableFuture<List<RouteItem>> future = new CompletableFuture<>();

        db.collection("routes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<RouteItem> allRoutes = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("routes")
                                        .document(document.getId())
                                        .collection("driverRoutes")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot routeDocument : task.getResult()) {
                                                        RouteItem routeItem = routeDocument.toObject(RouteItem.class);
                                                        allRoutes.add(routeItem);
                                                    }
                                                    // Check if this is the last driver
                                                    if (allRoutes.size() == task.getResult().size()) {
                                                        // Resolve the CompletableFuture with all routes
                                                        future.complete(allRoutes);
                                                    }
                                                } else {
                                                    Log.e("TAG", "Error getting routes for driver: " + document.getId(), task.getException());
                                                    // Complete exceptionally with the task exception
                                                    future.completeExceptionally(task.getException());
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.e("TAG", "Error getting all drivers", task.getException());
                            // Complete exceptionally with the task exception
                            future.completeExceptionally(task.getException());
                        }
                    }
                });

        return future;
    }
    void signOut(){
        auth.signOut();
    }


    public String getCurrentUseremail(){
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String email=firebaseUser.getEmail();
        return email;
    }
    public void setUser(){
        String type="drivers";
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String email=firebaseUser.getEmail();
        if (isValidScholarEmail(email)){
            type="users";
        }
        getUserById(type).thenAccept(user -> {
            // Handle the user object here
            if (user != null) {
                this.user=user;
            } else {
            }
        }).exceptionally(ex -> {
            // Handle exceptions here
            Log.e("Get user profile", "Error getting user", ex);
            return null; // You can return a default value or handle it as needed
        });
    }

    private boolean isValidScholarEmail(String email) {
        String emailRegex = "^[0-9]{2}p[0-9]{4}@eng\\.asu\\.edu\\.eg$";

        int currentYear = (Calendar.getInstance().get(Calendar.YEAR))%100;

        if (email.matches(emailRegex)) {
            int enrollmentYear = Integer.parseInt(email.substring(0, 2));
            int oldestYear = currentYear-10;
            return enrollmentYear <= currentYear && enrollmentYear >= oldestYear;
        }

        return false;
    }



        public static boolean isNetworkConnected(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            }
            return false;
        }


    public void changeStatus(String tripId, String user, String newStatus) {
        DocumentReference routeRef = db.collection("routes").document(tripId);

        routeRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve the RouteItem object
                        RouteItem routeItem = documentSnapshot.toObject(RouteItem.class);

                        // Update the status in the userRequestStatusMap
                        if (routeItem != null) {
                            routeItem.getuserRequestStatusMap().put(user, newStatus);

                            // Update the document in Firestore
                            routeRef.set(routeItem)
                                    .addOnSuccessListener(aVoid -> Log.d("ChangeStatus", "Status updated successfully"))
                                    .addOnFailureListener(e -> Log.w("ChangeStatus", "Error updating status", e));
                        }
                    } else {
                        Log.d("ChangeStatus", "Route document does not exist");
                    }
                })
                .addOnFailureListener(e -> Log.w("ChangeStatus", "Error retrieving route document", e));
    }


    public void removeUserStatus(String tripId, String user) {
        DocumentReference routeRef = db.collection("routes").document(tripId);

        routeRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve the RouteItem object
                        RouteItem routeItem = documentSnapshot.toObject(RouteItem.class);

                        // Remove the entry for the specified user from the userRequestStatusMap
                        if (routeItem != null) {
                            routeItem.getuserRequestStatusMap().remove(user);

                            // Update the document in Firestore
                            routeRef.set(routeItem)
                                    .addOnSuccessListener(aVoid -> Log.d("RemoveUserStatus", "User status removed successfully"))
                                    .addOnFailureListener(e -> Log.w("RemoveUserStatus", "Error removing user status", e));
                        }
                    } else {
                        Log.d("RemoveUserStatus", "Route document does not exist");
                    }
                })
                .addOnFailureListener(e -> Log.w("RemoveUserStatus", "Error retrieving route document", e));
    }

    public void pay(String userEmail){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference routesCollection = db.collection("routes");

        // Fetch all documents from the "routes" collection
        routesCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Convert the document data to a RouteItem object
                        RouteItem routeItem = document.toObject(RouteItem.class);

                        // Check if the userRequestStatusMap contains the specified email
                        if (routeItem.getuserRequestStatusMap().containsKey(userEmail)) {
                            // Update the status to "Paid"
                            routeItem.getuserRequestStatusMap().put(userEmail, "Paid");

                            // Save the updated routeItem back to Firestore
                            routesCollection.document(document.getId()).set(routeItem);
                        }
                    }
                } else {
                    // Handle the error
                    Exception exception = task.getException();
                    if (exception != null) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }
}


