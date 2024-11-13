package com.example.discussfirst;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//more left to do
public class dbConnect extends SQLiteOpenHelper {
    private static String dbName = "DiscussionDB";
    private static int dbVersion = 1;

    public dbConnect(@Nullable Context context) {
        super(context,dbName,null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
