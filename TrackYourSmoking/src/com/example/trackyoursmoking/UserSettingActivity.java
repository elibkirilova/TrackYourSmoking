package com.example.trackyoursmoking;

import android.os.Bundle;

import android.preference.PreferenceActivity;

public class UserSettingActivity extends PreferenceActivity   {
 
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
      //  setContentView(R.layout.settings);
       // addPreferencesFromResource(R.xml.settings);
        
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
        
    }

}
