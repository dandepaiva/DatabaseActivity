package com.example.DatabaseActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.UserDataHandler;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity Button";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button add_button = findViewById(R.id.add_button);
        final EditText productName = findViewById(R.id.product_name);
        final EditText quantity = findViewById(R.id.quantity);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productNameString = productName.getText().toString();
                String quantityValue = quantity.getText().toString();

                /*TODO
                    quantity should not be 0
                 */
                if(TextUtils.isEmpty(productNameString) || TextUtils.isEmpty(quantityValue)){
                    Log.d(TAG, "1 - onClick() called with " + productNameString + ", " + quantityValue);
                } else {

                    ContentValues values = new ContentValues();
                    values.put(MyDBHandler.COLUMN_PRODUCTNAME, productNameString);
                    values.put(MyDBHandler.COLUMN_QUANTITY, Integer.parseInt(quantityValue));
                    getContentResolver().insert(MyContentProvider.CONTENT_URI, values);

                    Log.d(TAG, "2 - onClick() called with " + productNameString + ", " + quantityValue);
                }
            }
        });

        Button find_button = findViewById(R.id.find_button);
        find_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String productNameString = productName.getText().toString();
                Cursor cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, null, MyDBHandler.COLUMN_PRODUCTNAME + " LIKE \"" + productNameString  + "\"", null, null);
                if (cursor.moveToFirst()) {

                    do {
                        Toast.makeText(MainActivity.this,
                                cursor.getString(cursor.getColumnIndex(MyDBHandler.COLUMN_PRODUCTNAME)) +
                                        ", " + cursor.getString(cursor.getColumnIndex(MyDBHandler.COLUMN_QUANTITY)),
                                Toast.LENGTH_SHORT).show();
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
