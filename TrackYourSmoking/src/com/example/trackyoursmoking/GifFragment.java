package com.example.trackyoursmoking;


import java.io.IOException;
import java.io.InputStream;

import com.example.trackyoursmoking.gifview.GifMovieView;
import com.example.trackyoursmoking.gifview.GifView;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GifFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	
    	  GifView gif_view = new GifView(inflater.getContext());
          gif_view.setGifImageResourceID(R.drawable.animsmoke);
    	
    	return gif_view;
    }
}
