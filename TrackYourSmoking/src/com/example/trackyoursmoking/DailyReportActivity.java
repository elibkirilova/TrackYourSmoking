package com.example.trackyoursmoking;

import java.util.Calendar;

import java.util.Date;
import java.util.List;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;



public class DailyReportActivity extends Fragment  {
	
	public final static String STOCK_SYMBOL = "com.example.myfirstapp.STOCK";
	
	private  IRepository repository;
	
	public DailyReportActivity(){
		this.repository = new TestRepository();
	}
	
	public DailyReportActivity(IRepository repository){
		this.repository = repository;
	}
	
	private TableLayout stockTableScrollView;
	

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.daily_report, container, false);
    	
        //String authToken = gb = getActivity().getIntent().getStringExtra(""); 
		stockTableScrollView = (TableLayout) v. findViewById(R.id.stockTableScrollView);

		loadActivities();
		
		return v;
	}
	
	
	private void loadActivities(){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		int currentYear = cal.get(Calendar.YEAR);

		int currentMonth = cal.get(Calendar.MONTH);
		
		int currentDay = cal.get(Calendar.DATE);
		
		
		List<SmokingActivity> activities = this.repository.takeCigarettesForGivenDay(currentYear, currentMonth, currentDay);
		
		int activitiesCount = activities.size();
		
	
		for(int i = 0; i < activitiesCount; ++i){
			
			insertStockInScrollView(activities.get(i), i);
		}
		
       
    }
	

	private void insertStockInScrollView(SmokingActivity stock, int arrayIndex){
		
		
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		
		View newStockRow = inflater.inflate(R.layout.cigarette_report_row, null);

		TextView newStockTextView = (TextView) newStockRow.findViewById(R.id.stockSymbolTextView);

		newStockTextView.setText(stock.toString());
		
		TextView activityIdTextView = (TextView) newStockRow.findViewById(R.id.activityIdTextView);
		
		activityIdTextView.setText(Integer.toString(stock.getId()));

		
		Button stockQuoteButton = (Button) newStockRow.findViewById(R.id.stockQuoteButton);
		stockQuoteButton.setOnClickListener(getStockActivityListener);
		
		stockTableScrollView.addView(newStockRow, arrayIndex);
	}
	
	
	public OnClickListener getStockActivityListener = new OnClickListener(){

		public void onClick(View v) {
			
			// Get the text saved in the TextView next to the clicked button
			// with the id stockSymbolTextView

			final TableRow tableRow = (TableRow) v.getParent();
			
            TextView stockTextView = (TextView) tableRow.findViewById(R.id.stockSymbolTextView);
            String stockSymbol = stockTextView.getText().toString();
            
             new AlertDialog.Builder(v.getContext())
		 	.setTitle("Please fix the following error(s)")
     	    .setMessage(stockSymbol)
     	    .setNegativeButton("Cancel", null)
     	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
     	        public void onClick(DialogInterface dialog, int which) { 
     	        
	 	        	TextView activityIdTextView = (TextView) tableRow.findViewById(R.id.activityIdTextView);
	 	            int activityId = Integer.parseInt(activityIdTextView.getText().toString()) ;
     	        	
	 	            repository.removeActivity(activityId);
	 	            
	 	           stockTableScrollView.removeView(tableRow);
     	        }
     	     }
     	   
    	     )
     	     .show();
		}
		
	};
	
	
}
