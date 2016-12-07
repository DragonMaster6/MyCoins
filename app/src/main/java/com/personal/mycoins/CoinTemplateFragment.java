package com.personal.mycoins;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.personal.mycoins.database.CoinSchema;

import org.w3c.dom.Text;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Author: Ben Matson
 * Date Created: 12/2/16
 * Last Edited: 12/2/16
 * Purpose: This class will allow Creation, Reads, and Updates to individual coins by following a basic layout
 *          replacing TextViews with EditViews and vice versa
 */

public class CoinTemplateFragment extends Fragment {
    public static final int TEMPLATE_WRITE = 1;
    public static final int TEMPLATE_READ = 2;

    private static CoinTemplateFragment sInstance;      // ensures that one of these is open at a time
    private int mState;     // Determines if EditViews are needed instead of TextViews
    private Cursor mEntry;    // Individual coin entry in the database

    // Create a simpleton
    public static CoinTemplateFragment getInstance(int coinEntry, int state){
        Bundle args = new Bundle();     // stores everything from the content values

        if(sInstance == null){
            // not existent, create a new one
            sInstance = new CoinTemplateFragment();
        }
        args.putInt("id",coinEntry);
        args.putInt("state",state);
        sInstance.setArguments(args);

        return sInstance;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        // unpack the arguments
        Bundle args = getArguments();
        Coin collection = new Coin(getContext());
        mEntry = collection.getCoin(args.getInt("id"));
            mEntry.moveToFirst();
        mState = args.getInt("state");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View v = inflater.inflate(R.layout.template_coinview, container, false);
        TextView typeField = (TextView) v.findViewById(R.id.coin_type_detail);
        TextView yearField = (TextView) v.findViewById(R.id.coin_year_detail);
        TextView mintField = (TextView) v.findViewById(R.id.coin_mint_detail);
        TextView countryField = (TextView) v.findViewById(R.id.coin_country_detail);
        TextView stateField = (TextView) v.findViewById(R.id.coin_usState_detail);
        TextView commentField = (TextView) v.findViewById(R.id.coin_comment_detail);

        switch(mState){
            case 1:
                // This is the write state. Replace the detail portions with EditViews for editing
                break;
            case 2:
                // This is the read state. Just update the detail section with proper information
                typeField.setText(mEntry.getString(mEntry.getColumnIndex(CoinSchema.coinTypes.COL_TITLE)));
                yearField.setText(""+mEntry.getInt(mEntry.getColumnIndex(CoinSchema.coins.COL_YEAR)));
                break;
        }

        return v;
    }
}
