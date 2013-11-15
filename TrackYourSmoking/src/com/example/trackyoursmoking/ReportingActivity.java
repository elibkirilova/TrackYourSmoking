package com.example.trackyoursmoking;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class ReportingActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.reporting);
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

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
                mTabHost.newTabSpec("tab5").setIndicator("for specified period",
                		  getResources().getDrawable(R.drawable.reporting_icon)),
                		  ReportForDatesPeriod.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab6").setIndicator("for specified days and time period",
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
}