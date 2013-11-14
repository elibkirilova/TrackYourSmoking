package com.example.trackyoursmoking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

public class ReportForDateAndTimePeriod extends Fragment {

	
	AlertDialog dialog;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		 View v = inflater.inflate(R.layout.report_for_dates_and_time_period, container, false);
		    
//		DatePickerDialog dpdFromDate = new DatePickerDialog(getActivity(), dateOnDateSetListener, 2013, 12,13);
//        dpdFromDate.show();
//
//	      dpdFromDate.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
//	          public void onClick(DialogInterface dialog, int which) {
//	             if (which == DialogInterface.BUTTON_NEGATIVE) {
//	                  
//	             }
//	          	} 
//	          });
		 
		 Button excludeDaysOfTheWeekButtton = (Button)v.findViewById(R.id.excludeDayButton);
		 
		 excludeDaysOfTheWeekButtton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					
					   final CharSequence[] items = {" Easy "," Medium "," Hard "," Very Hard "};
				          // arraylist to keep the selected items
				          final ArrayList seletedItems=new ArrayList();

				          AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				          builder.setTitle("Select The Difficulty Level");
				          builder.setMultiChoiceItems(items, null,
				                  new DialogInterface.OnMultiChoiceClickListener() {
				           @Override
				           public void onClick(DialogInterface dialog, int indexSelected,
				                   boolean isChecked) {
				               if (isChecked) {
				                   // If the user checked the item, add it to the selected items
				                   seletedItems.add(indexSelected);
				               } else if (seletedItems.contains(indexSelected)) {
				                   // Else, if the item is already in the array, remove it
				                   seletedItems.remove(Integer.valueOf(indexSelected));
				               }
				           }
				       })
				        // Set the action buttons
				       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				           @Override
				           public void onClick(DialogInterface dialog, int id) {
				               //  Your code when user clicked on OK
				               //  You can write the code  to save the selected item here

				           }
				       })
				       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				           @Override
				           public void onClick(DialogInterface dialog, int id) {
				              //  Your code when user clicked on Cancel

				           }
				       });
				          dialog = builder.create();//AlertDialog dialog; create like this outside onClick
				          dialog.show();

				}

	        });
//	      
	   
        
	      return v;
	}
	
	
	
	   private DatePickerDialog.OnDateSetListener dateOnDateSetListener = new DatePickerDialog.OnDateSetListener() {

   	    @Override
   	    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
   	       // datePick.setText(new StringBuilder().append(selectedDay).append("-").append(selectedMonth).append("-").append(selectedYear));
   	    }
   	};
}
