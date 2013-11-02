package com.example.trackyoursmoking;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PricingMainInfoFragment extends Fragment{

	private boolean isOnPack;
	
	public PricingMainInfoFragment(boolean isOnPack)
	{
		this.isOnPack = isOnPack;
	}
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
			 Bundle savedInstanceState) {
		 View simpleFragmentView = null;
		 if(this.isOnPack){
			 simpleFragmentView = inflater.inflate(R.layout.pricing_per_pack, container, false);
		 }
		 else{
			 simpleFragmentView = inflater.inflate(R.layout.pricing_per_cigarette, container, false);
		 }
		 return simpleFragmentView;
	 }
}
