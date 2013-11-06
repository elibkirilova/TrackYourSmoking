package com.example.trackyoursmoking;


import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;

import com.example.trackyoursmoking.gifview.GifMovieView;
import com.example.trackyoursmoking.gifview.GifView;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        
        InitialUserData userData = this.getInitialData();
        //userData = new InitialUserData();
        
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
        
       
        
        TextView initialDataTextView = (TextView)findViewById(R.id.initialDataTextView);
        initialDataTextView.setText(userData.toString());
        
        InputStream stream = null;
        try {
           stream = getAssets().open("smoke_under_the_minimum.gif");
            //stream = getPicture(R.drawable.smoke_under_the_minimum);
           
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        GifMovieView view = new GifMovieView(this, stream);
        //SurfaceView v = (SurfaceView) findViewById(R.id.imgSmokingProgressSurfaceView);
        //GifRun w = new  GifRun();
		//w.LoadGiff(v, this, R.drawable.smoke_under_the_minimum);
        
    
                // Inflate the layout for this fragment
            	// InputStream stream = null;
                // try {
                //     stream = getAssets().open("smoke_under_the_minimum.gif");
                // } catch (IOException e) {
                 //    e.printStackTrace();
                // }
                 
               //GifMovieView view = new GifMovieView(this, stream);

//        GifDecoderView view = new GifDecoderView(this, stream);
        //GifWebView view = new GifWebView(this, "file:///android_asset/piggy.gif");                 
               FragmentManager fragmentManager = getSupportFragmentManager();
      	        
       	       FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
       	 
       	       Fragment pricingFragment = new GifFragment();
       	         
       	       fragmentTransaction.replace(R.id.gifFragmentLayout, pricingFragment);
       	      
       	       fragmentTransaction.commit();
      
     
        //setContentView(view);
       
      
        //setContentView(gif_view);
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
			StringBuilder builder = new StringBuilder();
			
			boolean hasInitialData = sharedPrefs.getBoolean("has_initial_data", false);
			
			if(hasInitialData){
				
				initialData.setMaxCigarettensPerDay(sharedPrefs.getInt("max_cigarettes_per_day", 0));
				initialData.setMinCigarettensPerDay(sharedPrefs.getInt("min_cigarettes_per_day", 0));
				Double moneyLimit = Double.longBitsToDouble(sharedPrefs.getLong("month_money_limit", Double.doubleToLongBits((Double) null)));
				initialData.setPricePerCigarette(sharedPrefs.getLong("price_per_cigarette", Double.doubleToLongBits((Double) null)));
				initialData.setMonthMoneyLimit(moneyLimit);
					if(sharedPrefs.getBoolean("pricing_on_pack", true)){
						PackOfCigarettens pack = new PackOfCigarettens();
						pack.setCountOfCigarettes(sharedPrefs.getInt("count_in_pack", 0));
						pack.setPrice(Double.longBitsToDouble(sharedPrefs.getLong("pack_price", Double.doubleToLongBits((Double) null))));
					}
					return initialData;
				}			
			return null;
		}
		
	}

