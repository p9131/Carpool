package com.example.mile.history;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mile.FirebaseHelp;
import com.example.mile.MainActivity;
import com.example.mile.R;
import com.example.mile.Route.RouteItem;
import com.example.mile.Tracking_Page;
import com.example.mile.home;

import java.util.List;
import java.util.stream.Collectors;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {


    private final Listener listen;
    public List<RouteItem> routeList;



    public HistoryAdapter(List<RouteItem> HistoryList, Listener listen) {
        this.routeList = HistoryList;
        this.listen = listen;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(view,listen);
    }



    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        RouteItem route = routeList.get(position);

        holder.textHistoryName.setText(route.getdestination());
        holder.textHistoryDescription.setText(route.getStartTime());
        holder.textHistoryID.setText(route.getStartPoint());

        holder.itemView.setOnClickListener(v -> {
            if (listen != null) {
                listen.onClicked(route);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }



    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView textHistoryName;
        public TextView textHistoryDescription;
        public TextView textHistoryID;

        public HistoryViewHolder(@NonNull View itemView, Listener listen) {
            super(itemView);
            textHistoryName = itemView.findViewById(R.id.Date);
            textHistoryDescription = itemView.findViewById(R.id.Status);
            textHistoryID = itemView.findViewById(R.id.OrderId);
        }
    }

    public void setRoutesData(List<RouteItem> routeList) {
        this.routeList = routeList;
    }

}
