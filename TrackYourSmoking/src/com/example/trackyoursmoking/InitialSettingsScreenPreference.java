package com.example.trackyoursmoking;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.AttributeSet;

public class InitialSettingsScreenPreference extends Preference implements OnPreferenceClickListener {
	 public InitialSettingsScreenPreference(Context context) {
	        super(context);
	        setOnPreferenceClickListener(this);
	    }

	    public InitialSettingsScreenPreference(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        setOnPreferenceClickListener(this);
	    }

	    public InitialSettingsScreenPreference(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	        setOnPreferenceClickListener(this);
	    }

	    public boolean onPreferenceClick(Preference preference) {
	        
	    	
	    	
	        return true;
	    }

}