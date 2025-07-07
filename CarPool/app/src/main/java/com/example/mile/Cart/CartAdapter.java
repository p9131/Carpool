package com.example.mile.Cart;// CartItemAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mile.FirebaseHelp;
import com.example.mile.R;
import com.example.mile.Route.RouteItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartItemViewHolder> {

    private OnRemoveButtonClickListener removeButtonClickListener;
    public List<RouteItem> routeList;
    FirebaseHelp fire=FirebaseHelp.getInstance();

    String mail=fire.getCurrentUseremail();


    public CartAdapter(List<RouteItem> cartItemList) {
        this.routeList = cartItemList;
    }

    public void setOnRemoveButtonClickListener(OnRemoveButtonClickListener listener) {
        this.removeButtonClickListener = listener;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        RouteItem route = routeList.get(position);
        String status = route.getuserRequestStatusMap().get(mail);

        holder.status.setText(route.getStartTime());
//        holder.textCartDescription.setText("To: "+route.getdestination());
        holder.textCartDescription.setText(status);
        holder.removeButton.setOnClickListener(view -> {
            if (removeButtonClickListener != null) {
                RouteItem clickedRouteItem = routeList.get(position);
                removeButtonClickListener.onRemoveButtonClick(position, clickedRouteItem);

            }
        });
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textCartDescription, status;

        private ImageButton removeButton;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.textRouteDescription);
            textCartDescription = itemView.findViewById(R.id.textRouteName);
            removeButton = itemView.findViewById(R.id.removeButton);

        }


    }

    public void setRoutesData(List<RouteItem> routeList) {
        this.routeList = routeList;
    }

    public interface OnRemoveButtonClickListener {
        void onRemoveButtonClick(int position, RouteItem clickedRouteItem);
    }


}
