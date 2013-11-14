package com.example.trackyoursmoking;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TableLayout;

public class ReportForDatesPeriod extends Fragment {

	
	private DatePicker datePicker;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.repot_for_dates_period, container, false);
    
        datePicker = (DatePicker) v.findViewById(R.id.datesIntervalDatePicker);
        
        int display_mode = getResources().getConfiguration().orientation;

        if (display_mode == 1) {
        	datePicker.setCalendarViewShown(true);
        	datePicker.setSpinnersShown(false);
        } else {
        	//landscape
        	
        	datePicker.setCalendarViewShown(false);
        }      
        
        
		     
        
        Calendar clendar = new GregorianCalendar();
        
        datePicker.getCalendarView().setFirstDayOfWeek(Calendar.getInstance().getFirstDayOfWeek());
        
        datePicker.setMaxDate(clendar.getTimeInMillis());
        
        
        
          return v;

	}
	
   
}
