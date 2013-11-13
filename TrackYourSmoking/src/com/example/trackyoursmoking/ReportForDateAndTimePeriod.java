package com.example.trackyoursmoking;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

public class ReportForDateAndTimePeriod extends Fragment {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		 View v = inflater.inflate(R.layout.report_for_dates_and_time_period, container, false);
		    
		DatePickerDialog dpdFromDate = new DatePickerDialog(getActivity(), dateOnDateSetListener, 2013, 12,13);
        dpdFromDate.show();

	      dpdFromDate.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog, int which) {
	             if (which == DialogInterface.BUTTON_NEGATIVE) {
	                  
	             }
	          	} 
	          });
	      
	      return v;
	}
	
	   private DatePickerDialog.OnDateSetListener dateOnDateSetListener = new DatePickerDialog.OnDateSetListener() {

   	    @Override
   	    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
   	       // datePick.setText(new StringBuilder().append(selectedDay).append("-").append(selectedMonth).append("-").append(selectedYear));
   	    }
   	};
}
