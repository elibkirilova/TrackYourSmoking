package com.example.trackyoursmoking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBTools extends SQLiteOpenHelper {
	
	private final static int seedEntitiesCount = 50;
	private final static String TABLE_NAME = "smokingActivities";
	private final static String COLUMN_DATE = "activityDate";
	private final static String COLUMN_TIME = "activityTime";
	private final static String COLUMN_PRICE = "activityPrice";
	private final static String COLUMN_DAY_OF_THE_WEEK = "activityDAyOfTheWeek";
	private final static String COLUMN_ID = "activityID";
	private final static String ORDER_BY_DATE = COLUMN_DATE + " ASC, " + 
			COLUMN_TIME  + " ASC ";

	public DBTools(Context applicationContext){
		
		super(applicationContext, "trackyoursmoking.db", null, 33);
		
	}

	//NSString* select = [NSString stringWithFormat:@"select * from orders where isPaid = 1 and strftime('%%Y-%%m',dateOrder) = '%@'",dateString];
	
	

	@Override
	public void onCreate(SQLiteDatabase database) {
		
		String query = "CREATE TABLE " +
				TABLE_NAME +
				" (" +
				COLUMN_ID +
				" INTEGER PRIMARY KEY, " +
				COLUMN_DATE +
				" Text, " +
				COLUMN_TIME +
				" Text, " +
				COLUMN_PRICE +
				" DECIMAL(10,2), " +
				COLUMN_DAY_OF_THE_WEEK +
				" INTEGER " +
				" )";

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
	    		new String[] {COLUMN_ID, COLUMN_DATE, COLUMN_TIME , COLUMN_PRICE},
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
		 	    		   {COLUMN_ID,  COLUMN_DATE, COLUMN_TIME , COLUMN_PRICE}, 
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
	   
	   // List<SmokingActivity> some = this.getAllActivities(null);
	    return activities;
	}
	
	
	public PeriodReport getReport(String where){
		
		SQLiteDatabase database = this.getWritableDatabase();

		PeriodReport report = new PeriodReport();
		
		Cursor cursor  = database.rawQuery(
				"select min(countOfActivitiesPerDay), max(countOfActivitiesPerDay), avg(countOfActivitiesPerDay) from " +
						"( " +
						"select count(*) as countOfActivitiesPerDay " +
						" from smokingActivities " +
						" where " +
						where +
						" group by " +
						COLUMN_DATE +
						" )", null);
	   
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			report.setMinCigarettensPerDay(cursor.getInt(0));
			report.setMaxCigarettensPerDay(cursor.getInt(1));
			report.setAverrageCigarettesPerDay(cursor.getFloat(2));
	    }
		else{
			
			cursor.close();
		    database.close();
		    return null;
		}
	    // make sure to close the cursor
	    cursor.close();
	    
	    Cursor cursorAll  = database.rawQuery(
				"select count(*), sum(activityPrice) from smokingActivities " +
				" where " +
				where, null);
	    cursorAll.moveToFirst();
	    report.setCigarettesCount(cursorAll.getInt(0));
	    report.setMoneySpend(cursorAll.getFloat(1));
	    // make sure to close the cursor
	    cursorAll.close();
	    database.close();
	    
	  // List<SmokingActivity> some = this.getAllActivities(null);
	    
	   
	    
	    return report;
	}
	
		
	public List<SmokingActivity> getAllActivitiesByDay(int year, int month,
			int day){
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		
		String millisecondDate = this.dateToString(calendar.getTime());
		String WHERE = String.format(" %s = '%s' ", COLUMN_DATE, millisecondDate);
		
		
		return this.getAllActivities(WHERE);
		
		
	}
	
	public PeriodReport getReportByDatesPeriod(int yearFrom, int yearTo,
			int monthFrom, int monthTo, int dayFrom, int dayTo){
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(yearFrom, monthFrom, dayFrom);
		
		String millisecondDateFrom = this.dateToString(calendar.getTime());
		
		
		calendar.set(yearTo, monthTo, dayTo);
		
		String millisecondDateTo = this.dateToString(calendar.getTime());
		
		
	    String WHERE = String.format(
	    		" (%s BETWEEN Datetime('%s') and Datetime('%s')) ",
	    		
	    		COLUMN_DATE, millisecondDateFrom, millisecondDateTo);
		return this.getReport(WHERE);

	}
	
	public PeriodReport getReportByDay(int year, int month,
			int day){
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		
		String millisecondDate = this.dateToString(calendar.getTime());
		

		String WHERE = String.format(" %s = '%s' ", COLUMN_DATE, millisecondDate);
		return this.getReport(WHERE);

	}
	
	
	public PeriodReport getReportByDatesAndTimePeriod(int yearFrom, int yearTo,
			int monthFrom, int monthTo, int dayFrom, int dayTo, int hourFrom,
			int hourTo, int minutesFrom, int minutesTo,
			List<Integer> excludedDaysOfTheWeek){
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(yearFrom, monthFrom, dayFrom, hourFrom, minutesFrom);
		
		String millisecondDateFrom = this.dateToString(calendar.getTime());
		String millisecondTimeFrom = this.timeToString(calendar.getTime());
		
		calendar.set(yearTo, monthTo, dayTo, hourTo, minutesTo);
		
		String millisecondDateTo = this.dateToString(calendar.getTime());
		String millisecondTimeTo = this.timeToString(calendar.getTime());
		
	    String WHERE = 
	    		" (" +
	    		COLUMN_DATE +
	    		" BETWEEN Datetime('" +
	    		millisecondDateFrom +
	    		"') and Datetime('" +
	    		millisecondDateTo +
	    		"')) " +
	    		"and (" +
	    		COLUMN_TIME+
	    		" BETWEEN Datetime('" +
	    		millisecondTimeFrom +
	    		"') and Datetime('" +
	    		millisecondTimeTo +
	    		"')) ";

	    if(excludedDaysOfTheWeek.size() > 0){
	    	WHERE += " and " + COLUMN_DAY_OF_THE_WEEK + " NOT IN ( ";
	    	  StringBuilder s = new StringBuilder();
	    	  
	    	  Iterator<Integer> iterator = excludedDaysOfTheWeek.iterator();
	    	  
	          while (iterator.hasNext()) {
	              s.append(iterator.next());

	              if (iterator.hasNext()) {
	                  s.append(", ");
	              }
	          }
	          WHERE += s.toString() + " )";
	    }
	    
	  
	   
	    
		return this.getReport(WHERE);

	}
	
	private ContentValues activityToContentValues(SmokingActivity activity){
		
		ContentValues values = new ContentValues();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(activity.getDateAndTime());

		values.put(COLUMN_DATE, this.dateToString(activity.getDateAndTime()));
		values.put(COLUMN_TIME, this.timeToString(activity.getDateAndTime()));
		values.put(COLUMN_PRICE, activity.getCigarettePrice());
		values.put(COLUMN_DAY_OF_THE_WEEK, calendar.get(Calendar.DAY_OF_WEEK));
		
		return values;
	}
	
	
	
	 private SmokingActivity cursorToSmokingActivity(Cursor cursor) {
		 SmokingActivity activity = new SmokingActivity();
		 activity.setId((int)cursor.getLong(0));
		 try {
			activity.setDateAndTime(this.MillisecondsToDate(cursor.getString(1), cursor.getString(2)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 activity.setCigarettePrice(cursor.getFloat(3));
		 return activity;
	  }

	 private String dateToString(Date date){
		 SimpleDateFormat iso8601Format = new SimpleDateFormat(
		            "yyyy-MM-dd HH:mm:ss");
		 
		 String stringDate = iso8601Format.format(date);
		 String[] splitedDate = stringDate.split("\\s+");
		 
		 return(splitedDate[0] + " 00:00:00");
	 }
	 
	 private String timeToString(Date date){
		
		 
		 SimpleDateFormat iso8601Format = new SimpleDateFormat(
		            "yyyy-MM-dd HH:mm:ss");
		 
		 String stringDate = iso8601Format.format(date);
		 String[] splitedDate = stringDate.split("\\s+");
		 
		 return("2000-01-01 " +splitedDate[1]);
	 }
	 
	 private Date MillisecondsToDate(String date, String time) throws ParseException{
		 SimpleDateFormat iso8601Format = new SimpleDateFormat(
		            "yyyy-MM-dd HH:mm:ss");
		 

		 String[] splitedDate = date.split("\\s+");
		 String[] splitedTime = time.split("\\s+");
		 
		 try {
			return(iso8601Format.parse(splitedDate[0] + " "+ splitedTime[1]));
		} catch (ParseException e) {
			throw e;
		}
	 }
}








