package com.example.trackyoursmoking;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;

public class ReportingActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.reporting);
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
      
        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

            public void onTabChanged(String str) {
            	vibrate();
            }
        });
        
        Calendar calendar = Calendar.getInstance();
        
        Bundle dataTodayTab = new Bundle();
        dataTodayTab.putLong("date", new Date().getTime());

        Bundle lastWeekTab = new Bundle();
        Bundle lastMonth = new Bundle();
        
        lastWeekTab.putLong("dateTo", calendar.getTimeInMillis());
        lastMonth.putLong("dateTo", calendar.getTimeInMillis());
        
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        lastWeekTab.putLong("dateFrom", calendar.getTimeInMillis());

        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        lastMonth.putLong("dateFrom", calendar.getTimeInMillis());
        
        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator("today",
                        getResources().getDrawable(R.drawable.reporting_icon)),
                DailyReport.class,dataTodayTab);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator("last week",
                		  getResources().getDrawable(R.drawable.reporting_icon)),
                          ReportForDatesPeriod.class, lastWeekTab);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator("last month",
                		  getResources().getDrawable(R.drawable.reporting_icon)),
                		  ReportForDatesPeriod.class, lastMonth);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab4").setIndicator("for specified day",
                		  getResources().getDrawable(R.drawable.reporting_icon)),
                          DailyReport.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab5").setIndicator("for specific period",
                		  getResources().getDrawable(R.drawable.reporting_icon)),
                		  ReportForDatesPeriod.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab6").setIndicator("for specific period extended",
                		  getResources().getDrawable(R.drawable.reporting_icon)),
                		  ReportForDateAndTimePeriod.class, null);
        
        TabWidget tw = (TabWidget) findViewById(android.R.id.tabs);
        LinearLayout ll = (LinearLayout) tw.getParent();
        HorizontalScrollView hs = new HorizontalScrollView(this);
        hs.setLayoutParams(new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT));
        ll.addView(hs, 0);
        ll.removeView(tw);
        hs.addView(tw);
        hs.setHorizontalScrollBarEnabled(false);
    }
    
    
    private void vibrate(){
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
		
		boolean allowedVibration = sharedPrefs.getBoolean("allow_vibration", false);
		
		if(allowedVibration){
    			Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        		// Vibrate for 500 milliseconds
    			vibrator.vibrate(100);
			}			
	}
  
}