package com.example.trackyoursmoking;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DefaultRepository extends BaseRepository  {

	private DBTools database;
	
	public DefaultRepository(Application aplication) {
		super(aplication);
		database = new DBTools(aplication);
	}

	
    public boolean isMonthLimitReached(){
    	
    	Calendar calendar = Calendar.getInstance();

    	InitialUserData data= this.getInitialData();
    	if(data != null){
    		
    		if(data.getMonthMoneyLimit() != null)
    		{
    			PeriodReport report = this.getCigarettesPerDatesPeriod(
        			calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR),
        			calendar.get(Calendar.MONTH), calendar.get(Calendar.MONTH), 
        			1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    			
    			if(data.getMonthMoneyLimit() < report.getMoneySpend()){
    				return true;
    			}
    		}
    	}
    	return false;
    }
	

	@Override
	public List<SmokingActivity> getCigarettesSmokedToday() {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		int currentYear = cal.get(Calendar.YEAR);

		int currentMonth = cal.get(Calendar.MONTH);
		
		int currentDay = cal.get(Calendar.DATE);
		
		return this.takeCigarettesForGivenDay(currentYear, currentMonth, currentDay);
	}

	@Override
	public SmokingActivity addCigaretteToday() {
		SmokingActivity newActivity = new SmokingActivity();
		newActivity.setCigarettePrice(this.getInitialData().getPricePerCigarette());
		Calendar cal = Calendar.getInstance();
		newActivity.setDateAndTime(cal.getTime());
		return this.database.insertActivity(newActivity);
	}

	@Override
	public List<SmokingActivity> takeCigarettesForGivenDay(int year, int month,
			int day) {
		
		return this.database.getAllActivitiesByDay(year,month,
				day);
	}
	
	@Override
	public PeriodReport getCigarettesPerDatesPeriod(int yearFrom, int yearTo,
			int monthFrom, int monthTo, int dayFrom, int dayTo) {
		
		return this.database.getReportByDatesPeriod(yearFrom, yearTo, monthFrom, monthTo, dayFrom, dayTo);
	}

	@Override
	public PeriodReport getReportByDay(int year, int month, int day ) {
		
		return this.database.getReportByDay(year,month, day);
	}
	
	@Override
	public PeriodReport getCigarettesPerAndTimePeriod(int yearFrom, int yearTo,
			int monthFrom, int monthTo, int dayFrom, int dayTo, int hourFrom,
			int hourTo, int minutesFrom, int minutesTo,
			List<Integer> excludedDaysOfTheWeek) {
		
		return this.database.getReportByDatesAndTimePeriod(yearFrom, yearTo, monthFrom, monthTo, dayFrom, dayTo, hourFrom, hourTo, minutesFrom, minutesTo, excludedDaysOfTheWeek);
	}

	@Override
	public boolean setInitialData(InitialUserData userData) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.application);
		SharedPreferences.Editor editor = sharedPrefs.edit();
		
		editor.putBoolean("has_initial_data", true);

		editor.putInt("max_cigarettes_per_day", userData.getMaxCigarettensPerDay());
		editor.putInt("min_cigarettes_per_day", userData.getMinCigarettensPerDay());
		
		if(userData.getMonthMoneyLimit() != null)
        {
                editor.putLong("month_money_limit", Double.doubleToLongBits((Double) userData.getMonthMoneyLimit()));
        }
        editor.putLong("price_per_cigarette", Double.doubleToLongBits((Double) userData.getPricePerCigarette()));
		
		editor.commit();
		
		return true;
	}

	@Override
	public InitialUserData getInitialData() {
		InitialUserData initialData = new InitialUserData();
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.application);
		
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

	@Override
	public void removeActivity(int activityId) {
		this.database.deleteActivity(activityId);
		
	}




	

}
