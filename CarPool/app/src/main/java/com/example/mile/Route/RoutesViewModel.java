package com.example.mile.Route;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class RoutesViewModel extends ViewModel {

    RouteRepo routeRepository;
    private MutableLiveData<List<RouteItem>> routesLiveData;

    public RoutesViewModel() {
        routeRepository = new RouteRepo();
        routesLiveData = routeRepository.getAllDriversRoutes();
    }

    public MutableLiveData<List<RouteItem>> getRoutesLiveData() {
        return routesLiveData;
    }
}


