package com.example.ezstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        //init UI fields
        etEmail = findViewById(R.id.etEmail02);

        //auth firebase
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void clickBackToLogin(View view) {
        startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class));
        finish();
    }

    public void onBtnResetClick(View view) {
        recoverPassword();
    }

    private String email;

    private void recoverPassword() {
        email = etEmail.getText().toString().trim();

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
        else{
            // setup progress dialog
            progressDialog.setMessage("Sending recovery instructions to reset password ...");
            progressDialog.show();
        }

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //instructions sent
                        progressDialog.dismiss();
                        Toast.makeText(ForgetPasswordActivity.this,"Reset instructions sent to your E-mail...",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //fail to send instructions
                        progressDialog.dismiss();
                        Toast.makeText(ForgetPasswordActivity.this,"Invalid E-mail address or Something went wrong!, Try again",Toast.LENGTH_SHORT).show();
                    }
                });

    }
}