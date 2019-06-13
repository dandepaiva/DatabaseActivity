package com.example.DatabaseActivity.shoppingList;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.DatabaseActivity.MyApplication;
import com.example.DatabaseActivity.R;

import java.util.ArrayList;

/* TODO
    JAVADOC ALL
 */


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private final ArrayList<Product> productArrayList;

    MyAdapter(ArrayList<Product> productArrayList) {
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder myViewHolder, int position) {
        myViewHolder.onBind(productArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView productName;
        final TextView quantity;

        MyViewHolder(View v) {
            super(v);
            quantity = v.findViewById(R.id.list_quantity);
            productName = v.findViewById(R.id.list_product_name);
        }

        void onBind(Product p) {
            productName.setText(String.format("%s: ", p.getProductName()));
            quantity.setText(MyApplication.getContext().getString(R.string.my_adapter_units, p.getQuantity()));
        }
    }
}
