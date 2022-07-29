package com.example.ezstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AccessActivity extends AppCompatActivity {

    private String user_id = "";
    private String getResult ;

    private CodeScanner mCodeScanner;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);
        user_id = getIntent().getExtras().getString("user_Id");
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait ..!");
        progressDialog.setCanceledOnTouchOutside(false);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},123);
        }
        else{
            startScanning();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 123){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                startScanning();
            }
            else {
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startScanning() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view_acc);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.startPreview();
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getResult = result.getText();
                if(!getResult.isEmpty()){
                    // send access verification to firebase
                    updateFirebaseDB();
                }
            }
        }));
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    private void updateFirebaseDB() {
        progressDialog.setMessage("Creating access to store ... ");
        String timestamp = ""+System.currentTimeMillis();

        //setup data for save
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("uid",""+firebaseAuth.getUid());
        hashMap.put("accessQR",""+getResult);
        hashMap.put("accType","User");
        hashMap.put("online","true");

        //save to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Access");
        ref.child(firebaseAuth.getUid()).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //db updated

                        progressDialog.dismiss();
                        Toast.makeText(AccessActivity.this,"Access success, door will open in few movements ... ",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AccessActivity.this,"Something went wrong... Try again !",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    public void onBackAccessClick(View view) {
        finish();
    }
}