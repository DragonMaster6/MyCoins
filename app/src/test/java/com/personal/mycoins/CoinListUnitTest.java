package com.personal.mycoins;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.personal.mycoins.database.CoinSchema;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.BaseCursor;
import org.robolectric.fakes.RoboCursor;
import org.robolectric.internal.Shadow;
import org.robolectric.shadows.ShadowAbstractCursor;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowCursorWrapper;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Author: Ben Matson
 * Date Created: 11/21/16
 * Last Edited: 11/21/16
 * Purpose:
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)

public class CoinListUnitTest {
    private Coin coinList;
    private ContentValues newCoin;
    private int year;
    private String country;
    private String state;
    private double value;
    private char mint;
    private String type;

    @Before
    public void setup(){
        ShadowApplication app = Shadows.shadowOf(RuntimeEnvironment.application);
        coinList = new Coin(app.getApplicationContext());
        newCoin = new ContentValues();
        newCoin.put(CoinSchema.coins._ID, 1);
        newCoin.put(CoinSchema.coins.COL_YEAR, 2004);
        newCoin.put(CoinSchema.coins.COL_US_STATE, "Colorado");
        newCoin.put(CoinSchema.coins.COL_MINT, "D");
        newCoin.put(CoinSchema.coins.COL_COUNTRY, "United States");
        newCoin.put(CoinSchema.coins.COL_TYPEID, 1);
        coinList.addCoin(newCoin);

    }

    @After
    public void cleanup(){
        coinList = null;
    }

    // Test the CRUD methods of the Coin model

    /** CREATE tests **/
    // Should simply add a coin to the database
    @Test
    public void should_add_new_coin_to_database(){
        /**TODO: add a feature to check for duplicate coins **/
        ContentValues newCoin = new ContentValues();
        newCoin.put(CoinSchema.coins.COL_YEAR, 2016);
        newCoin.put(CoinSchema.coins.COL_US_STATE, "Colorado");
        newCoin.put(CoinSchema.coins.COL_COUNTRY, "United States");
        newCoin.put(CoinSchema.coins.COL_TYPEID, 1);
        newCoin.put(CoinSchema.coins.COL_MINT, "D");

        boolean result = coinList.addCoin(newCoin);

        assertThat(result, is(true));
    }
    // Test to ensure that duplicate coins are not made
    @Test
    public void shouldnt_add_duplicate_coins(){
        ContentValues newCoin = new ContentValues();
        newCoin.put(CoinSchema.coins.COL_YEAR, 2004);
        newCoin.put(CoinSchema.coins.COL_US_STATE, "Colorado");
        newCoin.put(CoinSchema.coins.COL_MINT, "D");
        newCoin.put(CoinSchema.coins.COL_COUNTRY, "United States");
        newCoin.put(CoinSchema.coins.COL_TYPEID, 1);

        boolean result = coinList.addCoin(newCoin);
        assertThat(result, is(false));
    }


    /** READ tests **/
    // Must test the database to extract all the coins
    @Test
    public void should_return_list_of_all_coins(){
        Cursor cursor = coinList.getCoins();

        assertThat(cursor.getCount(), is(1));
    }

    // Must test the database to extract a specific set of coins
    @Test
    public void should_return_list_of_specific_coins(){
        Assert.fail("Needs Implementing");
    }


    // Test that the database extracts one coin based on an id
    @Test
    public void should_return_one_specific_coin(){
        Cursor coin = coinList.getCoin(1);

        coin.moveToFirst();
        assertThat(coin.getString(coin.getColumnIndex(CoinSchema.coins.COL_COUNTRY)), is(newCoin.getAsString(CoinSchema.coins.COL_COUNTRY)));
    }


    // UPDATE tests


    // DELETE tests

}
