package com.example.mile.Route;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mile.R;

import java.util.List;


public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private List<RouteItem> routeList;
    private OnItemClickedListener listener;



    public RouteAdapter(List<RouteItem> routeList, OnItemClickedListener listener) {
        this.routeList = routeList;
        this.listener=listener;
    }


    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_item, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        RouteItem route = routeList.get(position);

        if (route.getStartPoint().contains("Gate 3") || route.getdestination().contains("Gate 3")) {
            holder.textRouteName.setText("Gate 3");
        } else if (route.getStartPoint().contains("Gate 4") || route.getdestination().contains("Gate 4")) {
            holder.textRouteName.setText("Gate 4");
        } else {
            holder.textRouteName.setText("Unknown Gate");
        }
        holder.textRouteDescription.setText(route.getStartTime());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(route);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public void setRoutesData(List<RouteItem> routeList) {
        this.routeList = routeList;
    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder {
        public TextView textRouteName;
        public TextView textRouteDescription;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            textRouteName = itemView.findViewById(R.id.textRouteName);
            textRouteDescription = itemView.findViewById(R.id.textRouteDescription);
        }
    }
}
