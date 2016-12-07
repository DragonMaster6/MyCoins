package com.personal.mycoins;

/*
 * Programmer: Ben Matson
 * Created on: Nov 4, 2016
 * Last Edited: 11/17/16
 * Purpose: Starting point of the app where the user will be greeted with all their coins
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup the Fragment manager to start displaying coins
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentById(R.id.coin_display);
        if(frag == null){
            CoinListFragment coinFrag = new CoinListFragment();
            fm.beginTransaction().add(R.id.coin_display, coinFrag).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();

        return true;
    }
}
