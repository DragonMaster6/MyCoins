package com.personal.mycoins.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Author: Ben Matson
 * Date Created: 11/21/16
 * Last Edited: 12/1/16
 * Purpose: This file controls the database versions and modifications
 */

public class CoinDBHelper extends SQLiteOpenHelper{
    private static CoinDBHelper sInstance;
    // The following constants will help setup the tables as defined in the schema
    private static final String CREATE_COINS_TABLE = "CREATE TABLE "+
            CoinSchema.coins.TABLE_NAME+" ("+
            CoinSchema.coins._ID+" INTEGER PRIMARY KEY, "+
            CoinSchema.coins.COL_TYPEID+" INTEGER NOT NULL, "+
            CoinSchema.coins.COL_MINT+" CHAR, "+
            CoinSchema.coins.COL_COUNTRY+" VARCHAR(64), "+
            CoinSchema.coins.COL_US_STATE+" VARCHAR(64), "+
            CoinSchema.coins.COL_YEAR+" INTEGER, "+
            CoinSchema.coins.COL_COMMENT+" TEXT"+
            ")";

    private static final String CREATE_COINTYPES_TABLE = "CREATE TABLE "+
            CoinSchema.coinTypes.TABLE_NAME+" ("+
            CoinSchema.coinTypes._ID+" INTEGER PRIMARY KEY, "+
            CoinSchema.coinTypes.COL_TITLE+" VARCHAR(32), "+
            CoinSchema.coinTypes.COL_VALUE+" DOUBLE"+
            ")";

    private static final String DELETE_COINS_TABLE = "DROP TABLE IF EXISTS "+CoinSchema.coins.TABLE_NAME;
    private static final String DELETE_COINTYPES_TABLE = "DROP TABLE IF EXISTS "+CoinSchema.coinTypes.TABLE_NAME;

    // These variables help keep track of versions and the filename
    private static int VERSION = 2;
    private static String NAME = "CoinDB.db";

    public static CoinDBHelper getInstance(Context context){
        if(sInstance == null){
            sInstance = new CoinDBHelper(context);
        }

        return sInstance;
    }
    private CoinDBHelper(Context context){
        super(context,NAME, null, VERSION);
    }

    // This is the creation of the db
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_COINS_TABLE);
        db.execSQL(CREATE_COINTYPES_TABLE);

    }

    // This method establishes that there is a new DB and to delete the old for the new one
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV){
        // Check the version and update the table as needed
        switch(VERSION){
            case 2:
                // Support for images
                db.execSQL("ALTER TABLE "+CoinSchema.coins.TABLE_NAME+" ADD "+CoinSchema.coins.COL_IMG_FRONT+" TEXT");
                db.execSQL("ALTER TABLE "+CoinSchema.coins.TABLE_NAME+" ADD "+CoinSchema.coins.COL_IMG_BACK+" TEXT");
            default:
                break;
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldV, int newV){
        db.execSQL(DELETE_COINS_TABLE);
        db.execSQL(DELETE_COINTYPES_TABLE);
        onCreate(db);

    }

}
