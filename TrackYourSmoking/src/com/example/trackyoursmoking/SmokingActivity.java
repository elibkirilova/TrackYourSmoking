package com.example.trackyoursmoking;

import java.security.InvalidParameterException;
import java.util.Date;

public class SmokingActivity {

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
	    
}
