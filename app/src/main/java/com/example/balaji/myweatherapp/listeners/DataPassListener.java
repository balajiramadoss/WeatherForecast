package com.example.balaji.myweatherapp.listeners;

import com.example.balaji.myweatherapp.models.Forecastday;

public interface DataPassListener {
    void passDataBetweenFragments(Forecastday obj);
}
