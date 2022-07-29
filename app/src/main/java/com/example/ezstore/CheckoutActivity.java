package com.example.ezstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezstore.model.CartItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class CheckoutActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private HashMap<String,Object> hashMap;
    private EasyDB easyDB;
    private Double allAmount =0.0;
    private TextView txtAmountPay;
    private EditText etNamePay,etAddressPay,etMobilePay;
    private RadioButton radCOD,radSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // setup counter

        Integer count = 0;

        //init ui components
        txtAmountPay = findViewById(R.id.txtAmountPay);
        etNamePay = findViewById(R.id.etNamePay);
        etAddressPay = findViewById(R.id.etAddressPay);
        etMobilePay = findViewById(R.id.etMobilePay);
        radCOD = findViewById(R.id.radCODPay);
        radSite = findViewById(R.id.radOnsitePay);

        //radio button default clicked
        radSite.setChecked(true);


        //firebase get instance
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait ..!");
        progressDialog.setCanceledOnTouchOutside(false);

        //create hashmap for store data
        hashMap = new HashMap<>();

        //get cart items from db
        easyDB = EasyDB.init(this,"ITEMS_DB").setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id", new String[]{"text","not null"}))
                .addColumn(new Column("Item_PID", new String[]{"text","not null"}))
                .addColumn(new Column("Item_name", new String[]{"text","not null"}))
                .addColumn(new Column("Item_price", new String[]{"text","not null"}))
                .addColumn(new Column("Item_qty", new String[]{"text","not null"}))
                .doneTableColumn();

        //data validation
        if(!TextUtils.isEmpty(etNamePay.getText().toString())&&!TextUtils.isEmpty(etMobilePay.getText().toString()))
        {
            Toast.makeText(this,"Please Fill the  field",Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            //get all records
            Cursor res = easyDB.getAllData();
            while (res.moveToNext()){
                hashMap.put("item_id_"+count, res.getString(1));
                hashMap.put("item_pid_"+count, res.getString(2));
                hashMap.put("item_name_"+count, res.getString(3));
                hashMap.put("item_price_"+count, res.getString(4));
                hashMap.put("item_qty_"+count, res.getString(5));

                allAmount = allAmount + (Double.parseDouble(res.getString(4))*Double.parseDouble(res.getString(5)));
                txtAmountPay.setText("Amount Rs"+allAmount.toString());

                count++;
            }

        }


    }

    private void saveFirebaseData() {

        //save to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Orders");
        ref.child(firebaseAuth.getUid()).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //db updated

                        progressDialog.dismiss();
                        Toast.makeText(CheckoutActivity.this,"Check-out Success",Toast.LENGTH_SHORT).show();
                        easyDB.deleteAllDataFromTable();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(CheckoutActivity.this,"Something went wrong... Check-out again !",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    public void onBtnPayClick(View view) {
        hashMap.put("uid",""+firebaseAuth.getUid());
        hashMap.put("name",etNamePay.getText().toString());
        hashMap.put("delivery_address",etAddressPay.getText().toString());
        hashMap.put("mobile",etMobilePay.getText().toString());
        hashMap.put("amount",allAmount);
        if(radCOD.isChecked())
        {
            hashMap.put("type_pay","true");
        }
        else {
            hashMap.put("type_pay","false");
        }

        saveFirebaseData();
    }
}