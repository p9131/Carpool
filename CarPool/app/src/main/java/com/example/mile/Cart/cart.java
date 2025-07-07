package com.example.mile.Cart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mile.FirebaseHelp;
import com.example.mile.R;
import com.example.mile.Route.RouteItem;
import com.example.mile.Route.RoutesViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link cart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cart extends Fragment implements CartAdapter.OnRemoveButtonClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public List<RouteItem> routes;
    public List<RouteItem> filteredList;
    FirebaseHelp db= FirebaseHelp.getInstance();
    Button pay;
    TextView price;

    public Map<String, String> userRoutes;

    public cart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cart.
     */
    // TODO: Rename and change types and number of parameters
    public static cart newInstance(String param1, String param2) {
        cart fragment = new cart();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public void onRemoveButtonClick(int position, RouteItem routeItem) {
        db.removeUserStatus(routeItem.getid(), db.getCurrentUseremail());
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
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }
    public RoutesViewModel routesViewModel;


    public Map<String, String> extractRoutesForUser(List<RouteItem> routeList) {
        String userEmail=db.getCurrentUseremail();
        Map<String, String> userRoutes = new HashMap<>();

        for (RouteItem routeItem : routeList) {
            String routeId = routeItem.getid();
            String status = routeItem.getuserRequestStatusMap().get(userEmail);

            if (status != null) {
                userRoutes.put(routeId, status);
            }
        }
        return userRoutes;
    }
    public List<RouteItem> filterRoutesList(String userEmail) {
        List<RouteItem> filteredRoutes = routes.stream()
                .filter(routeItem -> routeItem.getuserRequestStatusMap().containsKey(userEmail))
                .filter(routeItem -> {
                    String status = routeItem.getuserRequestStatusMap().get(userEmail);
                    assert status != null;
                    return !(status.equals("Paid"));
                })
                .collect(Collectors.toList());
        return filteredRoutes;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String Txt = "Total price = $ ";

        pay=view.findViewById(R.id.payBtn);
        price=view.findViewById(R.id.priceTxt);
        String userEmail=db.getCurrentUseremail();
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.pay(userEmail);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.cartList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        CartAdapter adapter = new CartAdapter((new ArrayList<>()));
        adapter.setOnRemoveButtonClickListener(this);
        recyclerView.setAdapter(adapter);


        routesViewModel = new ViewModelProvider(this).get(RoutesViewModel.class);

        routesViewModel.getRoutesLiveData().observe(getViewLifecycleOwner(), routeList -> {
            routes=routeList;
            filteredList=filterRoutesList(db.getCurrentUseremail());
            adapter.setRoutesData(filteredList);
            recyclerView.setAdapter(adapter);
            String x= String.valueOf(filteredList.size()*5);
            x=Txt+x;
            price.setText(x);
        });
    }


}

