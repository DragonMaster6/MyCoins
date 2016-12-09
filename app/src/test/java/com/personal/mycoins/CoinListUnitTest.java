package com.personal.mycoins;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.personal.mycoins.database.CoinDBHelper;
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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        CoinDBHelper db = CoinDBHelper.getInstance(app.getApplicationContext());
        coinList = new Coin(app.getApplicationContext());
        newCoin = new ContentValues();
        newCoin.put(CoinSchema.coins._ID, 1);
        newCoin.put(CoinSchema.coins.COL_YEAR, 2004);
        newCoin.put(CoinSchema.coins.COL_US_STATE, "Colorado");
        newCoin.put(CoinSchema.coins.COL_MINT, "D");
        newCoin.put(CoinSchema.coins.COL_COUNTRY, "United States");
        newCoin.put(CoinSchema.coins.COL_TYPEID, 1);
        coinList.addCoin(newCoin);

        SQLiteDatabase helper = db.getWritableDatabase();
        ContentValues type = new ContentValues();
        type.put("title", "Quarter");
        type.put("title", "Nickel");

        helper.insert(CoinSchema.coinTypes.TABLE_NAME, null, type);

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

    // Test the coin types return a list of titles and their values
    @Test
    public void should_return_list_of_coin_types(){
        Cursor titles = coinList.getCoinTypes();

        assertNotNull("Nullable Object", titles);
        assertThat(titles.getColumnName(1), is(CoinSchema.coinTypes.COL_TITLE));
        assertThat(titles.getColumnName(2), is(CoinSchema.coinTypes.COL_VALUE));
        assertThat(titles.getCount(), is(not(0)));
    }


    /** UPDATE tests **/
    // Test that a coin can be updated
    @Test
    public void should_be_able_to_update_a_single_coin(){
        ContentValues newCoin = new ContentValues();
        String newCountry = "United Kingdom";
        Cursor oldCoin = coinList.getCoin(0);
        oldCoin.moveToFirst();
        String oldCountry = oldCoin.getString(oldCoin.getColumnIndex(CoinSchema.coins.COL_COUNTRY));

        newCoin.put(CoinSchema.coins.COL_COUNTRY, newCountry);

        boolean result = coinList.updateCoin(newCoin);
        Cursor updatedCoin = coinList.getCoin(0);
        updatedCoin.moveToFirst();

        assertTrue(result);
        assertThat(updatedCoin.getString(updatedCoin.getColumnIndex(CoinSchema.coins.COL_COUNTRY)), is(newCountry));

    }


    /** DELETE tests **/

    // Test that a given coin can be deleted from the database
    @Test
    public void should_delete_a_specified_coin_from_collection(){
        assertThat(coinList.deleteCoin(1), is(true));     // deletes the coin in the setup function
    }


}
