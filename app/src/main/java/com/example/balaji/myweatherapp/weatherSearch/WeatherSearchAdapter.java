package com.example.balaji.myweatherapp.weatherSearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.balaji.myweatherapp.R;
import com.example.balaji.myweatherapp.helper.BaseRecyclerAdapter;
import com.example.balaji.myweatherapp.helper.BaseViewHolder;
import com.example.balaji.myweatherapp.listeners.OnItemClickListener;
import com.example.balaji.myweatherapp.models.SearchItem;

import java.util.ArrayList;
import java.util.List;

public class WeatherSearchAdapter extends BaseRecyclerAdapter {

    private final OnItemClickListener listener;
    private Context context;
    private List<SearchItem> searchItems;
    private List<Object> items;

    WeatherSearchAdapter(Context context, List<SearchItem> searchItems, OnItemClickListener listener) {
        this.context = context;
        this.searchItems = searchItems;
        this.listener = listener;
        items = new ArrayList<>();
        prepareItems();
    }

    private void prepareItems() {
        items.clear();
        if (searchItems != null) {
            items.addAll(searchItems);
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder;
        View view = LayoutInflater.from(context).inflate(R.layout.suggestion_layout, null);
        viewHolder = new WeatherSearchViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setDetails(List<SearchItem> searchItems) {
        this.searchItems = searchItems;
        prepareItems();
        notifyDataSetChanged();
    }

    class WeatherSearchViewHolder extends BaseViewHolder implements View.OnClickListener {
        private TextView suggestionText;
        private SearchItem item;

        public WeatherSearchViewHolder(View itemView) {
            super(itemView);
            suggestionText = itemView.findViewById(R.id.suggestionText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void bindData(int position) {
            super.bindData(position);
            item = (SearchItem) items.get(position);
            if (item != null && item.getName() != null)
                suggestionText.setText(item.getName());
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(item);
        }
    }
}
