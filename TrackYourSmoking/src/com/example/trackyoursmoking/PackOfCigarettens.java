package com.example.trackyoursmoking;

import java.security.InvalidParameterException;

public class PackOfCigarettens {
	
	private double price;
	private int countOfCigarettes;
	
	public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
    	
    	if(price < 0){
    		throw new InvalidParameterException("The 'price' can not be negative number.");
    	}
    	
        this.price = price;
    }
    
    public int getCountOfCigarettes() {
        return countOfCigarettes;
    }

    public void setCountOfCigarettes(int countOfCigarettes) {
    	
    	if(countOfCigarettes < 0){
    		throw new InvalidParameterException("The 'countOfCigarettes' can not be negative number.");
    	}
    	
        this.countOfCigarettes = countOfCigarettes;
    }
    
    
	 @Override public String toString() {
		    StringBuilder result = new StringBuilder();
		    String NEW_LINE = System.getProperty("line.separator");

		    result.append(" Cigarettes count: ");
		    result.append(this.getCountOfCigarettes());
		    result.append(NEW_LINE);
		    result.append(" Price: ");
		    result.append(this.getPrice());
		    return result.toString();
		  }
}
