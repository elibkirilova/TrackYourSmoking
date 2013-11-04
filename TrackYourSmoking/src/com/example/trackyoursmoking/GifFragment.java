package com.example.trackyoursmoking;


import java.io.IOException;
import java.io.InputStream;

import com.example.trackyoursmoking.gifview.GifMovieView;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GifFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	
    	InputStream stream = null;
        try {
            stream = getActivity().getAssets().open("smoke_under_the_minimum.gif");
           
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
      GifMovieView view = new GifMovieView(getActivity(), stream);
    	
    	return view;
    }
}
