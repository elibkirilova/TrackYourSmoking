package com.example.trackyoursmoking;

import java.security.InvalidParameterException;
import java.text.DecimalFormat;

public class PeriodReport {

	private int cigarettesCount;
	
	private int minCigarettensPerDay;
	
	private int maxCigarettensPerDay;
	
	private double moneySpend;
	
	private double averrageCigarettesPerDay;
	
	
	 public int getCigarettesCount() {
	        return cigarettesCount;
	    }
	
	 public void setCigarettesCount(int cigarettesCount) {
	    	
	    	if(cigarettesCount < 0){
	    		throw new InvalidParameterException("The 'cigarettesCount' should be a positive number.");
	    	}
	    	
	        this.minCigarettensPerDay = cigarettesCount;
	    }
	
	 public int getMinCigarettensPerDay() {
	        return minCigarettensPerDay;
	    }
	
	 public void setMinCigarettensPerDay(int minCigarettensPerDay) {
	    	
	    	if(minCigarettensPerDay < 0){
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
	 
	 public Double getMoneySpend() {
	        return this.moneySpend;
	    }
	
	 public void setMoneySpend(double moneySpend) {
	    	
	    	if(moneySpend < 0){
	    		throw new InvalidParameterException("The 'moneySpend' can not be negative number.");
	    	}
	    	
	        this.moneySpend = moneySpend;
	    }
	   
	 	public double getAverrageCigarettesPerDay() {
	        return averrageCigarettesPerDay;
	    }

	    public void setAverrageCigarettesPerDay(double averrageCigarettesPerDay) {
	    	
	    	if(averrageCigarettesPerDay < 0){
	    		throw new InvalidParameterException("The 'averrageCigarettesPerDay' can not be negative number.");
	    	}
	    	
	        this.averrageCigarettesPerDay = averrageCigarettesPerDay;
	    }
	    

		 @Override public String toString() {
			    StringBuilder result = new StringBuilder();
			    String NEW_LINE = System.getProperty("line.separator");
			    
			    DecimalFormat twoDForm = new DecimalFormat("#.##");
			    
			    result.append("Money spend: ");
			    result.append(twoDForm.format(this.getMoneySpend()));
			    result.append(NEW_LINE);
			    result.append("Min cig per day: ");
			    result.append(this.getMinCigarettensPerDay());
			    result.append(NEW_LINE);
			    result.append("Max cig per day: ");
			    result.append(this.getMaxCigarettensPerDay());
			    result.append(NEW_LINE);
			    
			    result.append("Averrage count of cigarettes per day: ");
			    result.append(twoDForm.format(this.getAverrageCigarettesPerDay()));
			    result.append(NEW_LINE);
			   
			    return result.toString();
			  }
		 
		 
}
