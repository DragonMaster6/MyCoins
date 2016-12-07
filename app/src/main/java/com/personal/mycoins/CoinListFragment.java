package com.personal.mycoins;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.personal.mycoins.database.CoinSchema;

/**
 * Author: Ben Matson
 * Date Created: 11/17/16
 * Last Edited: 12/2/16
 * Purpose: This will display to the user their coins sorted by year
 */

public class CoinListFragment extends Fragment{
    public static final int REQUEST_IMAGE_CAPTURE = 11;

    public static final int RESULT_OK = 1;

    private CoinListAdapter mCoinList;
    private Coin collection;
    private CoinListAdapter coinAdapter;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        // Creation of the Fragment
        // Unpack any arguments here
        collection = new Coin(getContext());
        coinAdapter = new CoinListAdapter(collection);      // default display of coins

        // setup the menu display
        setHasOptionsMenu(true);
    }

    // Have the option to create an instance method to initialize any arguments

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View v = inflater.inflate(R.layout.fragment_coinlist, container, false);

        // get a list of coins to display to the RecyclerView
        RecyclerView coinList = (RecyclerView) v.findViewById(R.id.list_of_coins);
        coinList.setAdapter(coinAdapter);
        coinList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set a divider for each item on the list
        RecyclerView.ItemDecoration dividerDecor = new DividerItemDecor(ContextCompat.getDrawable(getActivity(), R.drawable.coin_divider_small));
        coinList.addItemDecoration(dividerDecor);

        // Test to see if there is anything in the coin collection
        if(coinAdapter.getItemCount() <= 0)
            // Nothing, display a sad face message
            v.findViewById(R.id.no_coins_msg).setVisibility(View.VISIBLE);
        else
            v.findViewById(R.id.no_coins_msg).setVisibility(View.GONE);


        return v;
    }


    /** This will handle the menu display and actions **/
    @Override
    public void onCreateOptionsMenu(Menu m, MenuInflater inflater){
        inflater.inflate(R.menu.menu_coinlist, m);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // switch the item id to know what action to take
        switch(item.getItemId()){
            case R.id.menu_add_coin:
                // The user wants to add a new coin to the database.
                // Create a new fragment and display it to the screen
                Toast.makeText(getContext(), "Adding coin", Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }


    /** THis is the CoinList Adapter that handles the views of each item in the database **/
    public class CoinListAdapter extends RecyclerView.Adapter<CoinListAdapter.ViewHolder>{
        private Cursor coins;       // This is the collection of coins from the database

        // Constructor 1: get all the coins from the database
        public CoinListAdapter(Coin collection){
            coins = collection.getCoins();
        }
        // Constructor 2: get a specified set of coins from the database
        public CoinListAdapter(Coin collection, String criteria){
            coins = collection.getCoins(criteria);
        }

        @Override
        public void onBindViewHolder(final ViewHolder vh, int pos){
            // Move the cursor of the selection to the position of the coin
            coins.moveToPosition(pos);
            int id = coins.getInt(coins.getColumnIndexOrThrow(CoinSchema.coins._ID));
            vh.tempText.setText("Coin "+coins.getInt(coins.getColumnIndexOrThrow("coins_id")));

            // Setup a click listener to go to the detail fragment
            vh.container.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    // move the list back into position
                    coins.moveToPosition(vh.getAdapterPosition());

                    // Get the coin the user wants to see
                    int request = coins.getInt(coins.getColumnIndex(CoinSchema.coins.TABLE_NAME+CoinSchema.coins._ID));

                    // Get the template fragment instance and display it on screen
                    CoinTemplateFragment detailFrag = CoinTemplateFragment.getInstance(request, CoinTemplateFragment.TEMPLATE_READ);
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.coin_display, detailFrag).addToBackStack(null).commit();
                }
            });

        }

        @Override
        public int getItemCount(){
            return coins.getCount();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup container, int pos){
            View v = LayoutInflater.from(container.getContext()).inflate(R.layout.viewholder_coin, container,false);
            return new ViewHolder(v);
        }

        // Class to display an item to the list
        public class ViewHolder extends RecyclerView.ViewHolder{
            public ImageView frontImg;          // This will be the front of the selected coin
            public ImageView backImg;           // This is the back of the selected coin
            public TextView tempText;
            public LinearLayout container;
            public ViewHolder(View v){
                super(v);
                frontImg = (ImageView) v.findViewById(R.id.coin_front);
                backImg = (ImageView) v.findViewById(R.id.coin_back);
                tempText = (TextView) v.findViewById(R.id.temp_text);
                container = (LinearLayout) v.findViewById(R.id.coin_item_container);
            }
        }
    }

    /** Class to display a divider between items on a list **/
    public class DividerItemDecor extends RecyclerView.ItemDecoration{
        private Drawable mDivide;

        public DividerItemDecor(Drawable divider){
            mDivide = divider;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
            super.getItemOffsets(outRect, view, parent, state);

            if(parent.getChildAdapterPosition(view) == 0){
                // we don't want to draw a divider at the start of the list
                return;
            }

            outRect.top = mDivide.getIntrinsicHeight();
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state){
            int dividerLeft = parent.getPaddingLeft();
            int dividerRight = parent.getWidth() - parent.getPaddingRight();

            int count = parent.getChildCount();
            for(int i=0; i<count; i++){
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int dividerTop = child.getBottom()+params.bottomMargin;
                int dividerBottom = dividerTop+mDivide.getIntrinsicHeight();

                mDivide.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
                mDivide.draw(c);
            }
        }
    }
}
