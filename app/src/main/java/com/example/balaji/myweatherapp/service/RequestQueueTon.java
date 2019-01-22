package com.example.balaji.myweatherapp.service;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueTon {

    private static RequestQueueTon queue;
    private RequestQueue requestQueue;

    private RequestQueueTon(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public static RequestQueueTon getInstance(Context context) {
        if (queue == null) {
            queue = new RequestQueueTon(context);
        }
        return queue;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

}
