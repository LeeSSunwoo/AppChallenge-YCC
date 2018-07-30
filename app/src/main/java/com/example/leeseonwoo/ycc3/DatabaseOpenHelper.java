package com.example.leeseonwoo.ycc3;

/**
 * Created by leeseonwoo on 2018. 7. 20..
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String TAG = "DatabaseOpenHelper";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UserDATA.db";
    public static final String TABLE_NAME1 = "UserDATA";


    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable1 = "create table "+TABLE_NAME1+" (_id integer PRIMARY KEY autoincrement, ID text, Password text, Name text, Number text, Email text, gender integer, Login text);";
        try{
            db.execSQL(createTable1);
            Log.d(TAG, "Create Table UserDATA");
        }catch (Exception ex){
            Log.e(TAG, "Exception in CREATE_SQL", ex);
        }
        Log.w(TAG, "Create database");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.w(TAG, "opened database")
        ;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ".");
    }
}