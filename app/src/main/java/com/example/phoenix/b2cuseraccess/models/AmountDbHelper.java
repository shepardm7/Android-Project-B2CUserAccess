package com.example.phoenix.b2cuseraccess.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteReadOnlyDatabaseException;

import java.util.ArrayList;

/**
 * Created by Phoenix on 14-Aug-17.
 */

public class AmountDbHelper extends SQLiteOpenHelper {


    public AmountDbHelper(Context context) {
        super(context, "C0698729_USERACCESS", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE AmountsTable (id INTEGER PRIMARY KEY, AmountValue REAL NOT NULL, Email TEXT NOT NULL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS AmountsTable";
        db.execSQL(sql);
        onCreate(db);
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
        while(c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("id"));
            double amt = c.getDouble(c.getColumnIndex("AmountValue"));
            amounts.add(id + " - " + amt);
        }
        return amounts;
    }
}
