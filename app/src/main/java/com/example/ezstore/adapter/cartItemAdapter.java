package com.example.ezstore.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezstore.R;
import com.example.ezstore.model.CartItem;

import java.util.ArrayList;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class cartItemAdapter extends  RecyclerView.Adapter<cartItemAdapter.HolderCartItem>{

    private Context context;
    private ArrayList<CartItem> cartItemArrayList;

    public cartItemAdapter(Context context, ArrayList<CartItem> cartItemArrayList) {
        this.context = context;
        this.cartItemArrayList = cartItemArrayList;
    }


    @NonNull
    @Override
    public HolderCartItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("ResourceType") View view = LayoutInflater.from(context).inflate(R.layout.cart_item_card,parent,false);
        return new HolderCartItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCartItem holder, @SuppressLint("RecyclerView") int position) {
        CartItem modelCart = cartItemArrayList.get(position);

        String id = modelCart.getId();
        String title = modelCart.getName();
        String pid = modelCart.getPid();
        String price = modelCart.getPrice();
        String qty = modelCart.getQty();

        //set data
        holder.txtTitleCart.setText(title);
        holder.txtEachAmount.setText(price);
        holder.txtQtyCart.setText(qty);

        //handle remove click listener
        holder.txtItemRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create db if not exist
                EasyDB easyDB = EasyDB.init(context,"ITEMS_DB").setTableName("ITEMS_TABLE")
                        .addColumn(new Column("Item_Id", new String[]{"text","not null"}))
                        .addColumn(new Column("Item_PID", new String[]{"text","not null"}))
                        .addColumn(new Column("Item_name", new String[]{"text","not null"}))
                        .addColumn(new Column("Item_price", new String[]{"text","not null"}))
                        .addColumn(new Column("Item_qty", new String[]{"text","not null"}))
                        .doneTableColumn();

                easyDB.deleteRow(1,id);
                Toast.makeText(context, "Item Removed", Toast.LENGTH_SHORT).show();

                //refresh list
                cartItemArrayList.remove(position);
                notifyItemChanged(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemArrayList.size();
    }

    class HolderCartItem extends RecyclerView.ViewHolder{

        //ui attributes
        private TextView txtTitleCart,txtEachAmount,txtQtyCart,txtItemRemove;

        public HolderCartItem(@NonNull View itemView) {
            super(itemView);

            //init view attributes
            txtTitleCart = itemView.findViewById(R.id.txtTitleCart);
            txtEachAmount = itemView.findViewById(R.id.txtEachAmount);
            txtQtyCart = itemView.findViewById(R.id.txtQtyCart);
            txtItemRemove = itemView.findViewById(R.id.txtItemRemove);

        }
    }
}
