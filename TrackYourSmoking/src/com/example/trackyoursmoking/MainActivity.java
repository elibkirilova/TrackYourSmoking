package com.example.trackyoursmoking;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.AlertDialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;

import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity  {
	
	private  IRepository repository;
	
	public MainActivity(){
		this.repository = new TestRepository();
	}
	
	public MainActivity(IRepository repository){
		this.repository = repository;
	}
	
	Button addCigaretteButton;
	AnimationDrawable frameAnimation;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitialUserData userData = this.getInitialData();
        
        
        if(userData == null){
        	 
    		 startActivity(new Intent(getApplication(), InitialUserDataActivity.class));
    		 return;
        	 
        }
        
        addCigaretteButton = (Button)findViewById(R.id.addCigaretteButton);
        
        addCigaretteButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				reuestAddCigarette();
			}

        });
       

        TextView dailyDataTextView = (TextView)findViewById(R.id.dailyDataTextView);
        dailyDataTextView.setText("Today: "+ repository.getCigarettesSmokedToday());

        //TextView initialDataTextView = (TextView)findViewById(R.id.initialDataTextView);
        //initialDataTextView.setText(userData.toString());
        
        ImageView img = (ImageView)findViewById(R.id.imageView1);
        img.setBackgroundResource(R.drawable.animation_no_smoking);

        frameAnimation = (AnimationDrawable) img.getBackground();
        
        frameAnimation.start();
 
    }
	
	
	@Override
    public void onWindowFocusChanged(boolean hasFocus) {        
        super.onWindowFocusChanged(hasFocus);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//  
//        GifFragment pricingFragment = new GifFragment();
//          
//        fragmentTransaction.replace(R.id.gifFragmentLayout, pricingFragment);
//          
//        fragmentTransaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
       // return true;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	switch (item.getItemId()) {
	        case R.id.action_settings:
	        startActivity(new Intent(getApplication(), UserSettingActivity.class));
	        return true;
	       
        	default:
        	return super.onOptionsItemSelected(item);
	    	}
	    }
	    private void reuestAddCigarette(){

			 new AlertDialog.Builder(this)
			 	.setTitle("Smoked another one?")
	     	    .setMessage("One cigarette will be added to your daily amount.")
	     	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	     	        public void onClick(DialogInterface dialog, int which) { 
	     	            repository.addCigaretteToday();
	     	           Intent intent = getIntent();
	     	           finish();
	     	           overridePendingTransition(0, 0);
	     	           intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	     	           startActivity(intent);  
	     	        }
	     	     })
	     	     .setNegativeButton("No",new DialogInterface.OnClickListener() {
		     	        public void onClick(DialogInterface dialog, int which) { 
		     	           
		     	        }
		     	     })
	     	     .show();
	}

		private InitialUserData getInitialData() {
			
			InitialUserData initialData = new InitialUserData();
			
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
			
			boolean hasInitialData = sharedPrefs.getBoolean("has_initial_data", false);
			
			if(hasInitialData){
				
					initialData.setMaxCigarettensPerDay(sharedPrefs.getInt("max_cigarettes_per_day", 0));
					initialData.setMinCigarettensPerDay(sharedPrefs.getInt("min_cigarettes_per_day", 0));
					initialData.setPricePerCigarette(Double.longBitsToDouble(sharedPrefs.getLong("price_per_cigarette", 5)));
					Long moneyLimit = sharedPrefs.getLong("month_money_limit", -1);
					if(moneyLimit != -1){
						initialData.setMonthMoneyLimit(Double.longBitsToDouble(moneyLimit));
					}
					if(sharedPrefs.getBoolean("pricing_on_pack", false)){
						PackOfCigarettens pack = new PackOfCigarettens();
						pack.setCountOfCigarettes(sharedPrefs.getInt("count_in_pack", 5));
						pack.setPrice(Double.longBitsToDouble(sharedPrefs.getLong("pack_price", 5)));
						initialData.setPackOfCigarettens(pack);
					}
					return initialData;
				}			
			return null;
		}
		
	}

