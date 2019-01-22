package com.example.balaji.myweatherapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.balaji.myweatherapp.weatherSearch.WeatherFragment;

public class MainActivity extends AppCompatActivity {

    private WeatherFragment weatherFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ContentLoadingProgressBar mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressDialog = findViewById(R.id.progressBar);
        initializeWeatherFragment();
    }

    public void showDialog() {
        if (mProgressDialog != null)
            mProgressDialog.show();
    }

    public void hideDialog() {
        if (mProgressDialog != null)
            mProgressDialog.hide();
    }

    public void showInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("TURN INTERNET ON");
        builder.setMessage("You have no internet connection, kindly please turn on the Internet");
        builder.setPositiveButton("TURN ON", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(i);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void initializeWeatherFragment() {
        weatherFragment = WeatherFragment.newInstance(MainActivity.this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction = fragmentTransaction.replace(R.id.theContainerView, weatherFragment, weatherFragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }
}
