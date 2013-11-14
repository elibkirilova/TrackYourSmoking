package com.example.trackyoursmoking;


import java.security.InvalidParameterException;

public class InitialUserData {

	private int minCigarettensPerDay;
	
	private int maxCigarettensPerDay;
	
	private Double monthMoneyLimit;
	
	private double pricePerCigarette;
	
	
	 public int getMinCigarettensPerDay() {
	        return minCigarettensPerDay;
	    }
	
	 public void setMinCigarettensPerDay(int minCigarettensPerDay) {
	    	
	    	if(minCigarettensPerDay <= 0){
	    		throw new InvalidParameterException("The 'minCigarettensPerDay' should be a positive number.");
	    	}
	    	
	        this.minCigarettensPerDay = minCigarettensPerDay;
	    }
	    
	 public int getMaxCigarettensPerDay() {
	        return maxCigarettensPerDay;
	    }
	
	 public void setMaxCigarettensPerDay(int maxCigarettensPerDay) {
	    	
	    	if(maxCigarettensPerDay < this.minCigarettensPerDay){
	    		throw new InvalidParameterException("The 'maxCigarettensPerDay' should be equal ol large than 'minCigarettensPerDay'.");
	    	}
	    	
	        this.maxCigarettensPerDay = maxCigarettensPerDay;
	    }
	 
	 public Double getMonthMoneyLimit() {
	        return monthMoneyLimit;
	    }
	
	 public void setMonthMoneyLimit(Double monthMoneyLimit) {
	    	
	    	if(monthMoneyLimit != null && monthMoneyLimit < 0){
	    		throw new InvalidParameterException("The 'monthMoneyLimit' can not be negative number.");
	    	}
	    	
	        this.monthMoneyLimit = monthMoneyLimit;
	    }
	   
	 public double getPricePerCigarette() {
	        return pricePerCigarette;
	    }

	    public void setPricePerCigarette(double pricePerCigarette) {
	    	
	    	if(pricePerCigarette < 0){
	    		throw new InvalidParameterException("The 'pricePerCigarette' can not be negative number.");
	    	}
	    	
	        this.pricePerCigarette = pricePerCigarette;
	    }
	    

		 @Override public String toString() {
			    StringBuilder result = new StringBuilder();
			    String NEW_LINE = System.getProperty("line.separator");

			    result.append(" Max cig per day: ");
			    result.append(this.getMaxCigarettensPerDay());
			    result.append(NEW_LINE);
			    result.append(" Min cig per day: ");
			    result.append(this.getMinCigarettensPerDay());
			    result.append(NEW_LINE);
			    result.append(" Month Limit: ");
			    result.append(this.getMonthMoneyLimit());
			    result.append(NEW_LINE);
			    result.append(" Cig Price: ");
			    result.append(this.getPricePerCigarette());
			    result.append(NEW_LINE);
			   
			    return result.toString();
			  }
		 
		 
}
