package com.example.DatabaseActivity.AsyncTasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.example.DatabaseActivity.MyApplication;
import com.example.DatabaseActivity.MyContentProvider;
import com.example.DatabaseActivity.MyDBHandler;
import com.example.DatabaseActivity.Product;

public class AddProductAsync extends AsyncTask<Product, Product, Void> {

    private static final String TAG = "ADD_ASYNC";

    @Override
    protected Void doInBackground(Product...product) {
        Log.d(TAG, "adding asynchronously " + product[0].toString());

        ContentValues values = new ContentValues();
        values.put(MyDBHandler.COLUMN_PRODUCTNAME, product[0].getProductName());
        values.put(MyDBHandler.COLUMN_QUANTITY, product[0].getQuantity());

        ContentResolver database = MyApplication.getContext().getContentResolver();

        int onUpdate = database.update(MyContentProvider.CONTENT_URI, values, MyDBHandler.COLUMN_PRODUCTNAME + " = ?", new String[]{product[0].getProductName()});
        if (onUpdate == 0) {
            database.insert(MyContentProvider.CONTENT_URI, values);
        }
        return null;
    }
}
