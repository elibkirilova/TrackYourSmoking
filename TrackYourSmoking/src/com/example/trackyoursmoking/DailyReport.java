package com.example.trackyoursmoking;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.List;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;

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



public class DailyReport extends Fragment  {
	
	public final static String STOCK_SYMBOL = "com.example.myfirstapp.STOCK";
	
	private  IRepository repository;
	
	
	private DatePickerDialog dpdFromDate;
	
	private long loadedDate;
	
	
	
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

		
		
		Bundle bundle = this.getArguments();
		if(bundle != null){
			long date = bundle.getLong("date", 0);
	        if(date != 0){
	        	
	        	loadActivities(new Date(date));
	        	return view;
	        }
	        
		}
		
			
			
			if(savedInstanceState != null){
				long loadedDateInDatePicker = savedInstanceState.getLong("dialogOpenDate", 0);
				
				// in case the dialog was open the configuration was changed and then it was close
				if(loadedDateInDatePicker != 0){
					loadDialog(new Date(loadedDateInDatePicker));
				}
				
				long loadedDateInTheView = savedInstanceState.getLong("loadedDate", 0);
				
				if(loadedDateInTheView != 0){
					loadedDate = loadedDateInTheView;
					loadActivities(new Date(loadedDate));
				}
				
			}
			Button pickDateBtn = (Button) view.findViewById(R.id.pickDayButton);
			pickDateBtn.setVisibility(View.VISIBLE);
			pickDateBtn.setOnClickListener(new OnClickListener(){
				
				
				@Override
				public void onClick(View arg0) {
					loadDialog(new Date());
					
				}

	        });
			
			
		
		
		return view;
	}
	
	
	private void loadActivities(Date date){
		
		this.loadedDate = date.getTime();
		stockTableScrollView.removeAllViews();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		int currentYear = cal.get(Calendar.YEAR);

		int currentMonth = cal.get(Calendar.MONTH) ;
		
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
	
	private void loadDialog(Date date){
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		
		dpdFromDate = new DatePickerDialog(getActivity(), 
				new DatePickerDialog.OnDateSetListener() {
			   	    @Override
			   	    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			   	    	
				   	    Calendar calendar =	Calendar.getInstance();
				   	    calendar.set(selectedYear, selectedMonth, selectedDay, 0, 0);
				   	    
			   	    	loadActivities(calendar.getTime());
			   	    	dpdFromDate = null;
		   	    	
			   	    }
		}, mYear, mMonth, mDay);

		dpdFromDate.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        dpdFromDate.show();
        
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
			
			dataContainer.setText(String.format("%s-%s-%s ciggarette(s) %s.%s %s.%s Spent money %.2g%n", 
					selectedDay, new DateFormatSymbols().getMonths()[selectedMonth], selectedYear,
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
	
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    this.repository = new DefaultRepository(activity.getApplication());
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  if(dpdFromDate != null){
		  Calendar cal = Calendar.getInstance();
		  int selectedYear = dpdFromDate.getDatePicker().getYear();
		  int selectedMonth = dpdFromDate.getDatePicker().getMonth();
		  int selectedDay = dpdFromDate.getDatePicker().getDayOfMonth();
		  cal.set(selectedYear, selectedMonth, selectedDay);
		  
		  savedInstanceState.putLong("dialogOpenDate", cal.getTimeInMillis());
	  }
	  
	  if(loadedDate != 0){
		  
		  savedInstanceState.putLong("loadedDate", loadedDate);
	  }
	  
	}

}
