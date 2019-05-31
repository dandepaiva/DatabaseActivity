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
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.UserDataHandler;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity Button";
    private String TAG2 = "FIND BUTTON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        find_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] productNameString = {productName.getText().toString()};
                Cursor cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, null, MyDBHandler.COLUMN_PRODUCTNAME + " = ?", productNameString, null);
                ArrayList<String > tableRow = new ArrayList<>();
                String aux;
                if (cursor.moveToFirst()) {
                    do {
                         Log.d(TAG2, "1-findButton found one " + cursor.getString(cursor.getColumnIndex(MyDBHandler.COLUMN_PRODUCTNAME))+ " \t quantity: " + cursor.getString(cursor.getColumnIndex(MyDBHandler.COLUMN_QUANTITY)));
                         aux = cursor.getString(cursor.getColumnIndex(MyDBHandler.COLUMN_PRODUCTNAME))+ ", " + cursor.getString(cursor.getColumnIndex(MyDBHandler.COLUMN_QUANTITY));
                         tableRow.add(aux);
                    } while (cursor.moveToNext());
                }
                aux = "";
                for(String iterator : tableRow){
                    aux = aux + iterator +";\n" ;
                }

                Toast.makeText(MainActivity.this,
                        aux,
                        Toast.LENGTH_SHORT).show();
                cursor.close();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
