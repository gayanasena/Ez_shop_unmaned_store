package com.example.ezstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private Button btnReg;
    private EditText etNameVal,etEmailVal,etMobileVal,etPasswordVal;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //setup firebase

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait ..!");
        progressDialog.setCanceledOnTouchOutside(false);

        etNameVal = findViewById(R.id.etName);
        etEmailVal = findViewById(R.id.etEmail);
        etMobileVal= findViewById(R.id.etMobile);
        etPasswordVal = findViewById(R.id.etPassword);
    }

    public void onFaqBackClick(View view) {
       this.finish();
    }

    public void onFaqHaveAccClick(View view) {
        this.finish();
    }

    public void onBtnRegisterClick(View view) {
        //register user
        InputData();
    }

    private String name,email,mobileNo,password;

    private void InputData() {

        //input data
        name = etNameVal.getText().toString().trim();
        email = etEmailVal.getText().toString().trim();
        mobileNo = etMobileVal.getText().toString().trim();
        password = etPasswordVal.getText().toString().trim();

        //data validation
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Fill the Name field",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Fill the Email field",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(this,"Enter a valid E-mail address",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(mobileNo))
        {
            Toast.makeText(this,"Fill the Mobile Number field",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!PhoneNumberUtils.isGlobalPhoneNumber(mobileNo))
        {
            Toast.makeText(this,"Enter a valid Mobile Number",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Fill the Password field",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(password.length()<=8)
        {
            Toast.makeText(this,"Password length must be at least 8 characters",Toast.LENGTH_SHORT).show();
            return;
        }

        createAccount();
    }

    private void createAccount() {
        progressDialog.setMessage("Creating Account....");
        progressDialog.show();

        //creating account
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //account created
                        saveFirebaseData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed to create
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,"Registration failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveFirebaseData() {

        progressDialog.setMessage("Saving Account Info ... ");
        String timestamp = ""+System.currentTimeMillis();

        //setup data for save
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("uid",""+firebaseAuth.getUid());
        hashMap.put("name",""+name);
        hashMap.put("email",""+email);
        hashMap.put("mobile",""+mobileNo);
        hashMap.put("password",""+password);
        hashMap.put("timestamp",""+timestamp);
        hashMap.put("accType","User");
        hashMap.put("online","false");

        //save to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.child(firebaseAuth.getUid()).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //db updated

                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,"Registration Success",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,"Something went wrong... Register again !",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this,RegisterActivity.class));
                        finish();
                    }
                });
    }
}