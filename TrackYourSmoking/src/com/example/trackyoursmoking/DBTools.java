package com.example.trackyoursmoking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBTools extends SQLiteOpenHelper {
	
	private final static int seedEntitiesCount = 50;
	private final static String TABLE_NAME = "smokingActivities";
	private final static String COLUMN_YEAR = "activityYear";
	private final static String COLUMN_MONTH = "activityMonth";
	private final static String COLUMN_DAY = "activityDay";
	private final static String COLUMN_HOUR = "activityHour";
	private final static String COLUMN_MINUTES = "activityMinutes";
	private final static String COLUMN_PRICE = "activityPrice";
	private final static String COLUMN_ID = "activityID";
	private final static String ORDER_BY_DATE = COLUMN_YEAR + " ASC, " + 
	 	      COLUMN_MONTH  + " ASC, "  + 
	 	      COLUMN_DAY  + " ASC, " +
	 	      COLUMN_HOUR  + " ASC, " + 
	 	      COLUMN_MINUTES + " ASC";

	public DBTools(Context applicationContext){
		
		super(applicationContext, "trackyoursmoking.db", null, 24);
		
	}


	@Override
	public void onCreate(SQLiteDatabase database) {
		
		String query = "CREATE TABLE " +
				TABLE_NAME +
				" (" +
				COLUMN_ID +
				" INTEGER PRIMARY KEY, " +
				COLUMN_YEAR +
				" INTEGER, " +
				COLUMN_MONTH +
				" INTEGER, " +
				COLUMN_DAY +
				" INTEGER," +
				COLUMN_HOUR +
				" INTEGER, " +
				COLUMN_MINUTES +
				" INTEGER, " +
				COLUMN_PRICE +
				" DECIMAL(10,2) )";

		database.execSQL(query);
		

		for(int i = 0; i < seedEntitiesCount ; i ++){
			
		       int activitiesForDay = i % 20;
		       while(activitiesForDay > 0){
		    	   Calendar calendar = Calendar.getInstance();
				    calendar.add(Calendar.DATE, -i);
		    	    calendar.add(Calendar.HOUR, -(activitiesForDay % 24));
		    	    calendar.add(Calendar.MINUTE, -(activitiesForDay % 60));
		    	    Date time = calendar.getTime();
		    	    double price = (double)i /100;
		    	    
		    		SmokingActivity activity = new SmokingActivity(price, time);

		    		ContentValues values = activityToContentValues(activity);
		    	    long insertId = database.insert(TABLE_NAME, null,
		    	        values);
		    		activitiesForDay--;
		       }
		
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		
		String query = "DROP TABLE IF EXISTS " +
				TABLE_NAME;
		
		database.execSQL(query);
		onCreate(database);
		
	}
	
	public SmokingActivity insertActivity(SmokingActivity activity){
		
		SQLiteDatabase database = this.getWritableDatabase();

		ContentValues values = activityToContentValues(activity);
	    long insertId = database.insert("smokingActivities", null,
	        values);
	    Cursor cursor = database.query(TABLE_NAME,
	    		new String[] {COLUMN_ID, COLUMN_YEAR, COLUMN_MONTH , COLUMN_DAY, COLUMN_HOUR, COLUMN_MINUTES, COLUMN_PRICE},
	    		COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    
	    SmokingActivity newActivity = cursorToSmokingActivity(cursor);
	    
	    cursor.close();
	    database.close();

		return newActivity;
		
	}
	
	
	public void deleteActivity(int id){
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		String deleteQuery = "DELETE FROM " +
				TABLE_NAME +
				" WHERE " +
				COLUMN_ID +
				"=' " + 
				id + "'";
		
		database.execSQL(deleteQuery);
		
	}
	
	
	public List<SmokingActivity> getAllActivities(String where){
		
		SQLiteDatabase database = this.getWritableDatabase();
		List<SmokingActivity> activities = new ArrayList<SmokingActivity>();

		Cursor cursor = database.query(TABLE_NAME,
		 	       new String[]
		 	    		   {COLUMN_ID,  COLUMN_YEAR, COLUMN_MONTH , COLUMN_DAY, COLUMN_HOUR, COLUMN_MINUTES, COLUMN_PRICE}, 
		 	      where, null, null, null, ORDER_BY_DATE);

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
	
	
		public PeriodReport getReport(String where){
		
		SQLiteDatabase database = this.getWritableDatabase();
		List<SmokingActivity> activities = new ArrayList<SmokingActivity>();

	   Cursor cursor = database.query(TABLE_NAME,
	 	       new String[] 
	 	    		   {COLUMN_ID,  COLUMN_YEAR, COLUMN_MONTH , COLUMN_DAY, COLUMN_HOUR, COLUMN_MINUTES, COLUMN_PRICE}, 
	 	      where, null, null, null, ORDER_BY_DATE);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	SmokingActivity activity = cursorToSmokingActivity(cursor);
	    	activities.add(activity);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    database.close();
	    return null;
	}
	
		
		
	public List<SmokingActivity> getAllActivitiesByDay(int year, int month,
			int day){
		
		
		String WHERE = String.format(" %s = %s and %s = %s and %s = %s ",COLUMN_YEAR,  year, COLUMN_MONTH, month, COLUMN_DAY, day);
		
		//String WHERE = String.format(" (%s BETWEEN %s and %s) and (%s BETWEEN %s and %s) and (%s BETWEEN %s and %s)", currentDate,dayTomorrowMinusMinute );
		return this.getAllActivities(WHERE);
		
		
	}
	
	
	private ContentValues activityToContentValues(SmokingActivity activity){
		
		ContentValues values = new ContentValues();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(activity.getDateAndTime());

		values.put(COLUMN_YEAR, calendar.get(Calendar.YEAR));
		values.put(COLUMN_MONTH, calendar.get(Calendar.MONTH));
		values.put(COLUMN_DAY, calendar.get(Calendar.DATE));
		values.put(COLUMN_HOUR, calendar.get(Calendar.HOUR_OF_DAY));
		values.put(COLUMN_MINUTES, calendar.get(Calendar.MINUTE));
		values.put(COLUMN_PRICE, activity.getCigarettePrice());
		
		return values;
		
	}
	
	
	
	 private SmokingActivity cursorToSmokingActivity(Cursor cursor) {
		 SmokingActivity activity = new SmokingActivity();
		 activity.setId((int)cursor.getLong(0));
		 Calendar calendar = Calendar.getInstance();
		 calendar.set((int)cursor.getLong(1), (int)cursor.getLong(2), (int)cursor.getLong(3), (int)cursor.getLong(4), (int)cursor.getLong(5));
		 activity.setDateAndTime(calendar.getTime());
		 activity.setCigarettePrice(cursor.getFloat(6));
		 
		 return activity;
	  }

}








