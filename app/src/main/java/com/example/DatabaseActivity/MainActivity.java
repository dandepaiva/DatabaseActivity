package com.example.DatabaseActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderComunication {
    private String TAG = "FIND BUTTON";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ProductsAsyncTask asyncTask;

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


        asyncTask = new ProductsAsyncTask(MainActivity.this);
        asyncTask.execute();


        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productNameString = productName.getText().toString();
                String quantityValue = quantity.getText().toString();

                if (TextUtils.isEmpty(productNameString)) {
                    showToast(getString(R.string.no_product_name));
                } else if (TextUtils.isEmpty(quantityValue)) {
                    showToast(getString(R.string.no_product_quantity, productNameString));
                } else if (Integer.parseInt(quantityValue) == 0) {
                    showToast(getString(R.string.zero_product, productNameString));
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

                String message = MyDBHandler.deleteProduct(productNameString)
                        ? getString(R.string.delete_success, productNameString)
                        : getString(R.string.delete_failed, productNameString);

                showToast(message);

            }
        });

    }

    public void sendProgress(int progress){
        Log.d(TAG, "sendProgress() called with: progress = [" + progress + "]");
        TextView loading = findViewById(R.id.loadscreen);
        loading.setText(getString(R.string.progress_bar, progress));
    }

    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }
}
