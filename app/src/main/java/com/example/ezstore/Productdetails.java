package com.example.ezstore;

import static com.example.ezstore.HomeActivity.itemId;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ezstore.model.Products;

import java.util.ArrayList;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;



public class Productdetails extends AppCompatActivity {

    private TextView textTitle,textPrice,textDesc,textCategory,textQtyView;
    private ImageView imgItem;

    private String prodName,prodPrice,prodId;

    private  Integer itemQtyValue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);

        itemQtyValue = 1;

        textTitle = findViewById(R.id.txtTitle);
        textPrice = findViewById(R.id.txtPrice);
        textDesc = findViewById(R.id.txtDesc);
        imgItem = findViewById(R.id.imgMainImg);
        textCategory = findViewById(R.id.txtCategory);
        textQtyView = findViewById(R.id.txtQtyDisplay);

        //get data from view all products page
        prodName = getIntent().getExtras().getString("prod_name");
        prodPrice = getIntent().getExtras().getString("prod_price");
        String proDesc = getIntent().getExtras().getString("prod_desc");
        String proCategory = getIntent().getExtras().getString("prod_category");
        String ImgURL = getIntent().getExtras().getString("prod_img");
        prodId = getIntent().getExtras().getString("prod_id");

        textTitle.setText(prodName);
        textPrice.setText("Rs."+prodPrice);
        textDesc.setText(proDesc);
        textCategory.setText(proCategory);

        Glide.with(this).load(ImgURL).into(imgItem);


    }

    public void imgBackClick(View view) {
        finish();
    }

    private Double totPrice ;
    //Item Qty Manage buttons
    public void onQtyMinusClick(View view) {
        totPrice = 0.0;
        if(itemQtyValue > 1)
        {
            itemQtyValue--;
            totPrice = Double.parseDouble(prodPrice)*itemQtyValue;
            textPrice.setText("Rs."+totPrice.toString());
        }
        else{
            Toast.makeText(this, "Minimum order quantity reached!", Toast.LENGTH_SHORT).show();
        }
        textQtyView.setText(itemQtyValue.toString());
    }

    public void onQtyPlusClick(View view) {
        totPrice = 0.0;
        if(itemQtyValue >= 1)
        {
            itemQtyValue++;
            totPrice = Double.parseDouble(prodPrice)*itemQtyValue;
            textPrice.setText("Rs."+totPrice.toString());
        }
        textQtyView.setText(itemQtyValue.toString());
    }

    public void onBtnAddCartPD(View view) {
        //Press add to cart button prom product details view
        addToCart();
    }



    private  void  addToCart(/*String id,String name, String price, String qty*/){
        itemId++;
        EasyDB easyDB = EasyDB.init(this,"ITEMS_DB").setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id", new String[]{"text","not null"}))
                .addColumn(new Column("Item_PID", new String[]{"text","not null"}))
                .addColumn(new Column("Item_name", new String[]{"text","not null"}))
                .addColumn(new Column("Item_price", new String[]{"text","not null"}))
                .addColumn(new Column("Item_qty", new String[]{"text","not null"}))
                .doneTableColumn();

        boolean b = easyDB.addData("Item_Id",itemId)
                .addData("Item_PID",prodId)
                .addData("Item_name",prodName)
                .addData("Item_price",prodPrice)
                .addData("Item_qty",itemQtyValue)
                .doneDataAdding();

        Toast.makeText(this, "Item(s) added to cart...", Toast.LENGTH_SHORT).show();

    }
}
