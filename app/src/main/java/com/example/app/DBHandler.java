package com.example.app;

import static android.content.ContentValues.TAG;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static DBHandler dbHandler = null;

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "shop_products";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "products";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our course name column
    private static String NAME_COL = "name";

    private SQLiteDatabase db;

    private DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DBHandler getInstance(Context context) {
        if (dbHandler == null) {
            dbHandler = new DBHandler(context);
        }
        return dbHandler;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT)";
        db.execSQL(query);
    }
    public long addNewProduct(String productName) {
        this.db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME_COL, productName);
        long id = db.insert(TABLE_NAME, null, values);

        db.close();
        return id;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void remove(long str) {
        db = getWritableDatabase();
        db.beginTransaction();
        String s = Long.toString(str);
        try {
            db.delete(TABLE_NAME, "id=?", new String[]{s});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }
    public void update(long str, String newText) {
        db = getWritableDatabase();
        db.beginTransaction();
        String s = Long.toString(str);

        ContentValues values = new ContentValues();
        values.put(NAME_COL, newText);
        try {
            db.update(TABLE_NAME, values,"id=?", new String[]{s});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }
    public List<Product> getAllProducts() {

        List<Product> products = new ArrayList<>();
        String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME;

        db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") long ids = cursor.getLong(cursor.getColumnIndex(ID_COL));
                    @SuppressLint("Range") String str = cursor.getString(cursor.getColumnIndex(NAME_COL));
                    products.add(new Product(ids, str));
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return products;
    }
    public List<Product> getFirst(int number) {

        List<Product> products = new ArrayList<>();
        String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME;

        db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") long ids = cursor.getLong(cursor.getColumnIndex(ID_COL));
                    @SuppressLint("Range") String str = cursor.getString(cursor.getColumnIndex(NAME_COL));
                    products.add(new Product(ids, str));
                    number--;
                } while(cursor.moveToNext() && number > 0);
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return products;
    }
}
