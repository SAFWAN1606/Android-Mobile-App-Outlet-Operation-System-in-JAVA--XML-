package com.outlet.outletoperationsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.outlet.outletoperationsystem.Common.Common;
import com.outlet.outletoperationsystem.Model.User;

import io.paperdb.Paper;


public class SignIn extends AppCompatActivity {
    private EditText editPhone,editPassword;
    private Button btnSignIn;
    private CheckBox ckbRemember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);
        editPhone =   findViewById(R.id.editPhone);
        editPassword =  findViewById(R.id.editPassword);
        btnSignIn =   findViewById(R.id.btnSignIn);
        ckbRemember = findViewById(R.id.ckbRemember);

        Paper.init(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference user_table = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Common.isConnectedToInternet(getBaseContext())) {

                    if(ckbRemember.isChecked()){
                        Paper.book().write(Common.USER_KEY,editPhone.getText().toString());
                        Paper.book().write(Common.PWD_KEY,editPassword.getText().toString());
                    }

                    final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                    mDialog.setMessage("Please waiting...");
                    mDialog.show();
                    user_table.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child(editPhone.getText().toString()).exists()) {
                                User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
                                user.setPhone(editPhone.getText().toString());
                                mDialog.dismiss();
                                if (user.getPassword() != null && editPassword.getText() != null && user.getPassword().equals(editPassword.getText().toString())) {

                                    Intent homeIntent = new Intent(SignIn.this, Restaurant.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();

                                } else {
                                    Toast.makeText(SignIn.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }

                                finish();
                            } else {
                                Toast.makeText(SignIn.this, "User not exist in database", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    Toast.makeText(SignIn.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
