package com.example.ezstore.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ezstore.model.Products;
import com.example.ezstore.Productdetails;
import com.example.ezstore.R;
import com.example.ezstore.model.Products;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    ArrayList<Products> productsList;


    public ProductAdapter(Context context, ArrayList<Products> productsList) {
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.products_row_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, int position) {

        Glide.with(context).load(productsList.get(position).getImageUrl()).into(holder.prodImage);
        Products product = productsList.get(position);
        holder.prodName.setText(productsList.get(position).getProductName());
        holder.prodQty.setText(productsList.get(position).getProductQty());
        holder.prodPrice.setText("Rs."+productsList.get(position).getProductPrice());

        String prodId = productsList.get(position).getProductid().toString();
        String prodCategoryStr = productsList.get(position).getCategory().toString();
        String prodDescStr = productsList.get(position).getDescription().toString();
        String prodNameStr = productsList.get(position).getProductName().toString();
        String prodPriceStr = productsList.get(position).getProductPrice().toString();
        String ImgURLStr = productsList.get(position).getImageUrl();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Productdetails.class);
                i.putExtra("prod_name",prodNameStr);
                i.putExtra("prod_price",prodPriceStr);
                i.putExtra("prod_category",prodCategoryStr);
                i.putExtra("prod_desc",prodDescStr);
                i.putExtra("prod_img",ImgURLStr);
                i.putExtra("prod_id",prodId);

                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static final class ProductViewHolder extends RecyclerView.ViewHolder{

        ImageView prodImage;
        TextView prodName, prodQty, prodPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            prodImage = itemView.findViewById(R.id.prod_image);
            prodName = itemView.findViewById(R.id.prod_name);
            prodPrice = itemView.findViewById(R.id.prod_price);
            prodQty = itemView.findViewById(R.id.prod_qty);


        }
    }

}
