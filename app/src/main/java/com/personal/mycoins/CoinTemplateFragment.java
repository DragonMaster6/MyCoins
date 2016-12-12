package com.personal.mycoins;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
 * Last Edited: 12/12/16
 * Purpose: This class will allow Creation, Reads, and Updates to individual coins by following a basic layout
 *          replacing TextViews with EditViews and vice versa
 */

public class CoinTemplateFragment extends Fragment {
    public static final int TEMPLATE_WRITE = 1;
    public static final int TEMPLATE_READ = 2;
    
    private int mState;     // Determines if EditViews are needed instead of TextViews
    private Cursor mEntry;    // Individual coin entry in the database
    private Coin mCollection;    // Access to the database

    // Create a simpleton
    public static CoinTemplateFragment getInstance(int coinEntry, int state){
        Bundle args = new Bundle();     // stores everything from the content values
        CoinTemplateFragment result = new CoinTemplateFragment();
        args.putInt("id",coinEntry);
        args.putInt("state", state);
        result.setArguments(args);

        return result;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        // unpack the arguments
        final Bundle args = getArguments();
        mCollection = new Coin(getContext());

        // Check the id argument if there is a coin to fetch
        if(args.getInt("id") != -1) {
            mEntry = mCollection.getCoin(args.getInt("id"));
            mEntry.moveToFirst();
        }else
            mEntry = null;

        mState = args.getInt("state");
    }

    public interface OnDataSavedListener{
        public void onDataSaved(ContentValues values);
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
                actionButton.setText(mEntry==null?getString(R.string.add_btn):getString(R.string.update_btn));

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
                typeEdit.setSelection(mEntry==null?0:mEntry.getInt(mEntry.getColumnIndex(CoinSchema.coins.COL_TYPEID)));

                changeToEdit(yearField, yearEdit, mEntry==null?null:mEntry.getString(mEntry.getColumnIndex(CoinSchema.coins.COL_YEAR)));
                changeToEdit(countryField, countryEdit, mEntry==null?null:mEntry.getString(mEntry.getColumnIndex(CoinSchema.coins.COL_COUNTRY)));
                changeToEdit(stateField, stateEdit, mEntry==null?null:mEntry.getString(mEntry.getColumnIndex(CoinSchema.coins.COL_US_STATE)));
                changeToEdit(commentField, commentEdit, mEntry==null?"NA":mEntry.getString(mEntry.getColumnIndex(CoinSchema.coins.COL_COMMENT)));
                changeToEdit(mintField, mintEdit);

                // Setup the action button to add a coin
                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create a new ContentValues to capture information
                        ContentValues newCoin = new ContentValues();
                        int id = mEntry==null?-1:mEntry.getInt(mEntry.getColumnIndex(CoinSchema.coins.TABLE_NAME+CoinSchema.coins._ID));

                        /** TODO: Do some value checking here **/

                        // Once the information has been complete, prepare for the addition

                        newCoin.put(CoinSchema.coins.COL_TYPEID,typeEdit.getSelectedItemId());
                        newCoin.put(CoinSchema.coins.COL_MINT, mintEdit.getCheckedRadioButtonId()==R.id.radio_mint_denver?"d":"p");
                        newCoin.put(CoinSchema.coins.COL_YEAR, yearEdit.getText().toString().equals("")?6:Integer.parseInt(yearEdit.getText().toString()));
                        newCoin.put(CoinSchema.coins.COL_COUNTRY, countryEdit.getText().toString());
                        newCoin.put(CoinSchema.coins.COL_US_STATE, stateEdit.getText().toString());
                        newCoin.put(CoinSchema.coins.COL_COMMENT, commentEdit.getText().toString());

                        // now add or edit the coin to the database
                        if(mEntry == null)
                            helper.addCoin(newCoin);
                        else {
                            helper.updateCoin(newCoin, id);
                        }

                        // Finally return back to the list
                        getFragmentManager().popBackStack();
                        //getFragmentManager().beginTransaction().replace(R.id.coin_display, CoinTemplateFragment.getInstance(id,TEMPLATE_READ)).addToBackStack(null).commit();
                    }
                });



                break;
            case 2:
                // This is the read state. Just update the detail section with proper information
                // First update the coin entry just in case data has changed
                Log.d("ID COIN","["+mEntry.getInt(mEntry.getColumnIndex(CoinSchema.coins.TABLE_NAME+CoinSchema.coins._ID))+"]");
                mEntry = mCollection.getCoin(mEntry.getInt(mEntry.getColumnIndex(CoinSchema.coins.TABLE_NAME+CoinSchema.coins._ID)));
                mEntry.moveToFirst();

                // Update the fields now
                typeField.setText(mEntry.getString(mEntry.getColumnIndex(CoinSchema.coinTypes.COL_TITLE)));
                yearField.setText(""+mEntry.getInt(mEntry.getColumnIndex(CoinSchema.coins.COL_YEAR)));
                String mint = mEntry.getString(mEntry.getColumnIndex(CoinSchema.coins.COL_MINT));
                mintField.setText(mint.equals("d")?getString(R.string.mint_d):getString(R.string.mint_p));
                countryField.setText(mEntry.getString(mEntry.getColumnIndex(CoinSchema.coins.COL_COUNTRY)));
                stateField.setText(mEntry.getString(mEntry.getColumnIndex(CoinSchema.coins.COL_US_STATE)));
                commentField.setText(mEntry.getString(mEntry.getColumnIndex(CoinSchema.coins.COL_COMMENT)));
                actionButton.setText(getString(R.string.edit_btn));

                // setup a click listener for the user to edit that particular coin
                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // user wants to edit, prepare a new fragment
                        Log.d("EDIT BUTTON","CLICKED");
                        getFragmentManager().beginTransaction()
                                .replace(R.id.coin_display, getInstance(mEntry.getInt(mEntry.getColumnIndex(CoinSchema.coins.TABLE_NAME+CoinSchema.coins._ID)), TEMPLATE_WRITE),"edit")
                                .addToBackStack(null).commit();
                    }
                });
                break;
        }

        return v;
    }

    /** Misc Functions **/
    // The EditView version of editing
    private void changeToEdit(View detail, EditText edit, String data){
        detail.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);
        // Test to see if we have any data to fill
        if(data != null && !data.isEmpty()){
            edit.setText(data);
        }
    }

    // RadioGroup version of editing
    private void changeToEdit(View detail, RadioGroup edit){
        detail.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);
        if(mEntry != null){
            edit.check(mEntry.getString(mEntry.getColumnIndex(CoinSchema.coins.COL_MINT)).equalsIgnoreCase("d")?R.id.radio_mint_denver:R.id.radio_mint_penn);
        }else{
            edit.check(R.id.radio_mint_denver);
        }
    }
}
