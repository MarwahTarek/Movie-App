package com.example.android.movieapp.app;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    //Function that checks internet connection.
    private boolean haveNetworkConnection()
    {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for (NetworkInfo nw : netInfo) {
            if (nw.getTypeName().equalsIgnoreCase("WIFI")) {
                if (nw.isConnected()) {
                    haveConnectedWifi = true;
                }
            }
            if (nw.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (nw.isConnected()) {
                    haveConnectedMobile = true;
                }
            }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(LOG_TAG,"onCreate() start");

        if(!haveNetworkConnection())
        {
            Log.d(LOG_TAG,"onCreate() NO connection");
            Toast.makeText(this,"No Internet connection",Toast.LENGTH_LONG).show();

            //Calling this method to close this activity when internet is not available.
            Log.d(LOG_TAG,"onCreate() finish");

            finish();
        }

        Log.d(LOG_TAG,"onCreate() after finish");
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainActivityFragment())
                    .commit();
            Log.v(LOG_TAG,"onCreate() if");
        }
        Log.v(LOG_TAG,"onCreate() end");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_sort_by)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
