package com.example.trackyoursmoking;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.RelativeLayout;

public class ReportForDatesPeriod extends Fragment {

	private  IRepository repository;
	
	
	
	boolean isloadedReport;
	
	long selectedDateFrom;
	long selectedDateTo;
	
	private DatePicker datePicker;
	
	private Switch calendarSwitch;
	
	private DatePickerDialog dpdFromDate;
	private DatePickerDialog dpdToDate;
	
	private Button changeDateFrom;
	private Button changeDateTo;
	
	private Button showReport;
	
	private TextView dateFromTextView;
	private TextView dateToTextView;
	
	private TextView reportTextView; 
	
	public ReportForDatesPeriod(){
		this.repository = new TestRepository();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.report_for_dates_period, container, false);
    
        reportTextView = (TextView) v.findViewById(R.id.reportTextView);
        
        Bundle bundle = this.getArguments();
        if(bundle != null){
			long dateFrom = bundle.getLong("dateFrom", 0);
			long dateTo = bundle.getLong("dateTo", 0);
	        if(dateFrom != 0 && dateTo != 0){
	        	
	        	loadReport(dateFrom, dateTo);
	        	return v;
	        }
		}
        
        initializeUserInputElements(v);
        
        if(savedInstanceState != null){
        	loadFromSavedInstance(savedInstanceState);
        }
        
        return v;

	}
	
	private void initializeUserInputElements(View v){
		
		RelativeLayout pickingIntervalLayout = (RelativeLayout)v.findViewById(R.id.userInputRelativeLayout);
		pickingIntervalLayout.setVisibility(View.VISIBLE);

		datePicker = (DatePicker) v.findViewById(R.id.calendarDatePicker);
    	
		calendarSwitch = (Switch) v.findViewById(R.id.calendarDateAndTimeReportSwitch);
		
		calendarSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked){
					datePicker.setVisibility(View.VISIBLE);
				}
				else{
					datePicker.setVisibility(View.GONE);
				}
				
			}       
			
		});
		
		dateFromTextView = (TextView) v.findViewById(R.id.dateFromTextView);
		dateToTextView = (TextView) v.findViewById(R.id.dateToTextView);
		
		changeDateFrom = (Button) v.findViewById(R.id.fromDateButton);
		changeDateFrom.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {

				setPickingDateFrom();
				reportTextView.setText("");
			}

        });
		changeDateTo = (Button) v.findViewById(R.id.toDateButton);
		changeDateTo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {

				setPickingDateTo();
				reportTextView.setText("");
			}

        });
		
		showReport = (Button) v.findViewById(R.id.showReportDateButton);
		showReport.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				if(selectedDateFrom == 0 || selectedDateTo == 0){
					return;
				}

				loadReport(selectedDateFrom, selectedDateTo);
			}

        });
		
		
    }
	
	private void loadFromSavedInstance(Bundle savedInstanceState){
		Calendar clendar = Calendar.getInstance();
		
		 selectedDateFrom = savedInstanceState.getLong("selectedDateFrom", 0);
	
		 if(selectedDateFrom != 0){
			
			 clendar.setTimeInMillis(selectedDateFrom);
			 
			 this.updateDateFrom(clendar.get(Calendar.YEAR), clendar.get(Calendar.MONTH), clendar.get(Calendar.DATE));
		  }
		 
		 selectedDateTo = savedInstanceState.getLong("selectedDateTo", 0);
		 
		  if(selectedDateTo != 0){
			  
			  clendar.setTimeInMillis(selectedDateTo);
				 
			  this.updateDateTo(clendar.get(Calendar.YEAR), clendar.get(Calendar.MONTH), clendar.get(Calendar.DATE));
		  }
		
		  isloadedReport = savedInstanceState.getBoolean("loadedReport", false);
		  
		  if(isloadedReport){
			  
			  this.loadReport(selectedDateFrom, selectedDateTo);
		  }
		  
		
		  
		  long dialogOpenDateDateFrom = savedInstanceState.getLong("dialogOpenDateDateFrom", 0);
			
			 if(dialogOpenDateDateFrom != 0){
				  // set date
				  this.setPickingDateFrom();
			  }
			
			 long dialogOpenDateDateTo = savedInstanceState.getLong("dialogOpenDateDateTo", 0);
			 
			 if(dialogOpenDateDateTo != 0){
				 // set date
				  this.setPickingDateTo();
			  }
				
		
		
    }
	
    private void loadReport(long from, long to){
    	
    	Calendar calendar = Calendar.getInstance();
    	
    	calendar.setTimeInMillis(from);
    	int yearFrom = calendar.get(Calendar.YEAR);
    	int monthFrom = calendar.get(Calendar.MONTH);
    	int dayFrom = calendar.get(Calendar.DAY_OF_MONTH);
    	
    	calendar.setTimeInMillis(to);
    	int yearTo = calendar.get(Calendar.YEAR);
    	int monthTo = calendar.get(Calendar.MONTH);
    	int dayTo = calendar.get(Calendar.DAY_OF_MONTH);
    	PeriodReport report = 
    			this.repository.getCigarettesPerDatesPeriod(
    					yearFrom, yearTo, monthFrom, monthTo, dayFrom, dayTo);
    	
    	String status = "";
		int minimum = repository.getInitialData().getMinCigarettensPerDay();
		int maximum = repository.getInitialData().getMaxCigarettensPerDay();
		
		String reportText = "";
		
		if(report == null || report.getAverrageCigarettesPerDay() == 0){
			status = SmokingStates.NO_SMOKE_STATE;
			
		}
		else{
			if(report.getAverrageCigarettesPerDay() < minimum){
				status = SmokingStates.UNDER_MINIMUM_SMOKE_STATE;
			}
			else if(report.getAverrageCigarettesPerDay() == maximum){
				status = SmokingStates.REACHED_MAXIMUM_SMOKE_STATE;
			}
			else if(report.getAverrageCigarettesPerDay() == minimum){
				status = SmokingStates.REACHED_MINIMUM_SMOKE_STATE;
			}
			else if(report.getAverrageCigarettesPerDay() > maximum){
				status = SmokingStates.ABOVE_LIMIT_SMOKE_STATE;
			}
			else if(report.getAverrageCigarettesPerDay() < maximum){
				status = SmokingStates.AVERAGE_SMOKE_STATE;
			}
			
			reportText = report.toString();
		}

		reportTextView.setText(String.format("%s-%s-%s to %s-%s-%s %s %s %s %s", 
				dayFrom,  new DateFormatSymbols().getMonths()[monthFrom], yearFrom,
				dayTo,  new DateFormatSymbols().getMonths()[monthTo], yearTo,
				System.getProperty("line.separator"),
				status, System.getProperty("line.separator"), reportText));
		
    }

    
    private void setPickingDateFrom(){
    	
    	Calendar clendar = Calendar.getInstance();
    	
    	if(selectedDateFrom != 0){
     		 
    		clendar.setTimeInMillis(selectedDateFrom);
       		
      	 }
    	
    	dpdFromDate = new DatePickerDialog(getActivity(), 
				new DatePickerDialog.OnDateSetListener() {
			   	    @Override
			   	    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			   	    	
				   	    Calendar calendar =	Calendar.getInstance();
				   	    calendar.set(selectedYear, selectedMonth, selectedDay, 0, 0);
				   	    
				   	    updateDateFrom(selectedYear, selectedMonth, selectedDay);
			   	    	dpdFromDate = null;
		   	    	
			   	    }
		}, clendar.get(Calendar.YEAR), clendar.get(Calendar.MONTH), clendar.get(Calendar.DATE));

		
		
		if(selectedDateTo != 0){
    		dpdFromDate.getDatePicker().setMaxDate(selectedDateTo);
    	}
		else{
			dpdFromDate.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
		}
    	
        dpdFromDate.show();
    	
    }
    
    private void setPickingDateTo(){
    	
    	Calendar clendar = Calendar.getInstance();
    	
    	if(selectedDateTo != 0){
     		 
    		clendar.setTimeInMillis(selectedDateTo);
       		
      	 }
    	
    	dpdToDate = new DatePickerDialog(getActivity(), 
				new DatePickerDialog.OnDateSetListener() {
			   	    @Override
			   	    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			   	    	
				   	    Calendar calendar =	Calendar.getInstance();
				   	    calendar.set(selectedYear, selectedMonth, selectedDay, 0, 0);
				   	    
				   	    updateDateTo(selectedYear, selectedMonth, selectedDay);
				   	 dpdToDate = null;
		   	    	
			   	    }
		}, clendar.get(Calendar.YEAR), clendar.get(Calendar.MONTH), clendar.get(Calendar.DATE));

    	dpdToDate.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
		
    	
    	if(selectedDateFrom != 0){
    		dpdToDate.getDatePicker().setMinDate(selectedDateFrom);
    		
    	}
    	dpdToDate.show();
	
   }
    
		private void updateDateFrom(int year, int month, int day){
			// validation
			Calendar cal = Calendar.getInstance();
			cal.set(year, month, day);
			selectedDateFrom = cal.getTimeInMillis();
			dateFromTextView.setText(
					 String.format("%s-%s-%s", year, new DateFormatSymbols().getMonths()[month], day));
		}
		
		private void updateDateTo(int year, int month, int day){
			// validation
			Calendar cal = Calendar.getInstance();
			cal.set(year, month, day);
			selectedDateTo = cal.getTimeInMillis();
			dateToTextView.setText(
					 String.format("%s-%s-%s", year, new DateFormatSymbols().getMonths()[month], day));
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
			  
			  savedInstanceState.putLong("dialogOpenDateDateFrom", cal.getTimeInMillis());
		  }
		  
		  if(dpdToDate != null){
			  Calendar cal = Calendar.getInstance();
			  int selectedYear = dpdToDate.getDatePicker().getYear();
			  int selectedMonth = dpdToDate.getDatePicker().getMonth();
			  int selectedDay = dpdToDate.getDatePicker().getDayOfMonth();
			  cal.set(selectedYear, selectedMonth, selectedDay);
			  
			  savedInstanceState.putLong("dialogOpenDateDateTo", cal.getTimeInMillis());
		  }
		  
		  if(selectedDateFrom != 0){
			  
			  savedInstanceState.putLong("selectedDateFrom", selectedDateFrom);
		  }
		  
		  if(selectedDateTo != 0){
			  
			  savedInstanceState.putLong("selectedDateTo", selectedDateTo);
		  }
		  
		  if(isloadedReport){
			  
			  savedInstanceState.putBoolean("loadedReport", isloadedReport);
		  }
		}
	}
