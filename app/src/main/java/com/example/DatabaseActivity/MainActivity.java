package com.example.DatabaseActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String TAG = "FIND BUTTON";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.students_recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        final Button add_button = findViewById(R.id.add_button);
        final EditText productName = findViewById(R.id.product_name);
        final EditText quantity = findViewById(R.id.quantity);
        final Button find_button = findViewById(R.id.find_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productNameString = productName.getText().toString();
                String quantityValue = quantity.getText().toString();

                /*TODO
                    quantity should not be 0
                 */
                if(TextUtils.isEmpty(productNameString) || TextUtils.isEmpty(quantityValue)){
                } else {
                    ContentValues values = new ContentValues();
                    values.put(MyDBHandler.COLUMN_PRODUCTNAME, productNameString);
                    values.put(MyDBHandler.COLUMN_QUANTITY, Integer.parseInt(quantityValue));
                    getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
                }
            }
        });

        final ArrayList<Product> tableRow = new ArrayList<>();

        find_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] productNameString = {productName.getText().toString()};
                Cursor cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, null, MyDBHandler.COLUMN_PRODUCTNAME + " = ?", productNameString, MyDBHandler.COLUMN_QUANTITY);

                String prodNameQuery;
                int prodQuantQuery;
                Product productQuery;
                tableRow.clear();

                if (cursor.moveToFirst()) {
                    do {
                        productQuery= new Product();
                        prodNameQuery = cursor.getString(cursor.getColumnIndex(MyDBHandler.COLUMN_PRODUCTNAME));
                        prodQuantQuery = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MyDBHandler.COLUMN_QUANTITY)));
                        productQuery.setProductName(prodNameQuery);
                        productQuery.setQuantity(prodQuantQuery);

                        tableRow.add(productQuery);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                mAdapter = new MyAdapter(tableRow);
                recyclerView.setAdapter(mAdapter);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
