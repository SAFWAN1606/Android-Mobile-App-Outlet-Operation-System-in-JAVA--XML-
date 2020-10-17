package com.outlet.outletoperationsystem;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.outlet.outletoperationsystem.Common.Common;
import com.outlet.outletoperationsystem.Interface.ItemClickListener;
import com.outlet.outletoperationsystem.Model.Category;
import com.outlet.outletoperationsystem.Model.RestaurantModel;
import com.outlet.outletoperationsystem.ViewHolder.MenuViewHolder;
import com.outlet.outletoperationsystem.ViewHolder.RestaurantViewHolder;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class Restaurant extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    FirebaseDatabase database;
    DatabaseReference restaurant;
    TextView textFullName;
    RecyclerView recycler_restaurant;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<RestaurantModel,RestaurantViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Restaurant");
        setSupportActionBar(toolbar);
        database = FirebaseDatabase.getInstance();
        restaurant = database.getReference("Restaurant");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(Restaurant.this,Cart.class);
                startActivity(cartIntent);
            }
        });

        Paper.init(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        textFullName = (TextView) headerView.findViewById(R.id.textFullName);
        textFullName.setText(Common.currentUser.getName());

        recycler_restaurant = (RecyclerView) findViewById(R.id.recycler_restaurant);
        recycler_restaurant.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_restaurant.setLayoutManager(layoutManager);

        loadMenu();
    }

    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<RestaurantModel, RestaurantViewHolder>(RestaurantModel.class,R.layout.restaurant_item, RestaurantViewHolder.class,restaurant) {
            @Override
            protected void populateViewHolder(RestaurantViewHolder viewHolder, RestaurantModel model, int position) {
                viewHolder.textRestaurantName.setText(model.getName());
                viewHolder.textLocation.setText(model.getLocation());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageView);

                final RestaurantModel clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodList = new Intent(Restaurant.this, Home.class);
                        foodList.putExtra("restaurantId",adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });

            }
        };

        recycler_restaurant.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
           Intent restaurant = new Intent(this,Restaurant.class);
           startActivity(restaurant);
        } else if (id == R.id.nav_cart) {
            Intent cartIntent = new Intent(Restaurant.this,Cart.class);
            startActivity(cartIntent);
        } else if (id == R.id.nav_orders) {
            Intent orderIntent = new Intent(Restaurant.this,OrderStatus.class);
            startActivity(orderIntent);
        } else if (id == R.id.nav_logout) {
            Paper.book().destroy();
            Intent signIn = new Intent(Restaurant.this,SignIn.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
