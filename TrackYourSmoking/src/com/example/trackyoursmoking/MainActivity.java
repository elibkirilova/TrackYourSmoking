package com.example.trackyoursmoking;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import android.content.Context;
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

 	private String currentState;
 	
 	private boolean isDialogOpen;
 	private ProgressDialog ringProgressDialog;
 	private MediaPlayer mp;

	public MainActivity(){
		
	}
	
	public MainActivity(IRepository repository){
		this.repository = repository;
	}
	
		Button addCigaretteButton;
		AnimationDrawable frameAnimation;
		ImageView img;
		
	@Override
    	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    	this.repository = new DefaultRepository(getApplication());
        InitialUserData userData = this.repository.getInitialData();

        if(userData == null){
        	 
    		 startActivity(new Intent(getApplication(), InitialUserDataActivity.class));
    		 return;
        }

        stuckHangoutNotification();
        int display_mode = getResources().getConfiguration().orientation;

        if (display_mode == 1) {
           img = (ImageView)findViewById(R.id.smokingStateImageView);
        } else {
        	img = (ImageView)findViewById(R.id.smokingStateImageViewSmaller);
        }      

        addCigaretteButton = (Button)findViewById(R.id.addCigaretteButton);
        
        addCigaretteButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				reuestAddCigarette();
			}

        });

        updateScreen();
      
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
	       
	        case R.id.action_reporting:
		        startActivity(new Intent(getApplication(), ReportingActivity.class));
		       
		        return true;
	    	default:
	    	return super.onOptionsItemSelected(item);
	    	}
    	}
    
    	private void reuestAddCigarette(){


	    	String msg = "";
	    	String title = "";
	    	boolean showAlert = false;
	    	
	    	if(this.currentState == SmokingStates.REACHED_MAXIMUM_SMOKE_STATE){
	    		msg =  "Are you sure? I've already reached your daily limit...";
	    		title = "Reached daily limit";
	    		showAlert = true;
	    	}
	    	else if(this.currentState == SmokingStates.ABOVE_LIMIT_SMOKE_STATE){
	    		msg =  "Are you sure? Man, you smoke a lot...";
	    		title = "ABOVE DAILY LIMIT";
	    		showAlert = true;
	    	}
	    	
	    	if(showAlert){
				 new AlertDialog.Builder(this)
				 	.setTitle(title)
		     	    .setMessage(msg)
		     	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		     	        public void onClick(DialogInterface dialog, int which) { 
		     	           repository.addCigaretteToday();
		     	          vibrate();
		     	           updateScreen();
		     	          isDialogOpen = false;
		     	        }
		     	     })
		     	     .setNegativeButton("No",new DialogInterface.OnClickListener() {
			     	        public void onClick(DialogInterface dialog, int which) { 
			     	        	isDialogOpen = false;
			     	        }
			     	     })
		     	     .show();
				 isDialogOpen = true;
	    	}
	    	else{
	    		ringProgressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...",	"Adding cigarette to your daily amount...", true);
	    		ringProgressDialog.setCancelable(true);
	    		new Thread(new Runnable() {
	    			@Override
	    			public void run() {
	    					try {
								Thread.sleep(1000);
								vibrate();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	    				
	    				ringProgressDialog.dismiss();
	    				ringProgressDialog = null;
	    				
	    			}
	    		}).start();
	    		repository.addCigaretteToday();
	    		updateScreen();
	    	}
		}

		
    	private void vibrate(){
    		
    		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
    		
    		boolean allowedVibration = sharedPrefs.getBoolean("allow_vibration", false);
    		
    		if(allowedVibration){
	    			Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	        		// Vibrate for 500 milliseconds
	    			vibrator.vibrate(500);
    			}			
    	}
    	
    	
    	private void playAboveLimitMarch(){
    		
    		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
    		
    		boolean allowedSounEffects = sharedPrefs.getBoolean("allow_sound_effects", false);
    		
    		if(allowedSounEffects){
    			if(mp != null && mp.isPlaying()){
    				return;
    			}
    			mp = new MediaPlayer();
        		try {
    				mp.setDataSource(getApplication(), Uri.parse("android.resource://"
    				    + getApplicationContext().getPackageName() + "/" + R.raw.funeral_march));
    			} catch (IllegalArgumentException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (SecurityException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (IllegalStateException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        		
        		mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        			  public void onPrepared(MediaPlayer mp) {
        			      mp.start();
        			  }
        			});
        		mp.prepareAsync();
        		mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						mp.release();
						
					 }
    			});
    		}
    		
    	}
    	
    	private void stuckHangoutNotification(){
    		
    		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
    		
    		boolean allowedHangout = sharedPrefs.getBoolean("allow_hangout_notification", false);
    		 NotificationManager mNotificationManager =
					 (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    		if(allowedHangout){
    			
    			NotificationCompat.Builder mBuilder =
    				    new NotificationCompat.Builder(this)
    				    .setSmallIcon(R.drawable.ic_launcher)
    				    .setContentTitle("I'm just hangin'")
    				    .setContentText("this app settings -> allow hangin")
    				    .setAutoCancel(false);

    					Intent resultIntent = new Intent(this, MainActivity.class);

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
    					
    					// mId allows you to update the notification later on.
    					  mNotificationManager.notify(12, mBuilder.build());
    		}
    		else{
    		 mNotificationManager.cancel(12);
    		}
    	}
    	
		@Override
 		public void onResume() {
		    super.onResume();
		     updateScreen();
		     stuckHangoutNotification();
		}
		
		private void updateScreen(){
			this.repository.getInitialData();
			
			Calendar c = Calendar.getInstance();
			int mYear = c.get(Calendar.YEAR);
			int mMonth = c.get(Calendar.MONTH);
			int mDay = c.get(Calendar.DAY_OF_MONTH);
			
			PeriodReport smokedCigarettesReport = this.repository.getReportByDay(mYear, mMonth, mDay);
			
			loadSmokingData(mYear, mMonth, mDay, smokedCigarettesReport.getCigarettesCount(), smokedCigarettesReport.getMoneySpend());
			frameAnimation = (AnimationDrawable) img.getBackground();
	        frameAnimation.start();
		}	
		
		private void loadSmokingData(int selectedYear, int selectedMonth, int selectedDay, int cigarettesCount, double spendMoney){
					
				
				TextView dataContainer = (TextView)findViewById(R.id.dailyDataTextView);
			       
				String status = "";
				int minimum = repository.getInitialData().getMinCigarettensPerDay();
				int maximum = repository.getInitialData().getMaxCigarettensPerDay();
				
				if(cigarettesCount == 0){
					status = SmokingStates.NO_SMOKE_STATE;
					img.setBackgroundResource(R.drawable.animation_no_smoking);
					
				}
				else if(cigarettesCount < minimum){
					status = SmokingStates.UNDER_MINIMUM_SMOKE_STATE;
					img.setBackgroundResource(R.drawable.animation_under_minimum_smoking);
					
				}
				else if(cigarettesCount == maximum){
					status = SmokingStates.REACHED_MAXIMUM_SMOKE_STATE;
					img.setBackgroundResource(R.drawable.animation_average_smoking);
				}
				else if(cigarettesCount == minimum){
					status = SmokingStates.REACHED_MINIMUM_SMOKE_STATE;
					img.setBackgroundResource(R.drawable.animation_average_smoking);
				}
				else if(cigarettesCount > maximum){
					status = SmokingStates.ABOVE_LIMIT_SMOKE_STATE;
					playAboveLimitMarch();
					img.setBackgroundResource(R.drawable.animation_above_maximum_smoking);
				}
				else if(cigarettesCount < maximum){
					status = SmokingStates.AVERAGE_SMOKE_STATE;
					img.setBackgroundResource(R.drawable.animation_average_smoking);
				}
				
				currentState = status;
				
				DecimalFormat twoDForm = new DecimalFormat("0.00");
				
				String moneyLimitMsgg = "";
				boolean isLimitReachedThisMonth  = this.repository.isMonthLimitReached();
				if(isLimitReachedThisMonth){
					moneyLimitMsgg = "MONEY LIMIT REACHED"+ System.getProperty("line.separator");
				}
				
				dataContainer.setText(String.format("%s %s-%s-%s ciggarette(s) %s.%s %s.%s Spent money %s",
						moneyLimitMsgg,
						selectedDay,  new DateFormatSymbols().getMonths()[selectedMonth], selectedYear,
						cigarettesCount, System.getProperty("line.separator"),
						status, System.getProperty("line.separator"),twoDForm.format(spendMoney)));
				
			}
	
		@Override
		public void onSaveInstanceState(Bundle savedInstanceState) {
		  super.onSaveInstanceState(savedInstanceState);
		  if(isDialogOpen){
			  savedInstanceState.putBoolean("dialogOpen", true);
		  }
		  
		  if(ringProgressDialog != null){
			  ringProgressDialog.dismiss();
		  }
		}
		
		@Override
		public void onRestoreInstanceState(Bundle savedInstanceState) {
		  super.onRestoreInstanceState(savedInstanceState);
		 
		  boolean dialogOpen = savedInstanceState.getBoolean("dialogOpen", false);
		  if(dialogOpen){
			  reuestAddCigarette();
		  }
		}
	}

