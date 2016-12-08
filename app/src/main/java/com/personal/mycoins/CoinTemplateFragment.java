package com.personal.mycoins;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

        // Check the id argument if there is a coin to fetch
        if(args.getInt("id") != -1) {
            mEntry = collection.getCoin(args.getInt("id"));
            mEntry.moveToFirst();
        }else
            mEntry = null;

        mState = args.getInt("state");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        /** TODO: Make this more DRY **/
        final View v = inflater.inflate(R.layout.template_coinview, container, false);
        final Coin helper = new Coin(getContext());
        TextView typeField = (TextView) v.findViewById(R.id.coin_type_detail);
        TextView yearField = (TextView) v.findViewById(R.id.coin_year_detail);
        TextView mintField = (TextView) v.findViewById(R.id.coin_mint_detail);
        TextView countryField = (TextView) v.findViewById(R.id.coin_country_detail);
        TextView stateField = (TextView) v.findViewById(R.id.coin_usState_detail);
        TextView commentField = (TextView) v.findViewById(R.id.coin_comment_detail);
        Button actionButton = (Button) v.findViewById(R.id.coin_action_btn);

        // Based on the state the fragment is in, display the appropriate template
        switch(mState){
            case 1:
                // This is the write state. Replace the detail portions with EditViews for editing
                final Spinner typeEdit = (Spinner) v.findViewById(R.id.coin_type_edit);
                final EditText yearEdit = (EditText) v.findViewById(R.id.coin_year_edit);
                final RadioGroup mintEdit = (RadioGroup) v.findViewById(R.id.coin_mint_edit);
                final EditText countryEdit = (EditText) v.findViewById(R.id.coin_country_edit);
                final EditText stateEdit = (EditText) v.findViewById(R.id.coin_usState_edit);
                final AutoCompleteTextView commentEdit = (AutoCompleteTextView) v.findViewById(R.id.coin_comment_edit);
                actionButton.setText(getString(R.string.add_btn));

                // swap the textview with the edit view and fill in any details if necessary
                typeField.setVisibility(View.GONE);
                typeEdit.setVisibility(View.VISIBLE);
                    CursorAdapter dataAdapter = new CursorAdapter(getContext(), helper.getCoinTypes(), 0) {
                        @Override
                        public View newView(Context context, Cursor cursor, ViewGroup parent) {
                            return LayoutInflater.from(context).inflate(R.layout.viewholder_cointype, parent, false);
                        }

                        @Override
                        public void bindView(View view, Context context, Cursor cursor) {
                            TextView item = (TextView) view.findViewById(R.id.coin_type_view);
                            item.setText(cursor.getString(cursor.getColumnIndex(CoinSchema.coinTypes.COL_TITLE)));
                        }
                    };
                    typeEdit.setAdapter(dataAdapter);

                yearField.setVisibility(View.GONE);
                yearEdit.setVisibility(View.VISIBLE);

                mintField.setVisibility(View.GONE);
                mintEdit.setVisibility(View.VISIBLE);

                countryField.setVisibility(View.GONE);
                countryEdit.setVisibility(View.VISIBLE);

                stateField.setVisibility(View.GONE);
                stateEdit.setVisibility(View.VISIBLE);

                commentField.setVisibility(View.GONE);
                commentEdit.setVisibility(View.VISIBLE);

                // Setup the action button to add a coin
                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create a new ContentValues to capture information
                        ContentValues newCoin = new ContentValues();

                        // go through the fields and extract information
                        newCoin.put(CoinSchema.coins.COL_TYPEID,typeEdit.getSelectedItemId());
                        newCoin.put(CoinSchema.coins.COL_MINT, mintEdit.getCheckedRadioButtonId()==R.id.radio_mint_denver?"d":"p");
                        newCoin.put(CoinSchema.coins.COL_YEAR, yearEdit.getText().toString());
                        newCoin.put(CoinSchema.coins.COL_COUNTRY, countryEdit.getText().toString());
                        newCoin.put(CoinSchema.coins.COL_US_STATE, stateEdit.getText().toString());
                        newCoin.put(CoinSchema.coins.COL_COMMENT, commentEdit.getText().toString());

                        /** TODO: Do some value checking here **/
                        // now add the coin to the database
                        helper.addCoin(newCoin);

                        // Finally return back to the list
                        getFragmentManager().popBackStack();
                    }
                });



                break;
            case 2:
                // This is the read state. Just update the detail section with proper information
                typeField.setText(mEntry.getString(mEntry.getColumnIndex(CoinSchema.coinTypes.COL_TITLE)));
                yearField.setText(""+mEntry.getInt(mEntry.getColumnIndex(CoinSchema.coins.COL_YEAR)));
                String mint = mEntry.getString(mEntry.getColumnIndex(CoinSchema.coins.COL_MINT));
                mintField.setText(mint.equals("d")?getString(R.string.mint_d):getString(R.string.mint_p));
                countryField.setText(mEntry.getString(mEntry.getColumnIndex(CoinSchema.coins.COL_COUNTRY)));
                stateField.setText(mEntry.getString(mEntry.getColumnIndex(CoinSchema.coins.COL_US_STATE)));
                commentField.setText(mEntry.getString(mEntry.getColumnIndex(CoinSchema.coins.COL_COMMENT)));
                actionButton.setText(getString(R.string.edit_btn));
                break;
        }

        return v;
    }
}
