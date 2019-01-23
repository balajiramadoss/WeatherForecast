package com.example.balaji.myweatherapp.service;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.balaji.myweatherapp.MainActivity;
import com.example.balaji.myweatherapp.listeners.ApiListener;
import com.example.balaji.myweatherapp.models.SearchItem;
import com.example.balaji.myweatherapp.models.WeatherInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class VolleyService {

    public static final String HOME_FORECAST_URL = "http://api.apixu.com/v1/forecast.json?";
    public static final String HOME_SUGGESTION_URL = "http://api.apixu.com/v1/search.json?";
    private static final String KEY = "099fdd6a96574c3983982724192101";
    private static VolleyService service;
    private Context context;
    private WeatherInfo details;
    private List<SearchItem> searchItems;
    private MainActivity activity;


    private VolleyService(Context context, MainActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    public static VolleyService getInstance(Context context, MainActivity activity) {
        if (service == null) {
            service = new VolleyService(context, activity);
        }
        return service;
    }

    public void getWeatherDetails(String searchKey, final ApiListener<WeatherInfo> listener) {
        String url = HOME_FORECAST_URL + "key=" + KEY + "&q=" + searchKey + "&days=7";
        activity.showDialog();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    activity.hideDialog();
                    Log.d("Response is", response);
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    details = gson.fromJson(response, WeatherInfo.class);
                    listener.onSuccess(details);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                activity.hideDialog();
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 400:
                            Toast.makeText(context, "OOPS!! NO CITIES FOUND!!", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
                Log.d("Error", error.toString());
                Log.d("<<<ERROR CODE>>>>", String.valueOf(error.networkResponse.statusCode));
                // Toast.makeText(context, "ERROR!!!!"+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueueTon.getInstance(context).getRequestQueue().add(request);
    }

    public void getWeatherSuggestions(String searchKey, final ApiListener<List<SearchItem>> listener) {
        String url = HOME_SUGGESTION_URL + "key=" + KEY + "&q=" + searchKey;
        activity.showDialog();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    activity.hideDialog();
                    Log.d("Response is", response);
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    searchItems = gson.fromJson(response, new TypeToken<List<SearchItem>>() {
                    }.getType());
                    listener.onSuccess(searchItems);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                activity.hideDialog();
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 400:
                            Toast.makeText(context, "OOPS!! NO CITIES FOUND!!", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
                Log.d("Error", error.toString());
                if (error.networkResponse != null && error.networkResponse.statusCode != 0)
                    Log.d("<<<ERROR CODE>>>>", String.valueOf(error.networkResponse.statusCode));
                // Toast.makeText(context, "ERROR!!!!"+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueueTon.getInstance(context).getRequestQueue().add(request);
    }
}
