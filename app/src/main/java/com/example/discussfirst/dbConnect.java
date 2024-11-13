package com.example.discussfirst;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dbConnect extends SQLiteOpenHelper {
    private static final String dbName = "UniConnectDB";
    private static final String dbTable = "users";
    private static final int dbVersion = 1;

    private static final String ID = "id";
    private static final String username = "fullname";
    private static final String email = "email";
    private static final String password = "password";

    public dbConnect(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create table SQL query
        String query = "CREATE TABLE " + dbTable + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + username + " TEXT, "
                + email + " TEXT, "
                + password + " TEXT)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Drop the old table if it exists and create a new one
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbTable);
        onCreate(sqLiteDatabase);
    }

    // Method to register a new user
    public boolean registerUser(String fullname, String email, String pwd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(username, fullname);
        values.put(this.email, email);
        values.put(password, pwd);

        long result = db.insert(dbTable, null, values);
        db.close();
        return result != -1; // returns true if insert is successful
    }

    // Method to login and validate credentials
    public boolean loginUser(String email, String pwd) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + dbTable + " WHERE " + this.email + " = ? AND " + password + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, pwd});

        boolean isLoggedIn = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isLoggedIn; // returns true if user exists with matching credentials
    }

    // Method to update user information
    public boolean updateUser(int id, String fullname, String email, String pwd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(username, fullname);
        values.put(this.email, email);
        values.put(password, pwd);

        int result = db.update(dbTable, values, ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0; // returns true if update is successful
    }

    // Method to delete a user
    public boolean deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(dbTable, ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0; // returns true if delete is successful
    }
}
