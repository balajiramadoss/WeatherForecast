package com.example.balaji.myweatherapp.helper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public abstract class BaseRecyclerAdapter<T extends BaseViewHolder> extends RecyclerView.Adapter<T> {

    @NonNull
    public abstract T onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    public void onBindViewHolder(@NonNull T holder, int position) {
        holder.setDetails(position);
    }

    public abstract int getItemCount();
}
