package com.outlet.outletoperationsystem;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.outlet.outletoperationsystem.Common.Common;
import com.outlet.outletoperationsystem.Model.User;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {
    Button btnSignUp,btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        Paper.init(this);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signIn = new Intent(MainActivity.this,SignIn.class);
                startActivity(signIn);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(MainActivity.this,SignUp.class);
                startActivity(signUp);
            }
        });

        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);

        if(user != null && pwd != null){
            if(!user.isEmpty()&& !pwd.isEmpty() ){
                login(user,pwd);
            }
        }
    }

    private void login(final String phoneNumber, final String pwd) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference user_table = database.getReference("User");
        if(Common.isConnectedToInternet(getBaseContext())) {



            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please waiting...");
            mDialog.show();
            user_table.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(phoneNumber).exists()) {
                        User user = dataSnapshot.child(phoneNumber).getValue(User.class);
                        user.setPhone(phoneNumber);
                        mDialog.dismiss();
                        if (user.getPassword() != null && pwd != null && user.getPassword().equals(pwd)) {

                            Intent homeIntent = new Intent(MainActivity.this, Restaurant.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
                            finish();

                        } else {
                            Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }

                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "User not exist in database", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            Toast.makeText(MainActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
        }


        }

}
