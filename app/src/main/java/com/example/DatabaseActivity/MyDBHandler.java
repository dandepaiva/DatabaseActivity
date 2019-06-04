package com.example.DatabaseActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {
    private static String TAG = "SINGLETON";
    private static MyDBHandler instance;

    private ContentResolver myCR;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productDB.db";
    public static final String TABLE_PRODUCTS = "products";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_QUANTITY = "quantity";

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myCR = context.getContentResolver();
    }

    public static MyDBHandler getInstance() {
        if(instance == null){
            instance = new MyDBHandler(MyApplication.getContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOK_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + " ( " +
                COLUMN_PRODUCTNAME + " TEXT, " +
                COLUMN_QUANTITY + " INTEGER )";

        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        this.onCreate(db);
    }


    public static void addProduct(Product product) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_QUANTITY, product.getQuantity());

        int onUpdate = getInstance().myCR.update(MyContentProvider.CONTENT_URI, values, MyDBHandler.COLUMN_PRODUCTNAME + " = ?", new String[]{product.getProductName()});
        if (onUpdate == 0) {
            getInstance().myCR.insert(MyContentProvider.CONTENT_URI, values);
        }
    }

    public static ArrayList<Product> findProduct(String productNameString){
        String whereClause = null;
        String[] whereParam = null;

        if (!TextUtils.isEmpty(productNameString)) {
            whereClause = MyDBHandler.COLUMN_PRODUCTNAME + " = ?";
            whereParam = new String[]{productNameString};
        }

        Cursor cursor = getInstance().myCR.query(MyContentProvider.CONTENT_URI, null, whereClause, whereParam, MyDBHandler.COLUMN_QUANTITY);

        String prodNameQuery;
        int prodQuantQuery;
        Product productQuery;
        ArrayList<Product> tableRow = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                productQuery = new Product();
                prodNameQuery = cursor.getString(cursor.getColumnIndex(MyDBHandler.COLUMN_PRODUCTNAME));
                prodQuantQuery = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MyDBHandler.COLUMN_QUANTITY)));
                productQuery.setProductName(prodNameQuery);
                productQuery.setQuantity(prodQuantQuery);

                tableRow.add(productQuery);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tableRow;
    }

    public static boolean deleteProduct(String productName){
        boolean result = false;
        String selection = "productname = \"" + productName + "\"";
        int rowsDeleted = getInstance().myCR.delete(MyContentProvider.CONTENT_URI, selection, null);

        if(rowsDeleted > 0){
            result = true;
        }

        return result;
    }


}
