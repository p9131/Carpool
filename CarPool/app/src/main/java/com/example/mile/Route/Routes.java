package com.example.mile.Route;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mile.FirebaseHelp;
import com.example.mile.R;
import com.example.mile.User.User;
import com.example.mile.User.UserViewModel;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Routes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Routes extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseHelp db= FirebaseHelp.getInstance();
    RouteAdapter adapter;
    RoutesViewModel routesViewModel;
    private static final String DATE_FORMAT_PATTERN = "EEE MMM dd yyyy HH:mm";
    private static final String TIME_FORMAT_PATTERN = "HH:mm";

    public Routes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Routes.
     */
    // TODO: Rename and change types and number of parameters
    public static Routes newInstance(String param1, String param2) {
        Routes fragment = new Routes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private RoutesViewModel model1;

    public TextView nameTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }


    public void onItemClick(RouteItem routeItem) {
        db.updateUserStatus(routeItem.getid(), db.getCurrentUseremail(), "Pending");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        isSpinnerLeft =false;
        if (getActivity() != null) {
            sharedPreferences = getActivity().getSharedPreferences("isBypass", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }

        return inflater.inflate(R.layout.fragment_routes, container, false);
    }
    Spinner spinner;
    TextView textView6;
    TextView i;

    public boolean isSpinnerLeft;
    ImageButton imageButton;

    private UserViewModel userViewModel;
    List<RouteItem> myroutes;
    public String selectedValue;
    public Button toggleBypass;
    public static boolean isBypass;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.routes);
        toggleBypass = view.findViewById(R.id.bypass);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        isBypass=sharedPreferences.getBoolean("isBypass",false);
        editor.putBoolean("isBypass", isBypass);
        editor.apply();

        routesViewModel = new ViewModelProvider(this).get(RoutesViewModel.class);
        adapter = new RouteAdapter(new ArrayList<>(),this::onItemClick); // Initialize with an empty list
        recyclerView.setAdapter(adapter);

        toggleBypass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBypass= !isBypass;
                editor.putBoolean("isBypass", isBypass);
                editor.apply();
                adapter.setRoutesData(filterRoutesList(myroutes));
                recyclerView.setAdapter(adapter);
            }
        });


        routesViewModel.getRoutesLiveData().observe(getViewLifecycleOwner(), routeList -> {
            myroutes=routeList;
            adapter.setRoutesData(filterRoutesList(routeList));
            recyclerView.setAdapter(adapter);
        });

        textView6 = view.findViewById(R.id.textView6);
        imageButton =view.findViewById(R.id.imageButton);
        i =view.findViewById(R.id.allRoutes);


        spinner = view.findViewById(R.id.spinner);


        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.places,android.R.layout.simple_spinner_item);

        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedValue = (String) spinner.getSelectedItem();
                adapter.setRoutesData(filterRoutesList(myroutes));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                isSpinnerLeft = !isSpinnerLeft;
                updateViews(view);
                adapter.setRoutesData(filterRoutesList(myroutes));
                recyclerView.setAdapter(adapter);
            }
        });

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Observe the LiveData for a specific user (replace "yourUserId" with the actual user ID)
        userViewModel.getUserLiveData(db.getCurrentUserId()).observe(getViewLifecycleOwner(), new Observer<User>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(User user) {
                // Update UI based on the user data
                if (user != null) {
                    i.setText("Welcome " + user.getName());
                } else {
                    i.setText("Welcome");
                }
            }
        });
    }


    public List<RouteItem> filterRoutesList(List<RouteItem> routes) {
        List<RouteItem> filteredRoutes = routes.stream()
                .filter(routeItem -> {
                    if(isSpinnerLeft){
                        return routeItem.getStartPoint().equals(selectedValue);
                    }
                    else {
                        return routeItem.getdestination().equals(selectedValue);
                    }
                })
                .filter(Routes::isReservationValid)
                .collect(Collectors.toList());
        return filteredRoutes;
    }

    private void updateViews(View view) {
        // Get the parent layouts
        LinearLayout left = view.findViewById(R.id.fromLayout);
        LinearLayout right = view.findViewById(R.id.toLayout);

        int index = left.indexOfChild(textView6);
        if (index != -1) {
            left.removeView(textView6);
            right.removeView(spinner);
            isSpinnerLeft=true;
        } else {
            left.removeView(spinner);
            right.removeView(textView6);
            isSpinnerLeft=false;
        }

        // Update the layout parameters for each view
        LinearLayout.LayoutParams paramsTextView6 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                isSpinnerLeft ?  1.0f:0.7f
        );

        LinearLayout.LayoutParams paramsSpinner = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                isSpinnerLeft ? 0.7f:1.0f
        );



        // Apply the layout parameters to the views
        textView6.setLayoutParams(paramsTextView6);
        spinner.setLayoutParams(paramsSpinner);

        // Add views back to their parent layouts
        left.addView(isSpinnerLeft ? spinner : textView6);
        right.addView(isSpinnerLeft ? textView6 : spinner);

    }

    private static boolean isReservationValid(RouteItem route) {
        try {
            // Parse the route start time
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.US);
            Date startTime = dateFormat.parse(route.getStartTime());

            // Get the current time
            Calendar currentTime = Calendar.getInstance();
            Date now = currentTime.getTime();

            // Set reservation cutoff times based on ride type
            Calendar cutoffTime = Calendar.getInstance();

            if (route.getStartTime().contains("07:00")) {
                cutoffTime.setTimeInMillis(startTime.getTime());
                cutoffTime.set(Calendar.HOUR_OF_DAY, 22);
                cutoffTime.set(Calendar.MINUTE, 0);
                int cutoffDay = cutoffTime.get(Calendar.DAY_OF_MONTH);
                cutoffTime.set(Calendar.DAY_OF_MONTH,cutoffDay-1);
            } else if (route.getStartTime().contains("17:30")) {
                cutoffTime.setTimeInMillis(startTime.getTime());
                cutoffTime.set(Calendar.HOUR_OF_DAY, 13);
                cutoffTime.set(Calendar.MINUTE, 0);
            }

            return (now.before(cutoffTime.getTime()) && now.before(startTime))||isBypass;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }




}

