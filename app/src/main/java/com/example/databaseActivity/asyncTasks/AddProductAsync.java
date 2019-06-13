package com.example.databaseActivity.asyncTasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.example.databaseActivity.MyApplication;
import com.example.databaseActivity.shoppingList.MyContentProvider;
import com.example.databaseActivity.shoppingList.MyDBHandler;
import com.example.databaseActivity.shoppingList.Product;

class AddProductAsync extends AsyncTask<Product, Product, Void> {

    private static final String TAG = "ADD_ASYNC";

    @Override
    protected Void doInBackground(Product...product) {
        Log.d(TAG, "adding asynchronously " + product[0].toString());

        ContentValues values = new ContentValues();
        values.put(MyDBHandler.COLUMN_PRODUCT_NAME, product[0].getProductName());
        values.put(MyDBHandler.COLUMN_QUANTITY, product[0].getQuantity());

        ContentResolver database = MyApplication.getContext().getContentResolver();

        int onUpdate = database.update(MyContentProvider.CONTENT_URI, values, MyDBHandler.COLUMN_PRODUCT_NAME + " = ?", new String[]{product[0].getProductName()});
        if (onUpdate == 0) {
            database.insert(MyContentProvider.CONTENT_URI, values);
        }
        return null;
    }
}
