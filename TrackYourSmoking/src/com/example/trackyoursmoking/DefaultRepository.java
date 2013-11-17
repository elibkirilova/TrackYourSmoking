package com.example.trackyoursmoking;

import java.util.Calendar;
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

	@Override
	public int getCigarettesSmokedTodayCount() {
		// TODO Auto-generated method stub
		return this.database.getAllActivities().size();
	}

	@Override
	public List<SmokingActivity> getCigarettesSmokedToday() {
		// TODO Auto-generated method stub
		return this.database.getAllActivities();
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
		// TODO Auto-generated method stub
		return this.database.getAllActivities();
	}

	@Override
	public PeriodReport getCigarettesPerDatesPeriod(int yearFrom, int yearTo,
			int monthFrom, int monthTo, int dayFrom, int dayTo) {
		// TODO Auto-generated method stub
		return new PeriodReport();
	}

	@Override
	public PeriodReport getCigarettesPerAndTimePeriod(int yearFrom, int yearTo,
			int monthFrom, int monthTo, int dayFRom, int dayTo, int hourFrom,
			int hourTo, int minutesFrom, int minutesTo,
			List<Integer> excludedDaysOfTheWeek) {
		// TODO Auto-generated method stub
		return new PeriodReport();
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
