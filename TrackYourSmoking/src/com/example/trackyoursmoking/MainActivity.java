package com.example.trackyoursmoking;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
//        

        
        if(userData == null){
        	 
    		 startActivity(new Intent(getApplication(), InitialUserDataActivity.class));
    		 return;
        	 
        }
        
      
        Intent resultIntent = new Intent(this, InitialUserDataActivity.class);
        NotificationCompat.Builder mBuilder =
        	    new NotificationCompat.Builder(this)
       	    .setSmallIcon(R.drawable.ic_launcher)
       	    .setContentTitle("Above your daily limit")
       	    .setContentText("with 5 cigarettes.")
        	    .setAutoCancel(true).setSound (Uri.parse("android.resource://"
        	            + getApplicationContext().getPackageName() + "/" + R.raw.funeral_march));
       
//        Intent resultIntent = new Intent(this, InitialUserDataActivity.class);
//
//     // The stack builder object will contain an artificial back stack for the
//     // started Activity.
//     // This ensures that navigating backward from the Activity leads out of
//     // your application to the Home screen.
  TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//     // Adds the back stack for the Intent (but not the Intent itself)
    stackBuilder.addParentStack(InitialUserDataActivity.class);
//     // Adds the Intent that starts the Activity to the top of the stack
    stackBuilder.addNextIntent(resultIntent);
    PendingIntent resultPendingIntent =
            stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
            );
     mBuilder.setContentIntent(resultPendingIntent);
     NotificationManager mNotificationManager =
         (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
     // mId allows you to update the notification later on.
     mNotificationManager.notify(12345, mBuilder.build());
       
        
        

        TextView dailyDataTextView = (TextView)findViewById(R.id.dailyDataTextView);
        dailyDataTextView.setText("Today: "+ repository.getCigarettesSmokedToday());
        //dailyDataTextView.setText(userData.toString());

        //TextView initialDataTextView = (TextView)findViewById(R.id.initialDataTextView);
        //initialDataTextView.setText(userData.toString());
        
        ImageView img;
       

        int display_mode = getResources().getConfiguration().orientation;

        if (display_mode == 1) {
           img = (ImageView)findViewById(R.id.smokingStateImageView);
        } else {
        	img = (ImageView)findViewById(R.id.smokingStateImageViewSmaller);
        }      
        
        img.setBackgroundResource(R.drawable.animation_under_minimum_smoking);
        
        
        addCigaretteButton = (Button)findViewById(R.id.addCigaretteButton);
        
        addCigaretteButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				reuestAddCigarette();
			}

        });
        
        
        frameAnimation = (AnimationDrawable) img.getBackground();
        
        frameAnimation.start();

    }
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
       // return true;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        
        
      NotificationCompat.Builder mBuilder =
	    new NotificationCompat.Builder(this)
	    .setSmallIcon(R.drawable.ic_launcher)
	    .setContentTitle("I'm just hangin'")
	    .setContentText("this app settings -> allow hangin")
	    .setAutoCancel(false);

Intent resultIntent = new Intent(this, InitialUserDataActivity.class);



// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
 TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
  stackBuilder.addParentStack(InitialUserDataActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
  stackBuilder.addNextIntent(resultIntent);
  PendingIntent resultPendingIntent =
        stackBuilder.getPendingIntent(
           0,
         PendingIntent.FLAG_UPDATE_CURRENT
     );
 mBuilder.setContentIntent(resultPendingIntent);
 NotificationManager mNotificationManager =
 (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
  mNotificationManager.notify(12, mBuilder.build());

        
        return true;
    }
    
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	switch (item.getItemId()) {
	        case R.id.action_settings:
	        startActivity(new Intent(getApplication(), UserSettingActivity.class));
	       
	        return true;
	       
	        case R.id.action_reporting:
		        startActivity(new Intent(getApplication(), ReportingActivity.class));
		       
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
					return initialData;
				}			
			return null;
		}
		
	}

