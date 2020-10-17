package com.system.outletoperationsystemserver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.system.outletoperationsystemserver.Common.Common;
import com.system.outletoperationsystemserver.Interface.ItemClickListener;
import com.system.outletoperationsystemserver.Model.RestaurantModel;
import com.system.outletoperationsystemserver.ViewHolder.RestaurantViewHolder;

import java.security.ProtectionDomain;
import java.util.UUID;

public class Restaurant extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    FirebaseDatabase database;
    DatabaseReference restaurant;
    TextView textFullName;
    RecyclerView recycler_restaurant;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<RestaurantModel, RestaurantViewHolder> adapter;
    FirebaseStorage storage;
    StorageReference storageReference;
    DrawerLayout drawer;
    EditText editName,editLocation;
    Button  btnUpload,btnSelect;
    RestaurantModel newRestaurant;
    Uri saveUri;
    private  final int PICK_IMAGE_REQUEST = 71;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Restaurant");
        setSupportActionBar(toolbar);
        database = FirebaseDatabase.getInstance();
        restaurant = database.getReference("Restaurant");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              showDialog();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    private void showDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Restaurant.this);
        alertDialog.setTitle("Add new Restaurant");
        alertDialog.setMessage("Please fil full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_restaurant_layout = inflater.inflate(R.layout.add_new_restaurant_layout,null);
        editName = add_restaurant_layout.findViewById(R.id.editName);
        editLocation = add_restaurant_layout.findViewById(R.id.editLocation);
        btnSelect = add_restaurant_layout.findViewById(R.id.btnSelect);
        btnUpload = add_restaurant_layout.findViewById(R.id.btnUpload);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    chooseImage();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
        alertDialog.setView(add_restaurant_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if(newRestaurant != null){
                    restaurant.push().setValue(newRestaurant);

                    Snackbar.make(drawer,"New Restaurant "+newRestaurant.getName()+"was added", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void uploadImage() {
        if(saveUri != null){
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(Restaurant.this,"Uploaded !!!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newRestaurant = new RestaurantModel(editName.getText().toString(),uri.toString(),editLocation.getText().toString());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(Restaurant.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Uploaded "+progress+"%");
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
            && data != null && data.getData() != null){
            saveUri = data.getData();
           btnSelect.setText("Image Selected !");
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
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
                        Intent homeList = new Intent(Restaurant.this, Home.class);
                        homeList.putExtra("restaurantId",adapter.getRef(position).getKey());
                        startActivity(homeList);

                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
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
            //Intent cartIntent = new Intent(Restaurant.this,Cart.class);
            //startActivity(cartIntent);
        } else if (id == R.id.nav_orders) {
           // Intent orderIntent = new Intent(Restaurant.this,OrderStatus.class);
           // startActivity(orderIntent);
        } else if (id == R.id.nav_logout) {
            //Intent signIn = new Intent(Restaurant.this,SignIn.class);
           // signIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
           // startActivity(signIn);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }else if(item.getTitle().equals(Common.DELETE)){
            deleteRestaurant(adapter.getRef(item.getOrder()).getKey());
        }


        return super.onContextItemSelected(item);
    }

    private void deleteRestaurant(String key) {
        restaurant.child(key).removeValue();
        Toast.makeText(Restaurant.this, "Restaurant Deleted !!!", Toast.LENGTH_SHORT).show();
    }

    private void showUpdateDialog(final String key, final RestaurantModel item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Restaurant.this);
        alertDialog.setTitle("Update Restaurant");
        alertDialog.setMessage("Please fil full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_restaurant_layout = inflater.inflate(R.layout.add_new_restaurant_layout,null);
        editName = add_restaurant_layout.findViewById(R.id.editName);
        editLocation = add_restaurant_layout.findViewById(R.id.editLocation);
        btnSelect = add_restaurant_layout.findViewById(R.id.btnSelect);
        btnUpload = add_restaurant_layout.findViewById(R.id.btnUpload);
        editName.setText(item.getName());
        editLocation.setText(item.getLocation());
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               changeImage(item);
            }
        });
        alertDialog.setView(add_restaurant_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setName(editName.getText().toString());
                item.setLocation(editLocation.getText().toString());
                restaurant.child(key).setValue(item);

            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void changeImage(final RestaurantModel item) {
        if(saveUri != null){
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(Restaurant.this,"Uploaded !!!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                   item.setImage(uri.toString());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(Restaurant.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Uploaded "+progress+"%");
                }
            });
        }
    }
}
