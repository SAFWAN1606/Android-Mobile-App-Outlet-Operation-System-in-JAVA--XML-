package com.outlet.outletoperationsystem;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.outlet.outletoperationsystem.Common.Common;
import com.outlet.outletoperationsystem.Model.User;

public class SignUp extends AppCompatActivity {
    private EditText editPhone,editPassword,editName,editEmail;
    private Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editPhone =   findViewById(R.id.editPhone);
        editEmail =   findViewById(R.id.editEmail);
        editPassword =  findViewById(R.id.editPassword);
        editName =  findViewById(R.id.editName);
        btnSignUp =   findViewById(R.id.btnSignUp);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference user_table = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Common.isConnectedToInternet(getBaseContext())) {
                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Please waiting...");
                    mDialog.show();

                    user_table.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(editPhone.getText().toString()).exists()) {
                                Toast.makeText(SignUp.this, "Phone Number already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                User user = new User(editPassword.getText().toString(), editEmail.getText().toString(), editName.getText().toString());
                                user_table.child(editPhone.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            mDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    Toast.makeText(SignUp.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
