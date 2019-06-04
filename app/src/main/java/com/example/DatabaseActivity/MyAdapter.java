package com.example.DatabaseActivity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Product> productArrayList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public TextView quantity;

        public MyViewHolder(View v) {
            super(v);
            quantity = v.findViewById(R.id.list_quantity);
            productName = v.findViewById(R.id.list_product_name);
        }

        public void onBind(Product p) {
            productName.setText(p.getProductName()+": ");
            quantity.setText("\t" + p.getQuantity() + " units");
        }
    }

    public MyAdapter(ArrayList<Product> mDataset) {
        this.productArrayList = mDataset;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder myViewHolder, int position) {
        myViewHolder.onBind(productArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }
}
