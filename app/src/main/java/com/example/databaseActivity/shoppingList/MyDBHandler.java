package com.example.databaseActivity.shoppingList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.example.databaseActivity.MyApplication;

import java.util.ArrayList;

/**
 * {@link SQLiteOpenHelper} implementation for app
 */
public class MyDBHandler extends SQLiteOpenHelper {
    private static MyDBHandler instance;

    private final ContentResolver contentResolver;

    /**
     * Database information
     */
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productDB.db";
    static final String TABLE_PRODUCTS = "products";

    /**
     * name of the columns in the database
     */
    public static final String COLUMN_ID = "_id", COLUMN_PRODUCT_NAME = "productname", COLUMN_QUANTITY = "quantity";


    /**
     * Singleton implementation of MyDBHandler
     * (should be private but the constructor is being called by the ContentProvider)
     * @param context Context in which it is running
     */
    MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        contentResolver = context.getContentResolver();
    }

    private static MyDBHandler getInstance() {
        if (instance == null) {
            instance = new MyDBHandler(MyApplication.getContext());
        }
        return instance;
    }
    //-------------------------Singleton Implementation----------------------------------
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOK_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + " ( " +
                COLUMN_PRODUCT_NAME + " TEXT, " +
                COLUMN_QUANTITY + " INTEGER )";

        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        this.onCreate(db);
    }

    /**
     * Used to Add a product to the database, if it is not there yet
     * (there is only one product with each name)
     * @param product the Product to be added
     */
    public static void addProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getProductName());
        values.put(COLUMN_QUANTITY, product.getQuantity());

        int onUpdate = getInstance().contentResolver.update(MyContentProvider.CONTENT_URI, values, MyDBHandler.COLUMN_PRODUCT_NAME + " = ?", new String[]{product.getProductName()});
        if (onUpdate == 0) {
            getInstance().contentResolver.insert(MyContentProvider.CONTENT_URI, values);
        }
    }

    /**
     * Used to find a product
     * @param productNameString String with the name of the product we are looking for
     * @return ArrayList containing:<ul>
     *     <li>one Product if it finds a Product with the given name</li>
     *     <li>All products if String is Empty</li>
     *     <li>Nothing if the product is not in the database</li>
     * </ul>
     */
    public static ArrayList<Product> findProduct(String productNameString) {
        String whereClause = null;
        String[] whereParam = null;

        if (!TextUtils.isEmpty(productNameString)) {
            whereClause = MyDBHandler.COLUMN_PRODUCT_NAME + " = ?";
            whereParam = new String[]{productNameString};
        }

        Cursor cursor = getInstance().contentResolver.query(MyContentProvider.CONTENT_URI, null, whereClause, whereParam, MyDBHandler.COLUMN_QUANTITY);

        String prodNameQuery;
        int prodQuantityQuery;
        Product productQuery;
        ArrayList<Product> tableRow = new ArrayList<>();


        //cursor can be null !?
        assert cursor != null;
        //--------------------
        if (cursor.moveToFirst()) {
            do {
                productQuery = new Product();
                prodNameQuery = cursor.getString(cursor.getColumnIndex(MyDBHandler.COLUMN_PRODUCT_NAME));
                prodQuantityQuery = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MyDBHandler.COLUMN_QUANTITY)));
                productQuery.setProductName(prodNameQuery);
                productQuery.setQuantity(prodQuantityQuery);

                tableRow.add(productQuery);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tableRow;
    }

    /**
     * delete a Product from the database
     * @param productName String with the name of the product to be looked up
     * @return Boolean as true if product was deleted or false if it didn't find the product
     */
    public static boolean deleteProduct(String productName) {
        boolean result = false;
        String selection = "productname = \"" + productName + "\"";
        int rowsDeleted = getInstance().contentResolver.delete(MyContentProvider.CONTENT_URI, selection, null);

        if (rowsDeleted > 0) {
            result = true;
        }

        return result;
    }


}
