package com.example.trackyoursmoking;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SmokingActivity implements Comparable<SmokingActivity> {

	private int id;
	
	private double cigarettePrice;
	
	private Date dateAndTime;
	
	
	
	 public int getId() {
	        return this.id;
	    }
	
	 public void setId(int id) {
	    	
	        this.id = id;
	    }
	 
	 public double getCigarettePrice() {
	        return this.cigarettePrice;
	    }
	
	 public void setCigarettePrice(double cigarettePrice) {
	    	
	        this.cigarettePrice = cigarettePrice;
	    }
	 
	 
	 public Date getDateAndTime() {
	        return this.dateAndTime;
	    }
	
	 public void setDateAndTime(Date dateAndTime) {
	    	
	        this.dateAndTime = dateAndTime;
	    }



	@Override
	public int compareTo(SmokingActivity another) {
		Calendar cal1 = Calendar.getInstance();
    	Calendar cal2 = Calendar.getInstance();
    	cal1.setTime(this.getDateAndTime());
    	
    
    	
    	if(another != null){
    		cal2.setTime(another.getDateAndTime());
    	}
    	else {
    		 throw new NullPointerException("Can not compare value with null."); 
    	}
    	

    	if(cal1.after(cal2)){
    		return 1;
    	}

    	if(cal1.before(cal2)){
    		return -1;
    	}

    		return 0;
	}
	    
	 @Override 
	 public String toString() {
		    StringBuilder result = new StringBuilder();
		    String NEW_LINE = System.getProperty("line.separator");

		    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

			 // Get the date today using Calendar object.
			 Date today = Calendar.getInstance().getTime();        
			
			 String reportDate = df.format(this.getDateAndTime());

		    result.append(reportDate);
		    result.append(NEW_LINE);
		    result.append(this.getCigarettePrice());
		    
		    return result.toString();
		  }
}
