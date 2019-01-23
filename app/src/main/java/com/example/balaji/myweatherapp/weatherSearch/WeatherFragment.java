package com.example.balaji.myweatherapp.weatherSearch;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.balaji.myweatherapp.MainActivity;
import com.example.balaji.myweatherapp.R;
import com.example.balaji.myweatherapp.listeners.ApiListener;
import com.example.balaji.myweatherapp.listeners.DataPassListener;
import com.example.balaji.myweatherapp.listeners.OnItemClickListener;
import com.example.balaji.myweatherapp.models.Forecastday;
import com.example.balaji.myweatherapp.models.SearchItem;
import com.example.balaji.myweatherapp.models.WeatherInfo;
import com.example.balaji.myweatherapp.service.VolleyService;
import com.example.balaji.myweatherapp.utils.Utils;

import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment implements View.OnClickListener, OnItemClickListener {

    private ImageView searchImageView;
    private EditText searchEditText;
    private RecyclerView recyclerView;
    private RecyclerView suggestionRecyclerView;
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager layoutManager2;
    private WeatherInfo weatherDetails;
    private WeatherAdapter adapter;
    private WeatherSearchAdapter searchAdapter;
    private MainActivity activity;
    private ActionBar actionBar;
    private List<SearchItem> searchItems;
    private DataPassListener dataPassListener;
    public WeatherFragment() {
        // Required empty public constructor
    }

    public static WeatherFragment newInstance(MainActivity activity) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.activity = activity;
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            dataPassListener = (DataPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DataPassListener");
        }
    }

    private void setDetails(WeatherInfo weatherDetails) {
        this.weatherDetails = weatherDetails;
        adapter.setDetails(weatherDetails);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        setActionBar();
        recyclerView = view.findViewById(R.id.recyclerView);
        suggestionRecyclerView = view.findViewById(R.id.searchSuggestionRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager2 = new LinearLayoutManager(getContext());
        adapter = new WeatherAdapter(getContext(), weatherDetails, this);
        searchAdapter = new WeatherSearchAdapter(getContext(), searchItems, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        suggestionRecyclerView.setLayoutManager(layoutManager2);
        suggestionRecyclerView.setAdapter(searchAdapter);

        return view;
    }

    public void setActionBar() {
        actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF4081")));
            View headerView = LayoutInflater.from(getContext()).inflate(R.layout.header_layout_top, null);
            actionBar.setCustomView(headerView);
            searchImageView = headerView.findViewById(R.id.searchImageView);
            searchEditText = headerView.findViewById(R.id.searchEditText);
            searchImageView.setOnClickListener(this);
            searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                        if (Utils.haveNetworkConnection(getContext())) {
                            hitApi();
                            recyclerView.setVisibility(View.VISIBLE);
                            suggestionRecyclerView.setVisibility(View.GONE);
                        } else
                            activity.showInfoDialog();
                    }
                    return false;
                }
            });
            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    suggestionFilter(s.toString());
                    suggestionRecyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            });
        }
    }

    private void suggestionFilter(final String query) {
        if (!Utils.isNullorWhiteSpace(query))
            if (Utils.haveNetworkConnection(activity))
                VolleyService.getInstance(getContext(), activity).getWeatherSuggestions(query, new ApiListener<List<SearchItem>>() {
                    @Override
                    public void onSuccess(List<SearchItem> searchItems) {
                        if (searchItems != null) {
                            searchAdapter.setDetails(searchItems);
                        }
                    }
                });
            else
                activity.showInfoDialog();

    }

    private void hitApi() {
        VolleyService.getInstance(getContext(), activity).getWeatherDetails(searchEditText.getText().toString(), new ApiListener<WeatherInfo>() {
            @Override
            public void onSuccess(WeatherInfo weatherDetails) {
                if (weatherDetails != null)
                    setDetails(weatherDetails);
            }
        });
    }


    private void hitApi(String query) {
        VolleyService.getInstance(getContext(), activity).getWeatherDetails(query, new ApiListener<WeatherInfo>() {
            @Override
            public void onSuccess(WeatherInfo weatherDetails) {
                if (weatherDetails != null)
                    setDetails(weatherDetails);
            }
        });
        suggestionRecyclerView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.searchImageView) {
            if (Utils.haveNetworkConnection(getContext())) {
                recyclerView.setVisibility(View.VISIBLE);
                suggestionRecyclerView.setVisibility(View.GONE);
                hitApi();
            } else activity.showInfoDialog();
        }
    }

    @Override
    public void onItemClick(Object item) {
        if (item != null) {
            if (item instanceof SearchItem) {
                SearchItem searchItem = (SearchItem) item;
                String query = searchItem.getName();
                if (!Utils.isNullorWhiteSpace(query)) {
                    String str[] = query.split(",");
                    if (str.length > 0) {
                        hitApi(str[0]);
                        searchEditText.setInputType(InputType.TYPE_NULL);
                        //hideKeyboard();
                    }
                }
            }
            if (item instanceof Forecastday) {
                Forecastday forecastday = (Forecastday) item;
                dataPassListener.passDataBetweenFragments(forecastday);
            }
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getRootView().getWindowToken(), 0);
    }
}
