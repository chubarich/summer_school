package com.luseen.yandexsummerschool.ui.fragment.favourite;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luseen.yandexsummerschool.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment {

    public static FavouriteFragment newInstance() {
        
        Bundle args = new Bundle();
        
        FavouriteFragment fragment = new FavouriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

}