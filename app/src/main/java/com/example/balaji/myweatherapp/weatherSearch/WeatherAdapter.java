package com.example.balaji.myweatherapp.weatherSearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.balaji.myweatherapp.R;
import com.example.balaji.myweatherapp.helper.BaseRecyclerAdapter;
import com.example.balaji.myweatherapp.helper.BaseViewHolder;
import com.example.balaji.myweatherapp.models.Forecastday;
import com.example.balaji.myweatherapp.models.WeatherInfo;

import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends BaseRecyclerAdapter {

    private static final int WEATHER_LAYOUT = R.layout.weather_layout;
    private static final int TEMPERATURE_LAYOUT = R.layout.temperature_layout;
    private List<Object> items;
    private Context context;
    private WeatherInfo details;

    WeatherAdapter(Context context, WeatherInfo details) {
        this.context = context;
        this.details = details;
        items = new ArrayList<>();
        prepareItems();
    }

    private void prepareItems() {
        items.clear();
        if (details != null) {
            if (details.getCurrent() != null) {
                //items.add(details.getCurrent());
            }
            if (details.getLocation() != null) {
                //items.add(details.getLocation());
            }
            if (details.getForecast() != null && details.getForecast().getForecastday() != null) {
                items.addAll(details.getForecast().getForecastday());
            }
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        View view = LayoutInflater.from(context).inflate(viewType, null);
        switch (viewType) {
            case WEATHER_LAYOUT:
                viewHolder = new WeatherViewHolder(view);
                break;
            case TEMPERATURE_LAYOUT:
                viewHolder = new TemperatureViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return WEATHER_LAYOUT;
        return TEMPERATURE_LAYOUT;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setDetails(WeatherInfo details) {
        this.details = details;
        prepareItems();
        notifyDataSetChanged();
    }

    class WeatherViewHolder extends BaseViewHolder {
        private ImageView iconBig;
        private TextView conditionTv;
        private TextView maxTemp;
        private TextView minTemp;
        private TextView dateTv;
        private Forecastday item;

        public WeatherViewHolder(View itemView) {
            super(itemView);
            iconBig = itemView.findViewById(R.id.weatherIcon);
            conditionTv = itemView.findViewById(R.id.conditionText);
            maxTemp = itemView.findViewById(R.id.maxTemp);
            minTemp = itemView.findViewById(R.id.minTemp);
            dateTv = itemView.findViewById(R.id.date);
        }

        @Override
        public void bindData(int position) {
            super.bindData(position);
            item = (Forecastday) items.get(position);
            if (item.getDay() != null) {
                if (item.getDay().getCondition() != null) {
                    if (item.getDay().getCondition().getIcon() != null)
                        Glide.with(context).load(item.getDay().getCondition().getIcon()).into(iconBig);
                    conditionTv.setText(item.getDay().getCondition().getText());
                }
                maxTemp.setText(String.valueOf(item.getDay().getMaxtempC()) + " C");
                minTemp.setText(String.valueOf(item.getDay().getMintempC()) + " C");
                dateTv.setText(item.getDate() + "- Today");
            }
        }
    }

    class TemperatureViewHolder extends BaseViewHolder {
        private TextView maxTemp;
        private TextView minTemp;
        private TextView childDate;
        private TextView childCondition;
        private ImageView iconSmall;
        private Forecastday item;

        public TemperatureViewHolder(View itemView) {
            super(itemView);
            maxTemp = itemView.findViewById(R.id.childMaxTemp);
            minTemp = itemView.findViewById(R.id.childMinTemp);
            childDate = itemView.findViewById(R.id.childDate);
            childCondition = itemView.findViewById(R.id.childCondition);
            iconSmall = itemView.findViewById(R.id.childIcon);
        }

        @Override
        public void bindData(int position) {
            super.bindData(position);
            item = (Forecastday) items.get(position);
            if (position == 1) {
                childDate.setText(item.getDate() + "Tomorrow");
            } else childDate.setText(item.getDate());
            if (item.getDay() != null) {
                if (item.getDay().getCondition() != null) {
                    if (item.getDay().getCondition().getIcon() != null)
                        Glide.with(context).load(item.getDay().getCondition().getIcon()).into(iconSmall);
                    childCondition.setText(item.getDay().getCondition().getText());
                }
                maxTemp.setText(String.valueOf(item.getDay().getMaxtempC()) + " C");
                minTemp.setText(String.valueOf(item.getDay().getMintempC()) + " C");
            }
        }
    }


}
