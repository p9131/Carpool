package com.example.mile;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.mile.Route.RouteItem;
import com.example.mile.Route.RoutesViewModel;
import com.google.gson.Gson;

import java.util.List;

public class MyJavaScriptInterface {

    private final WebView webView;

    public MyJavaScriptInterface(WebView webView) {
        this.webView = webView;
    }


    FirebaseHelp mydb= FirebaseHelp.getInstance();
    RoutesViewModel routesViewModel;

    List<RouteItem> routeItems;
    @JavascriptInterface
    public String addRouteToFirebase(String startTime, String startPoint, String destination) {;
        RouteItem route=new RouteItem(mydb.getCurrentUseremail(),startTime, startPoint, destination);

        mydb.addRoute(route);
        Gson gson = new Gson();
        return gson.toJson(route);
    }

    public  void logOut(){
        mydb.signOut();
    }


    @JavascriptInterface
    public String sendDataToJavaScript() {
        return driver.work(getdrivermail());
    }

    @JavascriptInterface
    public String getdrivermail() {
        return mydb.getCurrentUseremail();
    }

    @JavascriptInterface
    public String getRouteUsers(String id) {
        return driver.work2(id);
    }

    @JavascriptInterface
    public void changeStatus(String tripId,String user,String newStatus){
        mydb.changeStatus(tripId, user, newStatus);
    }





}

