package com.example.mile.history;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mile.FirebaseHelp;
import com.example.mile.R;
import com.example.mile.Route.RouteItem;
import com.example.mile.Route.RoutesViewModel;
import com.example.mile.Tracking_Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link history#newInstance} factory method to
 * create an instance of this fragment.
 */
public class history extends Fragment implements Listener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public List<RouteItem> routes;
    FirebaseHelp db= FirebaseHelp.getInstance();
    public RoutesViewModel routesViewModel;

    public history() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment history.
     */
    // TODO: Rename and change types and number of parameters
    public static history newInstance(String param1, String param2) {
        history fragment = new history();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView recyclerView = view.findViewById(R.id.historyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        HistoryAdapter adapter = new HistoryAdapter((new ArrayList<>()),this);
        recyclerView.setAdapter(adapter);

        routesViewModel = new ViewModelProvider(this).get(RoutesViewModel.class);

        routesViewModel.getRoutesLiveData().observe(getViewLifecycleOwner(), routeList -> {
            routes=routeList;
            adapter.setRoutesData(filterRoutesList(db.getCurrentUseremail()));
            recyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void onClicked(RouteItem route) {
        Intent intent = new Intent(getActivity(), Tracking_Page.class);
        intent.putExtra("time",route.getStartTime());
        intent.putExtra("start",route.getStartPoint());
        intent.putExtra("destination",route.getdestination());
        startActivity(intent);
    }

    public List<RouteItem> filterRoutesList(String userEmail) {
        List<RouteItem> filteredRoutes = routes.stream()
                .filter(routeItem -> routeItem.getuserRequestStatusMap().containsKey(userEmail))
                .filter(routeItem -> {
                    String status = routeItem.getuserRequestStatusMap().get(userEmail);
                    assert status != null;
                    return status.equals("Paid");
                })
                .collect(Collectors.toList());
        return filteredRoutes;
    }
}

