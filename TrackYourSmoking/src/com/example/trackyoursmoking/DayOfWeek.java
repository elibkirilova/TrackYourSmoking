package com.example.trackyoursmoking;

public class DayOfWeek {
	
	public static final int SUNDAY = 1;
	public static final int MONDAY = 2;
    public static final int TUESDAY = 3;
    public static final int WEDNESDAY = 4;
    public static final int THURSDAY = 5;
    public static final int FRIDAY = 6;
    public static final int SATURDAY = 7;
   
   
    
    public static String[] getAll(){
    	String[] days = new String[] {
    			"Sunday",
    			"Monday",
    			"Tuesday",
    			"Wednesday",
    			"Thursday",
    			"Friday",
    			"Saturday",
    	};
    	
    	return days;
    }
}
