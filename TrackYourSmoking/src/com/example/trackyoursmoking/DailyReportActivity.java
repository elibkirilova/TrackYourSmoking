package com.example.trackyoursmoking;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;



public class DailyReportActivity extends Fragment  {
	
	public final static String STOCK_SYMBOL = "com.example.myfirstapp.STOCK";
	
	private  IRepository repository;
	
	public DailyReportActivity(){
		this.repository = new TestRepository();
	}
	
//	public DailyReportActivity(IRepository repository){
//		this.repository = repository;
//	}
	
	private TableLayout stockTableScrollView;
	private View view;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.daily_report, container, false);
    	
       
		stockTableScrollView = (TableLayout) view. findViewById(R.id.smokingActivityTableScrollView);

		if(savedInstanceState != null){
			
		}
		
		Bundle bundle = this.getArguments();
		if(bundle != null){
			long date = bundle.getLong("date", 0);
	        if(date != 0){
	        	
	        	loadActivities(new Date(date));
	        }
	        else{
	        	
	        	throw new IllegalArgumentException("Bundle does not contain 'date'.");
	        }
		}
		else{
			loadDatePickingProcess();
		}
		
		return view;
	}
	
	
	private void loadActivities(Date date){
		
		stockTableScrollView.removeAllViews();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		int currentYear = cal.get(Calendar.YEAR);

		int currentMonth = cal.get(Calendar.MONTH ) ;
		
		int currentDay = cal.get(Calendar.DATE);
		
		List<SmokingActivity> activities = this.repository.takeCigarettesForGivenDay(currentYear, currentMonth, currentDay);
		
		int activitiesCount = activities.size();
		
		double spendMoney = 0;

		for(int i = 0; i < activitiesCount; ++i){
			
			insertStockInScrollView(activities.get(i), i);
			spendMoney += activities.get(i).getCigarettePrice();
		}
		
		loadSmokingData(currentYear, currentMonth, currentDay, activitiesCount, spendMoney);
    }
	
	
	private void loadDatePickingProcess(){
			
			Button pickDateBtn = (Button) view.findViewById(R.id.pickDayButton);
			pickDateBtn.setVisibility(View.VISIBLE);
			pickDateBtn.setOnClickListener(new OnClickListener(){
				Calendar c = Calendar.getInstance();
				int mYear = c.get(Calendar.YEAR);
				int mMonth = c.get(Calendar.MONTH);
				int mDay = c.get(Calendar.DAY_OF_MONTH);
				@Override
				public void onClick(View arg0) {
					
					DatePickerDialog dpdFromDate = new DatePickerDialog(getActivity(), 
							new DatePickerDialog.OnDateSetListener() {
						   	    @Override
						   	    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
						   	    	
						   	    Calendar calendar =	Calendar.getInstance();
						   	    calendar.set(selectedYear, selectedMonth, selectedDay, 0, 0);
		
					   	    	loadActivities(calendar.getTime());
					   	    	
						   	    }}, mYear, mMonth, mDay);
					
					
				        
					dpdFromDate.getDatePicker().setMaxDate(c.getTimeInMillis());
			        dpdFromDate.show();
			
				      dpdFromDate.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", 
				    		  new DialogInterface.OnClickListener() {
						          public void onClick(DialogInterface dialog, int which) {
						             if (which == DialogInterface.BUTTON_NEGATIVE) {
						                  
						             }
						          	} 
				          });
				}

	        });
	       
	    }

	private void insertStockInScrollView(SmokingActivity activity, int arrayIndex){
		
		
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		
		View currentActivityRow = inflater.inflate(R.layout.cigarette_report_row, null);

		TextView currentActivityTimeTextView = (TextView) currentActivityRow.findViewById(R.id.activityTimeTextView);

		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());

		currentActivityTimeTextView.setText(dateFormat.format(activity.getDateAndTime()));
		
		TextView currentActivityPriceTextView = (TextView) currentActivityRow.findViewById(R.id.activityPriceTextView);

		currentActivityPriceTextView.setText(Double.toString(activity.getCigarettePrice()));
		
		TextView activityIdTextView = (TextView) currentActivityRow.findViewById(R.id.activityIdTextView);
		
		activityIdTextView.setText(Integer.toString(activity.getId()));

		
		Button deleteActivityButton = (Button) currentActivityRow.findViewById(R.id.deleteActivitybutton);
		deleteActivityButton.setOnClickListener(deleteActivityListener);
		
		stockTableScrollView.addView(currentActivityRow, arrayIndex);
	}
	
	
	private void loadSmokingData(int selectedYear, int selectedMonth, int selectedDay, int cigarettesCount, double spendMoney){
			
			TextView dataContainer = (TextView)view.findViewById(R.id.dateAndCigarettesCountTextView);		
			
		
			String status = "";
			int minimum = repository.getInitialData().getMinCigarettensPerDay();
			int maximum = repository.getInitialData().getMaxCigarettensPerDay();
			
			if(cigarettesCount == 0){
				status = SmokingStates.NO_SMOKE_STATE;
			}
			else if(cigarettesCount < minimum){
				status = SmokingStates.UNDER_MINIMUM_SMOKE_STATE;
			}
			else if(cigarettesCount == maximum){
				status = SmokingStates.REACHED_MAXIMUM_SMOKE_STATE;
			}
			else if(cigarettesCount == minimum){
				status = SmokingStates.REACHED_MINIMUM_SMOKE_STATE;
			}
			else if(cigarettesCount > maximum){
				status = SmokingStates.ABOVE_LIMIT_SMOKE_STATE;
			}
			else if(cigarettesCount < maximum){
				status = SmokingStates.AVERAGE_SMOKE_STATE;
			}
			
			dataContainer.setText(String.format("%s/%s/%s ciggarette(s) %s.%s %s.%s Spent money %.2g%n", 
					selectedDay, selectedMonth + 1, selectedYear,
					cigarettesCount, System.getProperty("line.separator"),
					status, System.getProperty("line.separator"), spendMoney));
			
		}
	
	public OnClickListener deleteActivityListener = new OnClickListener(){

		public void onClick(View v) {
			
			// Get the text saved in the TextView next to the clicked button
			// with the id stockSymbolTextView

			final TableRow tableRow = (TableRow) v.getParent();
			
            TextView stockTextView = (TextView) tableRow.findViewById(R.id.activityTimeTextView);
            String activityTime = stockTextView.getText().toString();
            
             new AlertDialog.Builder(v.getContext())
		 	.setTitle("Delete confirmation")
		 	.setMessage(String.format("Are you sure you want to delete the activity occurred at '%s' ?", activityTime))
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
