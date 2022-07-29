package com.example.ezstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ezstore.adapter.ProductAdapter;
import com.example.ezstore.adapter.ProductCategoryAdapter;
import com.example.ezstore.model.ProductCategory;
import com.example.ezstore.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewAllProductsActivity extends AppCompatActivity {

    ProductCategoryAdapter productCategoryAdapter;
    RecyclerView productCatRecycler, prodItemRecycler;
    ProductAdapter productAdapter;

    DatabaseReference databaseReference;
    ArrayList<Products> productsArrayList;

    private EditText textSearchBar;
    private ImageButton btnSrc;

    String QRValue = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_products);
        btnSrc = findViewById(R.id.imageButtonSrc);

        //init components
        textSearchBar = findViewById(R.id.editTextSearch);
        prodItemRecycler = findViewById(R.id.shopRecyclerView2);

        //get put extra values
        QRValue = getIntent().getExtras().getString("QRvalue");

        if( QRValue.equals("null_value")){
            findAllProducts();
        }
        else if(!QRValue.equals("null_value")){
            getItemFromQr();
        }
        else {
           finish();
        }


    }

    private void findAllProducts()
    {
        databaseReference  =FirebaseDatabase.getInstance().getReference("Items_promo");
        prodItemRecycler.setHasFixedSize(true);
        prodItemRecycler.setLayoutManager( new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        productsArrayList = new ArrayList<>();
        productAdapter = new ProductAdapter(this,productsArrayList);
        prodItemRecycler.setAdapter(productAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Products products = dataSnapshot.getValue(Products.class);
                    productsArrayList.add(products);
                }
                productAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getItemFromQr(){
        databaseReference  =FirebaseDatabase.getInstance().getReference();
        Query query =  databaseReference.child("Items_promo").orderByChild("barcode").equalTo(QRValue);
        prodItemRecycler.setHasFixedSize(true);
        prodItemRecycler.setLayoutManager( new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        productsArrayList = new ArrayList<>();
        productAdapter = new ProductAdapter(this,productsArrayList);
        prodItemRecycler.setAdapter(productAdapter);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Products products = dataSnapshot.getValue(Products.class);
                    productsArrayList.add(products);
                }
                productAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setProductCategoryRecycler(List<ProductCategory> productCategoryList){

        productCatRecycler = findViewById(R.id.cat_recycler);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        productCatRecycler.setLayoutManager(layoutManager);
        productCategoryAdapter = new ProductCategoryAdapter(this, productCategoryList);
        productCatRecycler.setAdapter(productCategoryAdapter);

    }

    public void imgBackClick(View view) {
        finish();
    }


    public void imgBtnSrcClick(View view) {
        textSearchBar = findViewById(R.id.editTextSearch);
        productsArrayList.clear();
        String srcStr = textSearchBar.getText().toString();
        String srcStrUp = srcStr.toUpperCase(Locale.ROOT);
        String srcStrLW = srcStr.toLowerCase(Locale.ROOT);

        databaseReference  =FirebaseDatabase.getInstance().getReference();
        Query query =  databaseReference.child("Items_promo").orderByChild("productName").startAt(srcStr).endAt(srcStr+"\uf8ff");
        prodItemRecycler.setHasFixedSize(true);
        prodItemRecycler.setLayoutManager( new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        productsArrayList = new ArrayList<>();
        productAdapter = new ProductAdapter(this,productsArrayList);
        prodItemRecycler.setAdapter(productAdapter);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Products products = dataSnapshot.getValue(Products.class);
                    productsArrayList.add(products);
                }
               productAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


   
}

