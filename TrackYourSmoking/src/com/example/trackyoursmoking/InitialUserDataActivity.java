package com.example.trackyoursmoking;

import java.security.InvalidParameterException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class InitialUserDataActivity extends Activity {

	
		private  IRepository repository;
		
		public InitialUserDataActivity(){
			this.repository = new TestRepository();
		}
		
		public InitialUserDataActivity(IRepository repository){
			this.repository = repository;
		}
	
		private static final String NEW_LINE = System.getProperty("line.separator");
		
		private static final int MIN_CIG_PER_DAY = 1;
		private static final int MAX_CIG_PER_DAY = 100;
		private static final int DEFAULT_MIN_CIG_PER_DAY = 5;
		private static final int DEFAULT_MAX_CIG_PER_DAY = 20;
		
		private int minCigPerDay;
		private int maxCigPerDay;
		
		TextView minCigPerDayTextView;
		TextView maxCigPerDayTextView;
		
		EditText monthMoneyLimitEditText;
		
		SeekBar minCigPerDaySeekBar;
		SeekBar maxCigPerDaySeekBar;

		Button saveSettingsButton;

		
		RadioGroup pricingRadioGroup;
		RadioButton perPackRadio;
		RadioButton perCigaretteRadio;
		
		@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.initial_settings);
	        
	        minCigPerDayTextView = (TextView) findViewById(R.id.minCigPerDayValue);
	        maxCigPerDayTextView = (TextView) findViewById(R.id.maxCigPerDayValue);
	        
	        monthMoneyLimitEditText = (EditText) findViewById(R.id.monthMoneyLimitEditText);
	        
	        minCigPerDaySeekBar = (SeekBar) findViewById(R.id.minCigPerDaySeekBar);
	        maxCigPerDaySeekBar = (SeekBar) findViewById(R.id.maxCigPerDaySeekBar);
	       
	        
	        saveSettingsButton = (Button)findViewById(R.id.saveSettingsButton);
	        
	        saveSettingsButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					saveInitialSettings();
					
				}

	        });
	        
	        
//	        pricingRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
//	        {
//	            public void onCheckedChanged(RadioGroup group, int checkedId) {
//	                 
//	               if(perPackRadio.isChecked()){
//	            	   FragmentManager fragmentManager = getFragmentManager();
//	       	        
//		       	       FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//		       	 
//		       	       PricingMainInfoFragment pricingFragment = new PricingMainInfoFragment();
//		       	         
//		       	     //  fragmentTransaction.replace(R.id.pricingFragmentLayout, pricingFragment);
//		       	         
//		       	       fragmentTransaction.commit();
//	               }
//	               else if(perCigaretteRadio.isChecked()){
//	            	   FragmentManager fragmentManager = getFragmentManager();
//		       	        
//		       	       FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//		       	 
//		       	       PricingMainInfoFragment pricingFragment = new PricingMainInfoFragment();
//		       	         
//		       	     //  fragmentTransaction.replace(R.id.pricingFragmentLayout, pricingFragment);
//		       	         
//		       	       fragmentTransaction.commit();
//	               }
//	            }
//	        });
//	        
	        
	        if(savedInstanceState == null){
	        	
	        	minCigPerDay = DEFAULT_MIN_CIG_PER_DAY;
	        	maxCigPerDay = DEFAULT_MAX_CIG_PER_DAY;
	    	}
	        else{
	        	minCigPerDay = savedInstanceState.getInt("CURRENT_MIN_CIG_PER_DAY");
	        	maxCigPerDay = savedInstanceState.getInt("CURRENT_MAX_CIG_PER_DAY");
	        }
	        minCigPerDaySeekBar.setProgress(minCigPerDay);
	        maxCigPerDaySeekBar.setProgress(maxCigPerDay);
	        
	        minCigPerDaySeekBar.setOnSeekBarChangeListener(minCigPerDaySeekBarListener);
	        maxCigPerDaySeekBar.setOnSeekBarChangeListener(maxCigPerDaySeekBarListener);

	        minCigPerDayTextView.setText(Integer.toString(minCigPerDay));
	        maxCigPerDayTextView.setText(Integer.toString(maxCigPerDay));
	        
	        minCigPerDaySeekBar.setMax(maxCigPerDay);
	        maxCigPerDaySeekBar.setMax(MAX_CIG_PER_DAY);
	        
//	        FragmentManager fragmentManager = getFragmentManager();
//	        
//	        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//	 
//	        PricingMainInfoFragment pricingFragment = new PricingMainInfoFragment();
//	         
//	       // fragmentTransaction.add(R.id.pricingFragmentLayout, pricingFragment);
//	         
//	        fragmentTransaction.commit();
	    }


	   
	    
	    private OnSeekBarChangeListener minCigPerDaySeekBarListener = new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean arg2) {
				
				if(progress < MIN_CIG_PER_DAY){
					progress = MIN_CIG_PER_DAY;
				}
				minCigPerDay = progress;
				minCigPerDayTextView.setText(Integer.toString(minCigPerDay));
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
	    	
	    };
	    
	    private OnSeekBarChangeListener maxCigPerDaySeekBarListener = new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean arg2) {
				
				if(progress < MIN_CIG_PER_DAY){
					progress = MIN_CIG_PER_DAY;
				}
				maxCigPerDay = progress;
				maxCigPerDayTextView.setText(Integer.toString(maxCigPerDay));
				minCigPerDaySeekBar.setMax(maxCigPerDay);
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
	    	
	    };
	    
		protected void onSaveInstanceState(Bundle outState){
	    		         
	    	        super.onSaveInstanceState(outState);
	    		         
	    	        outState.putInt("CURRENT_MIN_CIG_PER_DAY", minCigPerDay);
	    	        outState.putInt("CURRENT_MAX_CIG_PER_DAY", maxCigPerDay);
	    		         
	    	    }
		
		private void saveInitialSettings(){
			
			
			boolean isValid = true;
			
			StringBuilder errors = new StringBuilder();
			
			InitialUserData userSettings = new InitialUserData();
			
			userSettings.setMinCigarettensPerDay(minCigPerDay);
			userSettings.setMaxCigarettensPerDay(maxCigPerDay);
			
			String moneyLimitValue = monthMoneyLimitEditText.getText().toString().trim();
			
			if(moneyLimitValue.length() > 0){
				try{
					userSettings.setMonthMoneyLimit(Double.parseDouble(moneyLimitValue));
				}
				catch(InvalidParameterException ex){
					errors.append(ex.getMessage());
					errors.append(NEW_LINE);
					isValid = false;
				}
				
			}
			
			
            	 EditText pricePerCigaretteEditText = (EditText) findViewById(R.id.pricePerCigaretteEditText);
            	 String pricePerCigarette = pricePerCigaretteEditText.getText().toString().trim();
				
					if(pricePerCigarette.length() > 0){
						try{
							userSettings.setPricePerCigarette(Double.parseDouble(pricePerCigarette));
						}
						catch(InvalidParameterException ex){
							errors.append(ex.getMessage());
							errors.append(NEW_LINE);
							isValid = false;
						}
						
					}
					else{
						pricePerCigaretteEditText.setHint("(required)");
						isValid = false;
					}
            
           
			 if(!isValid){
				 if(errors.length() != 0){
					 new AlertDialog.Builder(this)
					 	.setTitle("Please fix the following error(s)")
		         	    .setMessage(errors.toString())
		         	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		         	        public void onClick(DialogInterface dialog, int which) { 
		         	            // continue with delete
		         	        }
		         	     })
		         	     .show();
				 }
			 }
			
			 else{
				 Intent theIndent = new Intent(getApplication(), MainActivity.class);
				 this.setInitialData(userSettings);
				 startActivity(theIndent);
				 
			 }
			 
		}
		
		private void setInitialData(InitialUserData initialData) {

					SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
					SharedPreferences.Editor editor = sharedPrefs.edit();
					
					editor.putBoolean("has_initial_data", true);

					editor.putInt("max_cigarettes_per_day", initialData.getMaxCigarettensPerDay());
					editor.putInt("min_cigarettes_per_day", initialData.getMinCigarettensPerDay());
					
					if(initialData.getMonthMoneyLimit() != null)
                    {
                            editor.putLong("month_money_limit", Double.doubleToLongBits((Double) initialData.getMonthMoneyLimit()));
                    }
                    editor.putLong("price_per_cigarette", Double.doubleToLongBits((Double) initialData.getPricePerCigarette()));
					
					editor.commit();
								
				}
		
		
}
