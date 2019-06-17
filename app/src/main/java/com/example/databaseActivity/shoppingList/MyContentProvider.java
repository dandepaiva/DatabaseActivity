package com.example.databaseActivity.shoppingList;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;


/* TODO
    JAVADOC the Class
    JAVADOC globals
 */

/**
 * Content Provider used to interact with the Database of Products
 */
public class MyContentProvider extends ContentProvider {
    private MyDBHandler databaseHandler;

    /**
     * URI to the database of Products
     */
    private static final String AUTHORITY = "com.example.databaseActivity.shoppingList.MyContentProvider";
    private static final String PRODUCTS_TABLE = "products";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PRODUCTS_TABLE);

    /**
     * Column IDs
     */
    public static final int PRODUCTS = 1;
    public static final int PRODUCTS_ID = 2;


    /** Creates a UriMatcher object */
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /*
     * <u>All</u> the content URI patterns that the provider recognizes
     */
    static {
        sURIMatcher.addURI(AUTHORITY, PRODUCTS_TABLE, PRODUCTS);
        sURIMatcher.addURI(AUTHORITY, PRODUCTS_TABLE + "/#", PRODUCTS_ID);
    }

    public MyContentProvider() {}

    @Override
    public boolean onCreate() {
        databaseHandler = new MyDBHandler(getContext());
        return true;
    }


    /**
     * UNUSED
     */
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = databaseHandler.getWritableDatabase();
        long id;


        // RECOMMENDED TO USE IF INSTEAD OF SWITCH
        switch (uriType){
            case PRODUCTS:
            id = sqlDB.insert(MyDBHandler.TABLE_PRODUCTS, null, values);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        //to use notifyChange: getContext() -> getContentResolver()
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(PRODUCTS_TABLE + "/" + id);
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MyDBHandler.TABLE_PRODUCTS);

        int uriType = sURIMatcher.match(uri);
        switch (uriType){
            case PRODUCTS_ID:
                queryBuilder.appendWhere(MyDBHandler.COLUMN_ID + "="
                + uri.getLastPathSegment());
                break;
            case PRODUCTS:
                break;
                default:
                    throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(databaseHandler.getReadableDatabase(),
                projection, selection, selectionArgs,
                null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = databaseHandler.getWritableDatabase();
        int rowsUpdate = 0;

        switch (uriType){
            case PRODUCTS:
                rowsUpdate = sqlDB.update(MyDBHandler.TABLE_PRODUCTS, values, selection, selectionArgs);
                break;
            case PRODUCTS_ID:
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)) {
                    rowsUpdate = sqlDB.update(MyDBHandler.TABLE_PRODUCTS,
                            values,
                            MyDBHandler.COLUMN_ID + "=" + id,
                            null);

                } else {
                    rowsUpdate = sqlDB.update(
                            MyDBHandler.TABLE_PRODUCTS, values,
                            MyDBHandler.COLUMN_ID + "=" + id  + " and " + selection,
                            selectionArgs);
                }
                break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdate;
    }




    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int uriType =sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = databaseHandler.getWritableDatabase();
        int rowsDeleted;

        switch (uriType){
            case PRODUCTS:
                rowsDeleted = sqlDB.delete(MyDBHandler.TABLE_PRODUCTS, selection, selectionArgs);
                break;
            case PRODUCTS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    rowsDeleted = sqlDB.delete(MyDBHandler.TABLE_PRODUCTS,
                            MyDBHandler.COLUMN_ID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(MyDBHandler.TABLE_PRODUCTS,
                            MyDBHandler.COLUMN_ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
