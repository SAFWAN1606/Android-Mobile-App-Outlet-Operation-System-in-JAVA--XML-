package com.outlet.outletoperationsystem.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.outlet.outletoperationsystem.Interface.ItemClickListener;
import com.outlet.outletoperationsystem.R;

public class RestaurantViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView textRestaurantName;
    public ImageView imageView;
    public TextView textLocation;
    private ItemClickListener itemClickListener;

    public RestaurantViewHolder(@NonNull View itemView) {
        super(itemView);

        textRestaurantName = (TextView) itemView.findViewById(R.id.restaurant_name);
        imageView = (ImageView) itemView.findViewById(R.id.restaurant_image);
        textLocation = (TextView) itemView.findViewById(R.id.restaurant_location);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
