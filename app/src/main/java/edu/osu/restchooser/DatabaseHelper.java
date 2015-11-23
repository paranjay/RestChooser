package edu.osu.restchooser;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DatabaseHelper {
    private static final String DATABASE_NAME = "RestChooser.db";
    private static final int DATABASE_VERSION = 1;
    private static final String USERS_TABLE = "Accounts";
    private static final String FILTERS_TABLE = "Filters";
    private static final String RESTAURANTS_TABLE = "Restaurants";
    private Context context;
    private SQLiteDatabase db;
    private SQLiteStatement insertStmt;
    private static final String INSERT_USER = "insert into " + USERS_TABLE + "(name, password) values (?, ?)" ;
    private static final String INSERT_FILTER = "insert into " + FILTERS_TABLE + "(reviewRating, distanceRange, cuisine) values (?, ?, ?)" ;
    private static final String RESTAURANT_FILTER = "insert into " + RESTAURANTS_TABLE + "(address, reviewRating, cuisine, reviews, businessId, name) " +
            "values (?, ?, ?, ?, ?, ?)" ;

    public DatabaseHelper(Context context) {
        this.context = context;
        RestarauntOpenHelper openHelper = new RestarauntOpenHelper(this.context);
        this.db = openHelper.getWritableDatabase();
//         openHelper.onUpgrade(this.db, 1, 2);

    }

    public long insertUser(String name, String password) {
        this.insertStmt = this.db.compileStatement(INSERT_USER);
        this.insertStmt.bindString(1, name);
        this.insertStmt.bindString(2, password);
        return this.insertStmt.executeInsert();
    }

    public long insertFilter(String reviewRating, String distanceRange, String cuisine)
    {
        this.insertStmt = this.db.compileStatement(INSERT_FILTER);
        this.insertStmt.bindString(1, reviewRating);
        this.insertStmt.bindString(2, distanceRange);
        this.insertStmt.bindString(3, cuisine);
        return this.insertStmt.executeInsert();
    }

    public long insertRestaurant(String businessId, String address, String reviewRating, String cuisine, String reviews, String name)
    {
        this.insertStmt = this.db.compileStatement(RESTAURANT_FILTER);
        this.insertStmt.bindString(1, address);
        this.insertStmt.bindString(2, reviewRating);
        this.insertStmt.bindString(3, cuisine);
        this.insertStmt.bindString(4, reviews);
        this.insertStmt.bindString(5, businessId);
        this.insertStmt.bindString(6, name);
        return this.insertStmt.executeInsert();
    }

    public void deleteAll() {

        this.db.delete(USERS_TABLE, null, null);
        this.db.delete(RESTAURANTS_TABLE, null, null);
        this.db.delete(FILTERS_TABLE, null, null);
    }

    public List<String> selectAllUsers(String username, String password) {
        List<String> list = new ArrayList<>();
        Cursor cursor = this.db.query(USERS_TABLE, new String[]
                { "name", "password" },
                "name = '"+ username +"' AND password= '"+ password+"'", null, null, null, "name desc");
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
                list.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    public List<Restaurant> GetAllRestaurants()
    {
        List<Restaurant> restaurants = new ArrayList<>();
        Cursor cursor = this.db.query(RESTAURANTS_TABLE, new String[]
                        { "businessId", "address", "reviewRating", "cuisine", "reviews, name"},
                null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Restaurant rest = new Restaurant(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3),cursor.getString(4), cursor.getString(5));
                restaurants.add(rest);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return restaurants;
    }

    private static class RestarauntOpenHelper extends SQLiteOpenHelper {
        RestarauntOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + USERS_TABLE + "(id INTEGER PRIMARY KEY, name TEXT, password TEXT, email TEXT, home TEXT)");
            db.execSQL("CREATE TABLE " + FILTERS_TABLE + "(id INTEGER PRIMARY KEY, dollarRating TEXT, reviewRating TEXT, distanceRange TEXT," +
                    "cuisine TEXT )");
            db.execSQL("CREATE TABLE " + RESTAURANTS_TABLE + "(id INTEGER PRIMARY KEY, businessId Text, address TEXT, reviewRating TEXT, cuisine TEXT," +
                    "reviews TEXT , name TEXT)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.w("Example", "Upgrading database; this will drop and recreate the tables.");
            db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + FILTERS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + RESTAURANTS_TABLE);
            onCreate(db);
        }
    }
}
