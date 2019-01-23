package com.example.balaji.myweatherapp.weatherDetails;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.balaji.myweatherapp.R;
import com.example.balaji.myweatherapp.models.Forecastday;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherDetailsFragment extends Fragment {

    private Forecastday forecastday;
    private TextView maxTempTv;
    private TextView minTempTv;
    private TextView conditionTv;
    private TextView sunriseTv;
    private TextView sunsetTv;
    private TextView moonriseTv;
    private TextView moonsetTv;
    private TextView uvTv;
    private ImageView imageView;
    private Context context;

    public WeatherDetailsFragment() {
        // Required empty public constructor
    }

    public static WeatherDetailsFragment newInstance(Forecastday forecastday) {
        WeatherDetailsFragment fragment = new WeatherDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.forecastday = forecastday;
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_details, container, false);
        maxTempTv = view.findViewById(R.id.detailMaxTemp);
        minTempTv = view.findViewById(R.id.detailMinTemp);
        conditionTv = view.findViewById(R.id.detailsWeatherCondition);
        sunriseTv = view.findViewById(R.id.sunrise);
        sunsetTv = view.findViewById(R.id.sunset);
        moonriseTv = view.findViewById(R.id.moonrise);
        moonsetTv = view.findViewById(R.id.moonset);
        uvTv = view.findViewById(R.id.uvIndex);
        imageView = view.findViewById(R.id.theWeatherImage);
        if (forecastday != null)
            refreshUI(forecastday);
        return view;
    }

    public void setDetails(Forecastday forecastday) {
        this.forecastday = forecastday;
        refreshUI(forecastday);
    }

    private void refreshUI(Forecastday forecastday) {
        if (forecastday != null) {
            //I believe the provider for not checking null :)
            maxTempTv.setText(String.valueOf("Max Temp  " + forecastday.getDay().getMaxtempC() + " C"));
            minTempTv.setText(String.valueOf("Min Temp  " + forecastday.getDay().getMintempC() + " C"));
            conditionTv.setText(String.valueOf(forecastday.getDay().getCondition().getText()));
            sunriseTv.setText("Sunrise at  " + forecastday.getAstro().getSunrise());
            sunsetTv.setText("Sunset at  " + forecastday.getAstro().getSunset());
            moonriseTv.setText("Moonrise at  " + forecastday.getAstro().getMoonrise());
            moonsetTv.setText("Moonset at  " + forecastday.getAstro().getMoonset());
            uvTv.setText(String.valueOf("UV INDEX  " + forecastday.getDay().getUv()));
            Glide.with(context).load(forecastday.getDay().getCondition().getIcon()).into(imageView);
        }
    }
}
