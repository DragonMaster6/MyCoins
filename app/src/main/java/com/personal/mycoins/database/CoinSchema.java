package com.personal.mycoins.database;

import android.provider.BaseColumns;

/**
 * Author: Ben Matson
 * Date Created: 11/21/16
 * Last Edited: 11/21/16
 * Purpose: This file holds the table structure of the app. Refer to this to add/remove columns
 *
 */

public class CoinSchema {
    //public static final String ALL_COLUMNS = coins.TABLE_NAME
    // blank constructor
    private CoinSchema(){}

    // Contract class to define the first primary table
    public static abstract class coins implements BaseColumns{
        public static final String TABLE_NAME = "coins";
        public static final String COL_TYPEID = "typeID";
        public static final String COL_YEAR = "year";
        public static final String COL_MINT = "mint";
        public static final String COL_COUNTRY = "country";
        public static final String COL_US_STATE = "usState";
        public static final String COL_COMMENT = "comment";
        public static final String COL_IMG_FRONT = "front";
        public static final String COL_IMG_BACK = "back";
    }

    // Contract class to define the types of coins available
    public static abstract class coinTypes implements BaseColumns{
        public static final String TABLE_NAME = "coinTypes";
        public static final String COL_TITLE = "title";
        public static final String COL_VALUE = "value";
    }
}
