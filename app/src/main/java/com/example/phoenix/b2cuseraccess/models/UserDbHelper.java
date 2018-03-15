package com.example.phoenix.b2cuseraccess.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Phoenix on 13-Aug-17.
 */

public class UserDbHelper extends SQLiteOpenHelper {

    public UserDbHelper(Context context) {
        super(context, "C0698729_USERACCESS", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "CREATE TABLE Users (id INTEGER PRIMARY KEY, Email TEXT, Password TEXT, Phone TEXT)";
        String sql2 = "CREATE TABLE AmountsTable (id INTEGER PRIMARY KEY, AmountValue REAL NOT NULL, Email TEXT NOT NULL)";
        db.execSQL(sql1);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql1 = "DROP TABLE IF EXISTS Users";
        String sql2 = "DROP TABLE IF EXISTS AmountsTable";
        db.execSQL(sql1);
        db.execSQL(sql2);
        onCreate(db);
    }

    public boolean insertUser(String email, String pass, String phone) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM Users WHERE Email = '" + email + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            return false;
        }
        ContentValues userData = new ContentValues();
        userData.put("Email", email);
        userData.put("Password", pass);
        userData.put("Phone", phone);
        db.insert("Users", null, userData);
//        String sql2 = "SELECT * FROM Users";
//        cursor = db.rawQuery(sql2, null);
//        while(cursor.moveToNext()) {
//            Log.d("email: ", cursor.getString(cursor.getColumnIndex("Email")));
//            Log.d("pass: ", cursor.getString(cursor.getColumnIndex("Password")));
//        }
        return true;
    }

    public boolean checkUserCredentials(String email, String pass) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM Users WHERE Email = '" + email + "' AND Password = '" + pass + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public void insertAmount(double amount) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues amountData = new ContentValues();
        amountData.put("AmountValue", amount);
        amountData.put("Email", UserSession.getSession());
        db.insert("AmountsTable", null, amountData);
    }

    public ArrayList<String> getAmounts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM AmountsTable WHERE Email = '" + UserSession.getSession() + "'", null);
        ArrayList<String> amounts = new ArrayList<>();
        int specificId = 1;
        while(c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("id"));
            double amt = c.getDouble(c.getColumnIndex("AmountValue"));
            amounts.add(specificId + " - " + amt);
            specificId++;
        }
        return amounts;
    }
}
