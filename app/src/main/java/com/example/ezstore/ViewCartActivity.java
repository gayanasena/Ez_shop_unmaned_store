package com.example.ezstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.ezstore.R;
import com.example.ezstore.adapter.cartItemAdapter;
import com.example.ezstore.model.CartItem;

import java.util.ArrayList;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class ViewCartActivity extends AppCompatActivity {

    private TextView txtTitleCart,txtEachAmount,txtQtyCart,txtItemRemove,txtAnount;

    private Double allAmount =0.0;
    private ArrayList<CartItem> cartItemArrayList;
    private cartItemAdapter cartItemAdapter;
    RecyclerView recyclerViewCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        //init list
        cartItemArrayList = new ArrayList<CartItem>();

        //init views
        txtTitleCart = findViewById(R.id.txtTitleCart);
        txtEachAmount = findViewById(R.id.txtEachAmount);
        txtQtyCart = findViewById(R.id.txtQtyCart);
        txtAnount = findViewById(R.id.txtAmount);
        txtItemRemove = findViewById(R.id.txtItemRemove);
        recyclerViewCart = findViewById(R.id.cart_recycler);
        recyclerViewCart.setHasFixedSize(true);
        recyclerViewCart.setLayoutManager( new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        EasyDB easyDB = EasyDB.init(this,"ITEMS_DB").setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id", new String[]{"text","not null"}))
                .addColumn(new Column("Item_PID", new String[]{"text","not null"}))
                .addColumn(new Column("Item_name", new String[]{"text","not null"}))
                .addColumn(new Column("Item_price", new String[]{"text","not null"}))
                .addColumn(new Column("Item_qty", new String[]{"text","not null"}))
                .doneTableColumn();
        //get all records
        Cursor res = easyDB.getAllData();
        while (res.moveToNext()){
            String id = res.getString(1);
            String pid = res.getString(2);
            String title = res.getString(3);
            String price = res.getString(4);
            String qty = res.getString(5);

            allAmount = allAmount + (Double.parseDouble(price)*Double.parseDouble(qty));
            txtAnount.setText(allAmount.toString());

            CartItem modelCart = new CartItem(
                     ""+id,
                    ""+pid,
                    ""+title,
                    ""+price,
                    ""+qty
                    );
            cartItemArrayList.add(modelCart);

        }
        //setup adapter
        cartItemAdapter =  new cartItemAdapter(this,cartItemArrayList);
        //set to recycler view
        recyclerViewCart.setAdapter(cartItemAdapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        allAmount = 0.0;
    }

    public void onBackCartClick(View view) {
        finish();
    }

    public void btnCheckOutClick(View view) {
        startActivity((new Intent(this,CheckoutActivity.class)
                .putExtra("Amount",allAmount)));

    }
}