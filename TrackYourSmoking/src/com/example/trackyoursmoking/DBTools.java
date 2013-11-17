package com.example.trackyoursmoking;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBTools extends SQLiteOpenHelper {
	
	public DBTools(Context applicationContext){
		
		super(applicationContext, "trackyoursmoking.db", null, 1);
		
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		
		String query = "CREATE TABLE smokingActivities (smokingActivityId INTEGER PRIMARY KEY, activityDateAndTime INTEGER, cigarettePrice DECIMAL(10,2) )";
		
		database.execSQL(query);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		
		String query = "DROP TABLE IF EXISTS smokingActivities";
		
		database.execSQL(query);
		onCreate(database);
		
	}
	
	public SmokingActivity insertActivity(SmokingActivity activity){
		
		SQLiteDatabase database = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("activityDateAndTime", activity.getDateAndTime().getTime());
		values.put("cigarettePrice", activity.getCigarettePrice());
	    long insertId = database.insert("smokingActivities", null,
	        values);
	    Cursor cursor = database.query("smokingActivities",
	       new String[] {"smokingActivityId", "activityDateAndTime", "cigarettePrice"}, "smokingActivityId" + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    
	    SmokingActivity newActivity = cursorToSmokingActivity(cursor);
	    
	    cursor.close();
	    database.close();

		return newActivity;
		
	}
	
	
	public void deleteActivity(int id){
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		String deleteQuery = "DELETE FROM smokingActivities WHERE smokingActivityId='" + id + "'";
		
		database.execSQL(deleteQuery);
		
	}
	
	 private SmokingActivity cursorToSmokingActivity(Cursor cursor) {
			 SmokingActivity activity = new SmokingActivity();
			 activity.setId((int)cursor.getLong(0));
			 activity.setDateAndTime(new Date(cursor.getLong(1)));
			 activity.setCigarettePrice(cursor.getFloat(2));
			 
			 return activity;
		  }
	
	public List<SmokingActivity> getAllActivities(){
		
		SQLiteDatabase database = this.getWritableDatabase();
		List<SmokingActivity> activities = new ArrayList<SmokingActivity>();

	   Cursor cursor = database.query("smokingActivities",
	 	       new String[] {"smokingActivityId", "activityDateAndTime", "cigarettePrice"}, 
	 	      null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	SmokingActivity activity = cursorToSmokingActivity(cursor);
	    	activities.add(activity);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    database.close();
	    return activities;
	}
	
	
}








