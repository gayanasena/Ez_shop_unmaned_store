package com.example.ezstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// manual imports

import com.example.ezstore.adapter.ProductAdapter;
import com.example.ezstore.adapter.ProductCategoryAdapter;
import com.example.ezstore.model.ProductCategory;
import com.example.ezstore.model.Products;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ProductCategoryAdapter productCategoryAdapter;
    RecyclerView productCatRecycler, prodItemRecycler;
    ProductAdapter productAdapter;
    String user_id = "";
    DatabaseReference databaseReference;
    ArrayList<Products> productsArrayList;

    public static Integer itemId = 1;

    FirebaseAuth firebaseAuth;

    private Button btnToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnToCart = findViewById(R.id.btnToCartHome);
        user_id = getIntent().getExtras().getString("user_Id");
        firebaseAuth = FirebaseAuth.getInstance();

        List<ProductCategory> productCategoryList = new ArrayList<>();
        productCategoryList.add(new ProductCategory(1, "Food"));
        productCategoryList.add(new ProductCategory(2, "Groceries"));
        productCategoryList.add(new ProductCategory(3, "Electronics"));
        productCategoryList.add(new ProductCategory(4,"Stationery"));
        productCategoryList.add(new ProductCategory(5, "Fashion & Beauty"));

        setProductCategoryRecycler(productCategoryList); //for categories

        prodItemRecycler = findViewById(R.id.product_recycler);
        databaseReference = FirebaseDatabase.getInstance().getReference("Items_promo");
        prodItemRecycler.setHasFixedSize(true);
        prodItemRecycler.setLayoutManager( new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

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

    private void onCategoryClick(){

    }

    private void setProductCategoryRecycler(List<ProductCategory> productCategoryList){

        productCatRecycler = findViewById(R.id.cat_recycler);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        productCatRecycler.setLayoutManager(layoutManager);
        productCategoryAdapter = new ProductCategoryAdapter(this, productCategoryList);
        productCatRecycler.setAdapter(productCategoryAdapter);

    }


    public void OnScanQRClick(View view) {
        startActivity(new Intent(HomeActivity.this,scanQRActivity .class));
    }

    public void onSignOutClick(View view) {
        firebaseAuth.signOut();
        startActivity((new Intent(this,LoginActivity.class)));
        finish();
    }

    public void onSearchProductClick(View view) {
        startActivity((new Intent(this,ViewAllProductsActivity.class).putExtra("QRvalue","null_value")));

    }

    public void btnToCart(View view) {
        startActivity((new Intent(this, ViewCartActivity.class)));
    }

    public void onLocateClick(View view) {
        startActivity((new Intent(this,LocateActivity.class)));
    }

    public void onAccessImgClick(View view) {
        startActivity((new Intent(this,AccessActivity.class).putExtra("user_Id",user_id)));
    }
}