package com.example.ezstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;


public class scanQRActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;

    private String getResult = "No QR Data";

    private TextView txtQRDisplay;

    private Button btnAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qractivity);

        btnAddItem = findViewById(R.id.btnToCartHome);
        btnAddItem.setEnabled(false);

        txtQRDisplay = (TextView) findViewById(R.id.txtQRDisp);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},123);
        }
        else{
            startScanning();
        }

    }

    @Override
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
        txtQRDisplay.setText("No QR Scan Data");
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getResult = result.getText();
                txtQRDisplay.setText(getResult);
                if(!getResult.isEmpty()){
                    btnAddItem.setEnabled(true);
                }
            }
        }));
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    public void onAddToCartClick(View view) {
        Intent Intent = new Intent(scanQRActivity.this,ViewAllProductsActivity.class);
        Intent.putExtra("QRvalue", getResult);
        startActivity(Intent);
    }

    public void onBackQRScanClick(View view) {
        finish();
    }
}
