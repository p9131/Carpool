package com.example.mile.Route;

import android.util.Log;

        import androidx.lifecycle.LiveData;
        import androidx.lifecycle.MutableLiveData;

import com.example.mile.Route.RouteItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
        import com.google.firebase.firestore.QuerySnapshot;

        import java.util.ArrayList;
        import java.util.List;
import java.util.Set;

public class RouteRepo {

    private static final String TAG = "RouteRepository";
    private ListenerRegistration listenerRegistration;

    FirebaseFirestore db;

    public RouteRepo() {
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
    }


    public MutableLiveData<List<RouteItem>> getAllDriversRoutes() {
        final MutableLiveData<List<RouteItem>> routesLiveData = new MutableLiveData<>();


        db.collection("routes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e(TAG, "Error getting routes for the driver", error);
                            return;
                        }

                        List<RouteItem> routeList = new ArrayList<>();
                        for (QueryDocumentSnapshot routeDocument : value) {
                            RouteItem routeItem = routeDocument.toObject(RouteItem.class);
                            routeList.add(routeItem);
                        }
                        routesLiveData.setValue(routeList);
                    }
                });
        return routesLiveData;
    }



}
