package com.example.ezstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail,etPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize UI values
        etEmail = findViewById(R.id.etEmail01);
        etPassword = findViewById(R.id.etPassword01);

        //auth firebase
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
    }


    public void clickRegister(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
    }

    public void onFaqRegClick(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
    }

    public void onBtnLoginClick(View view) {
        loginUser();
    }

    private String email, password;

    private void loginUser() {

        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        //validating inputs
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Enter a Email ",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(this,"Invalid E-mail address",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Fill the Password field",Toast.LENGTH_SHORT).show();
            return;
        }

        // setup progress bar
        progressDialog.setMessage("Logging In ...");
        progressDialog.show();

        //firebase process
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //logged in
                        makeUserOnline();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //login error
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"Invalid E-mail ot Password, Try again!",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void makeUserOnline() {
        // after logging
        progressDialog.setMessage("Checking User .....");

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("online","true");

        //update value to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //update successfully
                        checkUserType();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failing update
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"Error while checking data, Try again!",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserType() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            String accountType = ""+ds.child("accType").getValue();
                            if(accountType.equals("User")){
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class).putExtra("User_Id", firebaseAuth.getUid()));
                                finish();
                            }
                            else if(accountType.equals("Admin")){
                                progressDialog.dismiss();
                                // to admin activity
                                finish();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this,"Error in user account!, Try again!",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"Something went wrong!, Try again!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
    }

    public void onForgetPwdClick(View view) {
        startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));
        finish();
    }


}