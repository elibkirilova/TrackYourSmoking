package com.example.trackyoursmoking;


import gif.decoder.GifRun;
import android.os.Bundle;
import android.app.Activity;

import android.content.Intent;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private  IRepository repository;
	
	public MainActivity(){
		this.repository = new TestRepository();
	}
	
	public MainActivity(IRepository repository){
		this.repository = repository;
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        InitialUserData userData = repository.getInitialData();
       // userData = new InitialUserData();
        
        if(userData == null){
        	 
    		 startActivity(new Intent(getApplication(), InitialUserDataActivity.class));
    		 return;
        	 
        }
       
        TextView test = (TextView)findViewById(R.id.TestTextView);
        test.setText(userData.toString());
        
        SurfaceView v = (SurfaceView) findViewById(R.id.imgSmokingProgressSurfaceView);
        GifRun w = new  GifRun();
		w.LoadGiff(v, this, R.drawable.smoke_under_the_minimum);
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
   
	}
