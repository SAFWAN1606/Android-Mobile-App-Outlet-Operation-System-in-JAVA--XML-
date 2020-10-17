package com.system.outletoperationsystemserver.ViewHolder;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.system.outletoperationsystemserver.Common.Common;
import com.system.outletoperationsystemserver.Interface.ItemClickListener;
import com.system.outletoperationsystemserver.R;



public class RestaurantViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {
    public TextView textRestaurantName;
    public ImageView imageView;
    public TextView textLocation;
    private ItemClickListener itemClickListener;

    public RestaurantViewHolder(@NonNull View itemView) {
        super(itemView);

        textRestaurantName = (TextView) itemView.findViewById(R.id.restaurant_name);
        imageView = (ImageView) itemView.findViewById(R.id.restaurant_image);
        textLocation = (TextView) itemView.findViewById(R.id.restaurant_location);
        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }


    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);
    }
}
