package com.example.balaji.myweatherapp.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private int position;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public void bindData(int position) {

    }

    void setDetails(int position) {
        this.position = position;
        bindData(position);
    }

}
