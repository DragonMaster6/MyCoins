package com.personal.mycoins;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
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

import com.personal.mycoins.database.CoinDBHelper;
import com.personal.mycoins.database.CoinSchema;

/**
 * Author: Ben Matson
 * Date Created: 11/17/16
 * Last Edited: 12/8/16
 * Purpose: This will display to the user their coins sorted by year
 */

public class CoinListFragment extends Fragment{
    public static final int REQUEST_IMAGE_CAPTURE = 11;

    public static final int RESULT_OK = 1;

    private Coin collection;
    private CoinListAdapter coinAdapter;
    private boolean mCoinUpdate;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        // Creation of the Fragment
        // Unpack any arguments here
        Log.d("FRAGMENT","STARTING UP");

        // setup the menu display
        setHasOptionsMenu(true);
    }

    // Have the option to create an instance method to initialize any arguments

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View v = inflater.inflate(R.layout.fragment_coinlist, container, false);

        // get a list of coins from the database so that it refreshes the display
        RecyclerView coinList = (RecyclerView) v.findViewById(R.id.list_of_coins);
        collection = new Coin(getContext());
        coinAdapter = new CoinListAdapter(collection);      // default display of coins

        // set the collection adapter to the view
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
                //Toast.makeText(getContext(), "Adding coin", Toast.LENGTH_LONG).show();
                FragmentManager fm = getFragmentManager();
                CoinTemplateFragment frag = CoinTemplateFragment.getInstance(-1, CoinTemplateFragment.TEMPLATE_WRITE);
                getFragmentManager().beginTransaction().replace(R.id.coin_display, frag).addToBackStack(null).commit();
                break;
        }

        return true;
    }


    /** THis is the CoinList Adapter that handles the views of each item in the database **/
    public class CoinListAdapter extends RecyclerView.Adapter<CoinListAdapter.ViewHolder>{
        private Cursor coins;       // This is the collection of coins from the database
        private Coin collection;    // This is used to help remove coins from the database

        // Constructor 1: get all the coins from the database
        public CoinListAdapter(Coin collection){
            coins = collection.getCoins();
            this.collection = collection;
        }
        // Constructor 2: get a specified set of coins from the database
        public CoinListAdapter(Coin collection, String criteria){
            coins = collection.getCoins(criteria);
        }

        @Override
        public void onBindViewHolder(final ViewHolder vh, int pos){
            // Move the cursor of the selection to the position of the coin
            coins.moveToPosition(pos);
            final int id = coins.getInt(coins.getColumnIndexOrThrow(CoinSchema.coins.TABLE_NAME+CoinSchema.coins._ID));
            // Set the coin type
            switch(coins.getInt(coins.getColumnIndex(CoinSchema.coins.COL_TYPEID))){
                case 0:
                    // This is a Quarter
                    vh.frontText.setText(R.string.coin_quarter);
                    break;
                case 1:
                    // This is a Dime
                    vh.frontText.setText(R.string.coin_dime);
                    break;
                case 2:
                    // This is a Nickel
                    vh.frontText.setText(R.string.coin_nickel);
                    break;
                case 3:
                    // This is a penny
                    vh.frontText.setText(R.string.coin_penny);
                    break;
                default:
                    vh.frontText.setText(R.string.coin_other);
                    break;
            }
            // Set the year
            vh.backText.setText(""+coins.getInt(coins.getColumnIndex(CoinSchema.coins.COL_YEAR)));


            /** TODO: If there is no image to work with set a textview ontop of the default image to
             *        display the coin's type and year for easy reading
             */
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

            vh.container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(getContext(), "I've been long clicked", Toast.LENGTH_LONG).show();
                    vh.deleteBtn.setVisibility(View.VISIBLE);
                    vh.container.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.item_highlight));
                    vh.deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), "Deleting button", Toast.LENGTH_LONG).show();
                            // Remove the coin from the database
                            Log.d("COIN ID",""+id);
                            collection.deleteCoin(id);

                            // remove the delete button from view
                            vh.deleteBtn.setVisibility(View.GONE);

                            // Notify that the dataset changed
                            refreshData();
                            coinAdapter.notifyItemRemoved(vh.getAdapterPosition());
                            coinAdapter.notifyDataSetChanged();
                        }
                    });

                    // Ensures that we don't have a double click trigger happening at the same time
                    return true;
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
            container.setClickable(true);

            return new ViewHolder(v);
        }

        public void refreshData(){
            // NOTE: This will break the search features; be sure to save search criteria
            coins = collection.getCoins();
        }

        // Class to display an item to the list
        public class ViewHolder extends RecyclerView.ViewHolder{
            public ImageView frontImg;          // This will be the front of the selected coin
            public ImageView backImg;           // This is the back of the selected coin
            public ImageView deleteBtn;         // This button will activate when the user long presses the viewholder
            public TextView frontText;          // This will display the Coin type in case there is no image to view
            public TextView backText;           // This will display the Coin year in case there is no image to view
            public LinearLayout container;
            public ViewHolder(View v){
                super(v);
                frontImg = (ImageView) v.findViewById(R.id.coin_front);
                backImg = (ImageView) v.findViewById(R.id.coin_back);
                frontText = (TextView) v.findViewById(R.id.coin_front_text);
                backText = (TextView) v.findViewById(R.id.coin_back_text);
                deleteBtn = (ImageView) v.findViewById(R.id.coin_delete_btn);
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

    /** A Loader that will help synchronise any new new coins added to the database **/
    public static class CoinLoader extends CursorLoader{
        private Coin mCollection;
        public CoinLoader(Context context){
            super(context);
            mCollection = new Coin(context);
        }

        // The method that gets coins from the background
        @Override
        public Cursor loadInBackground(){
            return mCollection.getCoins();
        }
    }
}
