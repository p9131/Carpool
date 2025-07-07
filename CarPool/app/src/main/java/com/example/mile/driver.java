package com.example.mile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.mile.Route.RouteAdapter;
import com.example.mile.Route.RouteItem;
import com.example.mile.Route.RoutesViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class driver extends AppCompatActivity{
RoutesViewModel routesViewModel;
    static WebView webView;
    static List<RouteItem> list;
    public MyJavaScriptInterface inter = new MyJavaScriptInterface(webView);

    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        routesViewModel = new ViewModelProvider(this).get(RoutesViewModel.class);

        webView =findViewById(R.id.driverWeb);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setBuiltInZoomControls(false);

        webView.loadUrl("file:///android_asset/driver.html");
        webView.addJavascriptInterface(inter, "Android");
        webView.addJavascriptInterface(this, "Android2");
        routesViewModel.getRoutesLiveData().observe(this, routeList -> {
            Log.d("RoutesViewModel2", "Observer triggered with new data: " + routeList);
            list=routeList;
        });

    }


    public static String work(String mail){
        Gson gson = new Gson();
        String x;
        List<RouteItem> filteredList = list.stream()
                .filter(routeItem -> routeItem.getDriverEmail().equals(mail))
                .collect(Collectors.toList());
        x=gson.toJson(filteredList);
        return x;
    }


    public static String work2(String id){
        List<RouteItem> filteredList = list.stream()
                .filter(routeItem -> routeItem.getid().equals(id))
                .collect(Collectors.toList());
        Gson gson = new Gson();
        String x;
        x=gson.toJson(filteredList.get(0));
        return x;
    }


    @JavascriptInterface
    public void LogOut(){
        auth.signOut();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }


}

//'" + list + "'