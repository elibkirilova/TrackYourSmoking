package com.example.trackyoursmoking;

public class ClassicSingleton {

	 private static ClassicSingleton instance;
	 
	   protected ClassicSingleton() {
	      // Exists only to defeat instantiation.
	   }
	   public static ClassicSingleton getInstance() {
	      if(instance == null) {
	         instance = new ClassicSingleton();
	      }
	      return instance;
	   }
}
