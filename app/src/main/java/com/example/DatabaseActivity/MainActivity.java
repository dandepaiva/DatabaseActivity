package com.example.DatabaseActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

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
        final Button delete_button = findViewById(R.id.delete_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productNameString = productName.getText().toString();
                String quantityValue = quantity.getText().toString();

                if (TextUtils.isEmpty(productNameString)) {
                    Toast.makeText(MainActivity.this, getString(R.string.no_product_name), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(quantityValue)) {
                    Toast.makeText(MainActivity.this, getString(R.string.no_product_quantity, productNameString), Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(quantityValue) == 0) {
                    Toast.makeText(MainActivity.this, getString(R.string.zero_product,productNameString), Toast.LENGTH_SHORT).show();
                } else {

                    Product findProduct = new Product(productNameString, Integer.parseInt(quantityValue));
                    MyDBHandler.addProduct(findProduct);

                }
            }
        });

        find_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productNameString = productName.getText().toString();
                mAdapter = new MyAdapter(MyDBHandler.findProduct(productNameString));
                recyclerView.setAdapter(mAdapter);
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productNameString = productName.getText().toString();

                if (MyDBHandler.deleteProduct(productNameString)) {
                    Toast.makeText(MainActivity.this, getString(R.string.delete_success, productNameString), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.delete_failed, productNameString), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
