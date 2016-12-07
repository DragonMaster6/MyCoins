package com.personal.mycoins;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.personal.mycoins.database.CoinSchema;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertThat;

/**
 * Author: Ben Matson
 * Date Created: 11/21/16
 * Last Edited: 11/21/16
 * Purpose: This file will define the tests for the Coin model to ensure proper functionality
 *          Remember: FIRST -> Fast, Independent, Repeatable, Self-checking, Timely
 *                    SMART -> Simple, Measurable,
 */

@RunWith(AndroidJUnit4.class)
public class AndroidCoinUnitTest {
    private Coin coinList;
    private int year;
    private String country;
    private String state;
    private double value;
    private char mint;
    private String type;

    @Before
    public void setup(){
        coinList = new Coin(InstrumentationRegistry.getContext());
    }

    @After
    public void cleanup(){
        coinList = null;
    }

    // Test the CRUD methods of the Coin model

    // CREATE tests
    @Test
    public void should_add_coin_to_database(){
        ContentValues newCoin = new ContentValues();
        newCoin.put(CoinSchema.coins.COL_YEAR, 2016);
        newCoin.put(CoinSchema.coins.COL_US_STATE, "Colorado");
        newCoin.put(CoinSchema.coins.COL_COUNTRY, "United States");
        newCoin.put(CoinSchema.coins.COL_TYPEID, 1);
        newCoin.put(CoinSchema.coins.COL_MINT, "D");

        boolean result = coinList.addCoin(newCoin);

        assertThat(result, is(true));
    }


    // READ tests


    // UPDATE tests


    // DELETE tests
}
