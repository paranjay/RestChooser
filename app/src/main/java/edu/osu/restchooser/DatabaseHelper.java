package edu.osu.restchooser;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

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
    private static final String INSERT_FILTER = "insert into " + FILTERS_TABLE + "(name, password) values (?, ?)" ;
    private static final String RESTAURANT_FILTER = "insert into " + RESTAURANTS_TABLE + "(name, password) values (?, ?)" ;

    public DatabaseHelper(Context context) {
        this.context = context;
        RestarauntOpenHelper openHelper = new RestarauntOpenHelper(this.context);
        this.db = openHelper.getWritableDatabase();
        this.insertStmt = this.db.compileStatement(INSERT_USER);
    }

    public long insert(String name, String password) {
        this.insertStmt.bindString(1, name);
        this.insertStmt.bindString(2, password);
        return this.insertStmt.executeInsert();
    }

    public long insertFilter(String )


    public void deleteAll() {

        this.db.delete(USERS_TABLE, null, null);
        this.db.delete(RESTAURANTS_TABLE, null, null);
        this.db.delete(FILTERS_TABLE, null, null);
    }

    public List<String> selectAll(String username, String password) {
        List<String> list = new ArrayList<String>();
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

    private static class RestarauntOpenHelper extends SQLiteOpenHelper {
        RestarauntOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + USERS_TABLE + "(id INTEGER PRIMARY KEY, name TEXT, password TEXT, email TEXT, home TEXT)");
            db.execSQL("CREATE TABLE " + FILTERS_TABLE + "(id INTEGER PRIMARY KEY, dollarRating TEXT, reviewRating TEXT, distanceRange TEXT," +
                    "cuisine TEXT )");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.w("Example", "Upgrading database; this will drop and recreate the tables.");
            db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
            onCreate(db);
        }
    }
}
