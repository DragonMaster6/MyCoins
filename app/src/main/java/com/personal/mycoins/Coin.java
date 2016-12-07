package com.personal.mycoins;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.personal.mycoins.database.CoinDBHelper;
import com.personal.mycoins.database.CoinSchema;

/**
 * Author: Ben Matson
 * Date Created: 11/21/16
 * Last Edited: 11/28/16
 * Purpose: This file is the model class for the individual coins that will be added to the
 *          database. Including CRUD methods to update/add various coins in a user's collection
 */

public class Coin {
    private CoinDBHelper dbHelper;      // The database helper class used to access coins
    private SQLiteDatabase db;          // The database to access the coins

    public Coin(Context context){
        // Initialize anything here
        dbHelper = new CoinDBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    /** CREATE methods go here **/
    public boolean addCoin(ContentValues coin){
        long result;
        // connect to the database using a different thread

        // search through the database for a duplicate coin if it exists
        Cursor duplicate = db.rawQuery("SELECT * FROM "+CoinSchema.coins.TABLE_NAME+" WHERE "+
            CoinSchema.coins.COL_TYPEID+"="+coin.getAsInteger(CoinSchema.coins.COL_TYPEID)+" AND "+
            CoinSchema.coins.COL_COUNTRY+"=\""+coin.getAsString(CoinSchema.coins.COL_COUNTRY)+"\" AND "+
            CoinSchema.coins.COL_US_STATE+"=\""+coin.getAsString(CoinSchema.coins.COL_US_STATE)+"\" AND "+
            CoinSchema.coins.COL_MINT+"=\""+coin.getAsString(CoinSchema.coins.COL_MINT)+"\" AND "+
            CoinSchema.coins.COL_YEAR+"="+coin.getAsInteger(CoinSchema.coins.COL_YEAR), null);

        if(duplicate.getCount() == 0){
            result = db.insert(CoinSchema.coins.TABLE_NAME, null, coin);
        }else {
            // insert the values of the coin into the database
            result = -1;
        }

        duplicate.close();
        return (result > -1);
    }

    /** READ methods go here **/

    // method that gets all the coins from the database
    public Cursor getCoins(){
        return db.rawQuery("SELECT "+CoinSchema.coins.TABLE_NAME+"."+CoinSchema.coins._ID+" AS coins_id,* FROM "+CoinSchema.coins.TABLE_NAME+","+CoinSchema.coinTypes.TABLE_NAME+" WHERE "+
                CoinSchema.coins.COL_TYPEID+"="+CoinSchema.coinTypes.TABLE_NAME+"."+CoinSchema.coinTypes._ID, null);
    }


    // Method that gets only a selected view designated by the user
    public Cursor getCoins(String criteria){
        return null;
    }


    // Method that gets an individual coin
    public Cursor getCoin(int id){
        return db.rawQuery("SELECT "+CoinSchema.coins.TABLE_NAME+"."+CoinSchema.coins._ID+" AS coins_ID, * FROM "+CoinSchema.coins.TABLE_NAME+","+CoinSchema.coinTypes.TABLE_NAME+" WHERE "+
            CoinSchema.coins.TABLE_NAME+"."+CoinSchema.coins._ID+"="+id+" AND "+
                CoinSchema.coins.COL_TYPEID+"="+CoinSchema.coinTypes.TABLE_NAME+"."+CoinSchema.coinTypes._ID, null);
    }

    /** UPDATE methods go here **/
    public boolean updateCoin(int id){
        return false;
    }

    /** DELETE methods go here **/
    public boolean deleteCoin(int id){
        return false;
    }
}
