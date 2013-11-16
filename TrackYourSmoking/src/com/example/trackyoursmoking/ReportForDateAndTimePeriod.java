package com.example.trackyoursmoking;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ReportForDateAndTimePeriod extends Fragment {

	private  IRepository repository;

	
	
	private long selectedDateFrom;
	private long selectedDateTo;
	
    private	long selectedTimeFrom;
	private long selectedTimeTo;
	
	boolean[] checkedDays;
	
	private CalendarView datePicker;
	
	private Switch calendarSwitch;
	
	private DatePickerDialog dpdFromDate;
	private DatePickerDialog dpdToDate;
	
	private TimePickerDialog tpdFromTime;
	private TimePickerDialog tpdToTime;
	
	
	private Button changeDateFrom;
	private Button changeDateTo;
	
	private Button changeTimeFrom;
	private Button changeTimeTo;
	
	private Button excludeDaysOfTheWeekButtton;
	private Button showReport;
	
	private TextView dateFromTextView;
	private TextView dateToTextView;
	
	private TextView timeFromTextView;
	private TextView timeToTextView;
	
	private TextView reportTextView; 
	
	private AlertDialog excludeDaysDialog;
	private String[] daysOfTheWeek =   DayOfWeek.getAll();
	
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    this.repository = new TestRepository(activity.getApplication());
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		 View v = inflater.inflate(R.layout.report_for_dates_and_time_period, container, false);
		    
		 initializeUserInputElements(v);
	        
	        if(savedInstanceState != null){
	        	loadFromSavedInstance(savedInstanceState);
	        }
	        else{
	          this.checkedDays = new boolean[] {true, true, true, true, true, true, true};
	        }

	      return v;
	}
	
	private void initializeUserInputElements(View v){
			

			datePicker = (CalendarView) v.findViewById(R.id.calendarDatePicker);
			
			
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
			
			reportTextView = (TextView) v.findViewById(R.id.reportTextView);
			
			
			dateFromTextView = (TextView) v.findViewById(R.id.dateFromtextView);
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
			
			timeFromTextView = (TextView) v.findViewById(R.id.timeFromTextView);
			timeToTextView = (TextView) v.findViewById(R.id.timeToTextView);
			
			changeTimeFrom = (Button) v.findViewById(R.id.timeFromButton);
			changeTimeFrom.setOnClickListener(new OnClickListener(){
				
				@Override
				public void onClick(View arg0) {
	
					setPickingTimeFrom();
					reportTextView.setText("");
				}
	
	        });
			changeTimeTo = (Button) v.findViewById(R.id.timeToButton);
			changeTimeTo.setOnClickListener(new OnClickListener(){
				
				@Override
				public void onClick(View arg0) {
	
					setPickingTimeTo();
					reportTextView.setText("");
				}
	
	        });
			
			showReport = (Button) v.findViewById(R.id.showReportDateAndTimeButton);
			showReport.setOnClickListener(new OnClickListener(){
	
				@Override
				public void onClick(View arg0) {
					
					if(selectedDateFrom == 0 || selectedDateTo == 0){
						return;
					}
					
					if(selectedTimeFrom == 0 || selectedTimeTo == 0){
						return;
					}
	
					loadReport(selectedDateFrom, selectedDateTo, selectedTimeFrom, selectedTimeTo);
				}
	
	        });
			
			 excludeDaysOfTheWeekButtton = (Button)v.findViewById(R.id.excludeDayButton);
			 
			 excludeDaysOfTheWeekButtton.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {

							openExclideDaysDialog();

					}

		        });
	      
	    }
		
	private void loadFromSavedInstance(Bundle savedInstanceState){
		Calendar clendar = Calendar.getInstance();
		byte[] checkedDaysForSave = savedInstanceState.getByteArray("selectedDaysOfTheWeekReportForDateAndTimePeriod");
		if(checkedDaysForSave != null){

			 this.checkedDays = new boolean[checkedDaysForSave.length];
			  for(int i = 0; i < checkedDaysForSave.length; i++){
				  if(checkedDaysForSave[i] == 1){
					  checkedDays[i] = true;
				  }
				  else{
					  checkedDays[i] = false;
				  }
			  }
		}
		else{
			 this.checkedDays = new boolean[] {true, true, true, true, true, true, true};
		}
		
		 selectedDateFrom = savedInstanceState.getLong("selectedDateFromReportForDateAndTimePeriod", 0);
	
		 if(selectedDateFrom != 0){
			
			 clendar.setTimeInMillis(selectedDateFrom);
			 
			 this.updateDateFrom(clendar.get(Calendar.YEAR), clendar.get(Calendar.MONTH), clendar.get(Calendar.DATE));
		  }
		 
		 selectedDateTo = savedInstanceState.getLong("selectedDateToReportForDateAndTimePeriod", 0);
		 
		  if(selectedDateTo != 0){
			  
			  clendar.setTimeInMillis(selectedDateTo);
				 
			  this.updateDateTo(clendar.get(Calendar.YEAR), clendar.get(Calendar.MONTH), clendar.get(Calendar.DATE));
		  }
		  
		  selectedTimeFrom = savedInstanceState.getLong("selectedTimeFromReportForDateAndTimePeriod", 0);
		  if(selectedTimeFrom != 0){
				
				 clendar.setTimeInMillis(selectedTimeFrom);
				 
				 this.updateTimeFrom(clendar.get(Calendar.HOUR_OF_DAY), clendar.get(Calendar.MINUTE));
			  }
			 
		  selectedTimeTo = savedInstanceState.getLong("selectedTimeToReportForDateAndTimePeriod", 0);
			 
			  if(selectedTimeTo != 0){
				  
				  clendar.setTimeInMillis(selectedTimeTo);
					 
				  this.updateTimeTo(clendar.get(Calendar.HOUR_OF_DAY), clendar.get(Calendar.MINUTE));
			  }
		
		 
		  
		  if(savedInstanceState.getBoolean("loadedReportReportForDateAndTimePeriod", false)){
			  
			  this.loadReport(selectedDateFrom, selectedDateTo, selectedTimeFrom, selectedTimeTo);
		  }
		  
		
		  
		    long dialogOpenDateDateFrom = savedInstanceState.getLong("dialogOpenDateDateFromReportForDateAndTimePeriod", 0);
			
			 if(dialogOpenDateDateFrom != 0){
				 clendar.setTimeInMillis(dialogOpenDateDateFrom);

				 this.openDateFromDialog(clendar);
			  }
			
			 long dialogOpenDateDateTo = savedInstanceState.getLong("dialogOpenDateDateToReportForDateAndTimePeriod", 0);
			 
			 if(dialogOpenDateDateTo != 0){
				 clendar.setTimeInMillis(dialogOpenDateDateTo);
				 this.openDateToDialog(clendar);
			  }
				
			 long dialogOpenTimeFrom = savedInstanceState.getLong("dialogOpenTimeFromReportForDateAndTimePeriod", 0);
				
			 if(dialogOpenTimeFrom != 0){
				 clendar.setTimeInMillis(dialogOpenTimeFrom);

				 this.openTimeFromDialog(clendar);
			  }
			
			 long dialogOpenTimeTo = savedInstanceState.getLong("dialogOpenTimeToReportForDateAndTimePeriod", 0);
			 
			 if(dialogOpenTimeTo != 0){
				 clendar.setTimeInMillis(dialogOpenTimeTo);
				 this.openDateToDialog(clendar);
			  }
			 
			
			 
			 if(savedInstanceState.getBoolean("dialogOpenExcludeDaysReportForDateAndTimePeriod", false)){
				 this.openExclideDaysDialog();
			 };
		
    }
	
    private void loadReport(long from, long to, long timeFrom, long timeTo){
    	
    	Calendar calendar = Calendar.getInstance();
    	
    	calendar.setTimeInMillis(from);
    	int yearFrom = calendar.get(Calendar.YEAR);
    	int monthFrom = calendar.get(Calendar.MONTH);
    	int dayFrom = calendar.get(Calendar.DAY_OF_MONTH);
    	calendar.setTimeInMillis(timeFrom);
    	int hourFrom = calendar.get(Calendar.HOUR_OF_DAY);
    	int minutesFrom = calendar.get(Calendar.MINUTE);
    	
    	calendar.setTimeInMillis(to);
    	int yearTo = calendar.get(Calendar.YEAR);
    	int monthTo = calendar.get(Calendar.MONTH);
    	int dayTo = calendar.get(Calendar.DAY_OF_MONTH);
    	calendar.setTimeInMillis(timeTo);
    	int hourTo = calendar.get(Calendar.HOUR_OF_DAY);
    	int minutesTo = calendar.get(Calendar.MINUTE);
    	
    	
    	List<Integer> passesDaysValuesDependingOnCalendar = new ArrayList<Integer>();
    	StringBuilder excludedDays = new StringBuilder();
    	
    	 for(int i = 0; i < checkedDays.length; i++){
			  if(checkedDays[i]){
				  passesDaysValuesDependingOnCalendar.add(i + 1);
			  }
			  else{
				  excludedDays.append(this.daysOfTheWeek[i]);
				  excludedDays.append(" ;");
			  }
			  
		  }
    	
    	PeriodReport report = 
    			this.repository.getCigarettesPerAndTimePeriod(
    					yearFrom, yearTo, monthFrom, monthTo, 
    					dayFrom, dayTo, hourFrom, hourTo, minutesFrom, minutesTo, passesDaysValuesDependingOnCalendar);

    	
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
		
		

		reportTextView.setText(String.format("%s-%s-%s to %s-%s-%s between %s-%s and %s-%s %s %s %s %s %s Excluded days: %s", 
				dayFrom,  new DateFormatSymbols().getMonths()[monthFrom], yearFrom,
				dayTo,  new DateFormatSymbols().getMonths()[monthTo], yearTo,
				hourFrom, minutesFrom,
				hourTo, minutesTo,
				System.getProperty("line.separator"),
				status, System.getProperty("line.separator"), reportText, System.getProperty("line.separator"), excludedDays));
		
    }

    private void setPickingDateFrom(){
    	
    	Calendar clendar = Calendar.getInstance();
    	
    	if(selectedDateFrom != 0){
     		 
    		clendar.setTimeInMillis(selectedDateFrom);
       		
      	 }
    	
    	openDateFromDialog(clendar);

		
	
    }
    
    private void openExclideDaysDialog(){
	
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle("Exclude days");
	        builder.setMultiChoiceItems(daysOfTheWeek, checkedDays,
	                new DialogInterface.OnMultiChoiceClickListener() {
	         @Override
	         public void onClick(DialogInterface dialog, int indexSelected,
	                 boolean isChecked) {
	             if (isChecked) {
	                
	          	   checkedDays[indexSelected] = true;
	                
	             } else{
	                
	          	   checkedDays[indexSelected] = false;
	             }
	         }
	     })
	     
	     .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface dialog, int id) {
	        	 excludeDaysDialog = null;
	         }
	     });
	     
	        excludeDaysDialog = builder.create();//AlertDialog dialog; create like this outside onClick
	        excludeDaysDialog.show();
	       	
      }
    
    private void openDateFromDialog(Calendar clendar){
	
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

    	
	 	dpdFromDate.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());

    	
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
    	
    	openDateToDialog(clendar);
   }
    
    private void openDateToDialog(Calendar clendar){
    	
    	
    	
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
    
	private void setPickingTimeFrom(){
	    	
	    	Calendar clendar = Calendar.getInstance();
	    	
	    	if(selectedTimeFrom != 0){
	     		 
	    		clendar.setTimeInMillis(selectedTimeFrom);
	       		
	      	 }
	    	
	    	openTimeFromDialog(clendar);
	
			
		
	    }
    
    
    private void openTimeFromDialog(Calendar clendar){
    	
    	
    	
		 tpdFromTime = new TimePickerDialog(getActivity(), 
					new TimePickerDialog.OnTimeSetListener() {
						
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
							Calendar calendar =	Calendar.getInstance();
					   	    calendar.set(1, 1, 1, hourOfDay, minute);
					   	 
					   	    updateTimeFrom(hourOfDay, minute);
					   	 tpdFromTime = null;
							
						}
					}
				   	   
			, clendar.get(Calendar.HOUR_OF_DAY), clendar.get(Calendar.MINUTE), true);
	
		
	    	tpdFromTime.show();
    	
   }
    
    private void setPickingTimeTo(){
    	
    	Calendar clendar = Calendar.getInstance();
    	
    	if(selectedTimeTo != 0){
     		 
    		clendar.setTimeInMillis(selectedTimeTo);
       		
      	 }
    	
    	openTimeToDialog(clendar);

		
	
    }


	private void openTimeToDialog(Calendar clendar){

		 tpdToTime = new TimePickerDialog(getActivity(), 
					new TimePickerDialog.OnTimeSetListener() {
						
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
							Calendar calendar =	Calendar.getInstance();
					   	    calendar.set(1, 1, 1, hourOfDay, minute);
					   	    
					   	    updateTimeTo(hourOfDay, minute);
					   	 tpdToTime = null;
							
						}
					}
				   	   
			, clendar.get(Calendar.HOUR_OF_DAY), clendar.get(Calendar.MINUTE), true);
		
		
		 tpdToTime.show();
		
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
		
		private void updateTimeFrom(int hour, int minutes){
			// validation
			Calendar cal = Calendar.getInstance();
			cal.set(1, 1, 1, hour, minutes);
			if(selectedTimeTo != 0 && selectedTimeTo < cal.getTimeInMillis()){
				
				timeFromTextView.setText("should be less than to 'time to'");
				return;
			}
			selectedTimeFrom = cal.getTimeInMillis();
			timeFromTextView.setText(
					 String.format("%s:%s", hour, minutes));
		}
		
		private void updateTimeTo(int hour, int minutes){
			// validation
			Calendar cal = Calendar.getInstance();
			cal.set(1, 1, 1, hour, minutes);
			selectedTimeTo = cal.getTimeInMillis();
			if(selectedTimeFrom != 0 && selectedTimeFrom > cal.getTimeInMillis()){
				
				timeToTextView.setText("should be grater than 'time from'");
			
				return;
			}
			timeToTextView.setText(
					 String.format("%s:%s", hour, minutes));
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
			  
			  savedInstanceState.putLong("dialogOpenDateDateFromReportForDateAndTimePeriod", cal.getTimeInMillis());
		  }
		  
		  if(dpdToDate != null){
			  Calendar cal = Calendar.getInstance();
			  int selectedYear = dpdToDate.getDatePicker().getYear();
			  int selectedMonth = dpdToDate.getDatePicker().getMonth();
			  int selectedDay = dpdToDate.getDatePicker().getDayOfMonth();
			  cal.set(selectedYear, selectedMonth, selectedDay);
			  
			  savedInstanceState.putLong("dialogOpenDateDateToReportForDateAndTimePeriod", cal.getTimeInMillis());
		  }
		  
		  
		  if(tpdFromTime != null){

			  savedInstanceState.putLong("dialogOpenTimeFromReportForDateAndTimePeriod", selectedTimeFrom);
		  }
		  
		  if(tpdToTime != null){

			  savedInstanceState.putLong("dialogOpenTimeToReportForDateAndTimePeriod", selectedTimeTo);
		  }
		  
		  if(selectedTimeFrom != 0){
			  
			  savedInstanceState.putLong("selectedTimeFromReportForDateAndTimePeriod", selectedTimeFrom);
		  }
		  
		  if(selectedTimeTo != 0){
			  
			  savedInstanceState.putLong("selectedTimeToReportForDateAndTimePeriod", selectedTimeTo);
		  }
		  
		  if(selectedDateFrom != 0){
			  
			  savedInstanceState.putLong("selectedDateFromReportForDateAndTimePeriod", selectedDateFrom);
		  }
		  
		  if(selectedDateTo != 0){
			  
			  savedInstanceState.putLong("selectedDateToReportForDateAndTimePeriod", selectedDateTo);
		  }
		  
		  if(excludeDaysDialog != null){
			  
			  savedInstanceState.putBoolean("dialogOpenExcludeDaysReportForDateAndTimePeriod", true);
		  }
		 
		  if(checkedDays != null){
			  
			byte[] checkedDaysForSave = new byte[checkedDays.length];
			  for(int i = 0; i < checkedDays.length; i++){
				  if(checkedDays[i]){
					  checkedDaysForSave[i] = 1;
				  }
				  else{
					  checkedDaysForSave[i] = 0;
				  }
			  }
			  
			  savedInstanceState.putByteArray("selectedDaysOfTheWeekReportForDateAndTimePeriod", checkedDaysForSave);
		  }
		  
		  
		  
		  
		 
		  

			  if(this.reportTextView != null && this.reportTextView.getText().toString().trim().length() > 0){
				  
				  savedInstanceState.putBoolean("loadedReportReportForDateAndTimePeriod", true);
			  }
		  }
		}

