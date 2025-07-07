package com.example.mile.Route;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RouteItem implements Serializable {
    private String driverEmail;
    private String id;
    private String startTime;
    private String destination;
    private String startPoint;
    private Map<String, String> userRequestStatusMap;

    public RouteItem() {
        // Required empty constructor for Firestore
    }

    public RouteItem(String driverEmail,String startTime, String startPoint, String destination) {
        this.driverEmail=driverEmail;
        this.startTime = startTime;
        this.startPoint = startPoint;
        this.destination = destination;
        this.userRequestStatusMap = new HashMap<>();
        setid();
    }


    public String getDriverEmail() {
        return driverEmail;
    }
    public String getid() {
        return id;
    }
    public String getStartTime() {
        return startTime;
    }

    public String getdestination() {
        return destination;
    }

    public String getStartPoint() {
        return startPoint;
    }
    public Map<String, String> getuserRequestStatusMap(){
        return userRequestStatusMap;
    }

    public void setid() {
        this.id=UUID.randomUUID().toString();
    }



}
