package com.example.DatabaseActivity.shoppingList;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DatabaseActivity.AsyncTasks.ProductsAsyncTask;
import com.example.DatabaseActivity.LoaderCommunication;
import com.example.DatabaseActivity.MyApplication;
import com.example.DatabaseActivity.R;
import com.example.DatabaseActivity.Repository;

import java.util.ArrayList;

/**
 * This Activity allows the user to create and interact with a database of {@link Product}s
 */
public class MainActivity extends AppCompatActivity implements LoaderCommunication {
    private String TAG = "FIND BUTTON";
    private String TAG2 = "REPOSITORY";

    /**
     * global variables to be used to generate and manage a RecyclerView
     * <p>
     * it will be used to show the user a list of {@link Product}s in the database
     */
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button addButton = findViewById(R.id.add_button);
        final Button findButton = findViewById(R.id.find_button);
        final Button deleteButton = findViewById(R.id.delete_button);
        final EditText productName = findViewById(R.id.product_name);
        final EditText quantity = findViewById(R.id.quantity);

        recyclerView = findViewById(R.id.products_recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager recyclerViewLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayout);

        addButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productNameString = productName.getText().toString();
                String quantityValue = quantity.getText().toString();

                /** Check if you wrote the name of the product */
                if (TextUtils.isEmpty(productNameString)) {
                    showToast(getString(R.string.no_product_name));
                }
                /** Check if you gave it a quantity */
                else if (TextUtils.isEmpty(quantityValue)) {
                    showToast(getString(R.string.no_product_quantity, productNameString));
                }
                /** Check if the quantity is 0 */
                else if (Integer.parseInt(quantityValue) == 0) {
                    showToast(getString(R.string.zero_product, productNameString));

                } else {
                    Product findProduct = new Product(productNameString, Integer.parseInt(quantityValue));
                    Repository.getInstance().insertProduct(findProduct, new Repository.UpdateUI() {
                        @Override
                        public void updateProgress(String text) {
                        }
                    });
                }
            }
        });

        /*
         * Find click listener
         * when pressing the trashcanButton [Find] it will
         * check the TextView with the name of the product
         * send it to the {@link Repository} to be found or not in the database
         * updated the Recycler View with the product's values
         */
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String productNameString = productName.getText().toString();

                Repository.getInstance().findProduct(productNameString, new Repository.ProductsArrayList() {
                    @Override
                    public void productsArrayList(ArrayList<Product> products) {
                        new Runnable() {
                            @Override
                            public void run() {

                            }
                        };
                        recyclerViewAdapter = new MyAdapter(products);
                    }
                });
                recyclerView.setAdapter(recyclerViewAdapter);
            }
        });

        /*
         * Delete trashcanButton listener
         * when pressing the trashcanButton [Delete] it will
         * check the TextView with the name of the product
         * send it to the {@link Repository} to be deleted or not in the database
         * shows a {@link Toast} with a message of success or not of the query
         */
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String productNameString = productName.getText().toString();

                Repository.getInstance().deleteProduct(productNameString, new Repository.IsSuccess() {
                    @Override
                    public void isSuccess(boolean success) {
                        final String message = success
                                ? MyApplication.getContext().getString(R.string.delete_success, productNameString)
                                : MyApplication.getContext().getString(R.string.delete_failed, productNameString);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast(message);
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * Writes progress to a textView
     *
     * @param progress made at {@link ProductsAsyncTask}
     */
    public void sendProgress(int progress) {
        TextView loading = findViewById(R.id.load_screen);
        loading.setText(getString(R.string.progress_bar, progress));
    }

    /**
     * Generalization of the
     * {@link Toast#makeText(Context, int, int)} usage
     * writes it to the MainActivity
     *
     * @param text String to be written in the Toast
     */
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
