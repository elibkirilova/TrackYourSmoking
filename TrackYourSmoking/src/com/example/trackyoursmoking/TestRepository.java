package com.example.trackyoursmoking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;

public class TestRepository extends BaseRepository {

	public TestRepository(Application aplication) {
		super(aplication);
		// TODO Auto-generated constructor stub
	}

	private static InitialUserData initialData;
	
	private static List<SmokingActivity> cigarettesSmoked = new ArrayList<SmokingActivity>();

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
		newActivity.setId(cigarettesSmoked.size());
		newActivity.setCigarettePrice(initialData.getPricePerCigarette());
		Calendar cal = Calendar.getInstance();
		newActivity.setDateAndTime(cal.getTime());
		cigarettesSmoked.add(newActivity);
		return newActivity;
	}

	
	public boolean setInitialData(InitialUserData userData) {
		initialData = userData;
		return false;
	}

	public InitialUserData getInitialData() {
		return initialData;
	}

	@Override
	public List<SmokingActivity> takeCigarettesForGivenDay(int year, int month, int day) {
		
		List<SmokingActivity> list = new ArrayList<SmokingActivity>();
		
		for(int i = 0; i < cigarettesSmoked.size(); i++)
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(cigarettesSmoked.get(i).getDateAndTime());
			
			int currentYear = cal.get(Calendar.YEAR);

			int currentMonth = cal.get(Calendar.MONTH);
			
			int currentDay = cal.get(Calendar.DATE);
			
			if(currentYear == year && currentMonth == month && currentDay == day)
			{
				list.add(cigarettesSmoked.get(i));
			}
		}
		Collections.sort(list);
		return list;
	}

	@Override
	public int getCigarettesSmokedTodayCount() {
		List<SmokingActivity> result = this.getCigarettesSmokedToday();
		return result.size();
	}

	@Override
	public void removeActivity(int activityId) {
		
		int activityIndex = -1;
		
		for(int i = 0; i < cigarettesSmoked.size(); i++){
			if(cigarettesSmoked.get(i).getId() == activityId){
				activityIndex = i;
				break;
			}
		}
		
		if(activityIndex != -1){
			cigarettesSmoked.remove(activityIndex);
		}
		else{
			throw new IllegalArgumentException(String.format("Activity with id {0} not found.", activityId));
		}
		
	}

	@Override
	public PeriodReport getCigarettesPerDatesPeriod
		(int yearFrom, int yearTo, int monthFrom, int monthTo, int dayFrom, int dayTo) {
		
		List<SmokingActivity> list = new ArrayList<SmokingActivity>();
		
		if(cigarettesSmoked.size() != 0){
		
			double moneySpend = 0;
			
			Calendar cal = Calendar.getInstance();
			int countOfDays = this.getDaysBetweenDays(yearFrom, yearTo, monthFrom, monthTo, dayFrom, dayTo);
			
			for(int i = 0; i < cigarettesSmoked.size(); i++)
			{
				
				cal.setTime(cigarettesSmoked.get(i).getDateAndTime());
				
				int currentYear = cal.get(Calendar.YEAR);
	
				int currentMonth = cal.get(Calendar.MONTH);
				
				int currentDay = cal.get(Calendar.DATE);
				
				if(currentYear >= yearFrom && currentYear <= yearTo &&
					currentMonth >= monthFrom && currentMonth <= monthTo &&
					currentDay >= dayFrom && currentDay <= dayTo){
					list.add(cigarettesSmoked.get(i));
					moneySpend += cigarettesSmoked.get(i).getCigarettePrice();
				}
			}
			
			if(list.size() != 0){
				
				Map<String, List<SmokingActivity>> map = new HashMap<String, List<SmokingActivity>>();
				for (SmokingActivity activity : list) {
					cal.setTime(activity.getDateAndTime());
					
					int currentYear = cal.get(Calendar.YEAR);
		
					int currentMonth = cal.get(Calendar.MONTH);
					
					int currentDay = cal.get(Calendar.DATE);
					
					String key = String.format("%s/%s/%s",currentYear, currentMonth, currentDay);
					
				   if (map.get(key) == null) {
				      map.put(key, new ArrayList<SmokingActivity>());
				   }
				   map.get(key).add(activity);
				}

				int minCigarettesPerDay = Integer.MAX_VALUE;
				int maxCigarretesPerDay = Integer.MIN_VALUE;
				int countOfCigarretes = list.size();
				
				
				
				double averageCigarettesPerDay = 0;
				
				for(Map.Entry<String, List<SmokingActivity>> dailyActivities : map.entrySet()){
					averageCigarettesPerDay += dailyActivities.getValue().size();
					
					if(minCigarettesPerDay > dailyActivities.getValue().size()){
						minCigarettesPerDay = dailyActivities.getValue().size();
					}
					
					if(maxCigarretesPerDay < dailyActivities.getValue().size()){
						maxCigarretesPerDay = dailyActivities.getValue().size();
					}
				}
				
				if(countOfDays != 0){
					averageCigarettesPerDay /= countOfDays;
				}
				
				PeriodReport report = new PeriodReport();
				report.setMinCigarettensPerDay(minCigarettesPerDay);
				report.setMaxCigarettensPerDay(maxCigarretesPerDay);
				report.setAverrageCigarettesPerDay(averageCigarettesPerDay);
				report.setMoneySpend(moneySpend);
				report.setCigarettesCount(countOfCigarretes);
				
				return report;
			}
			else{
				return null;
			}
		}
		else{
			return null;
		}
		
		
		
	}
	
	@Override
	public PeriodReport getCigarettesPerAndTimePeriod
		(int yearFrom, int yearTo, int monthFrom, int monthTo, int dayFrom, int dayTo,
				int hourFrom, int hourTo, int minutesFrom, int minutesTo, List<Integer> includedDaysOfTheWeek){
		List<SmokingActivity> list = new ArrayList<SmokingActivity>();
		
		if(cigarettesSmoked.size() != 0){
		
			double moneySpend = 0;
			int countOfDays = this.getDaysBetweenDays(yearFrom, yearTo, monthFrom, monthTo, dayFrom, dayTo);
			Calendar cal = Calendar.getInstance();
			for(int i = 0; i < cigarettesSmoked.size(); i++)
			{
				
				cal.setTime(cigarettesSmoked.get(i).getDateAndTime());
				
				int currentYear = cal.get(Calendar.YEAR);
	
				int currentMonth = cal.get(Calendar.MONTH);
				
				int currentDay = cal.get(Calendar.DATE);
				
				int currentHour = cal.get(Calendar.HOUR_OF_DAY);
				
				int currentMinutes = cal.get(Calendar.MINUTE);
				
				Integer currentDayOfTheWeek = cal.get(Calendar.DAY_OF_WEEK);
				
				if(currentYear >= yearFrom && currentYear <= yearTo &&
					currentMonth >= monthFrom && currentMonth <= monthTo &&
					currentDay >= dayFrom && currentDay <= dayTo &&
					currentHour >= hourFrom && currentHour <= hourTo &&
					currentMinutes >= minutesFrom && currentMinutes <= minutesTo &&
					includedDaysOfTheWeek.contains(currentDayOfTheWeek)){
					list.add(cigarettesSmoked.get(i));
					moneySpend += cigarettesSmoked.get(i).getCigarettePrice();
					
					
				}
			}
			
			if(list.size() != 0){
				
				Map<String, List<SmokingActivity>> map = new HashMap<String, List<SmokingActivity>>();
				for (SmokingActivity activity : list) {
					cal.setTime(activity.getDateAndTime());
					
					int currentYear = cal.get(Calendar.YEAR);
		
					int currentMonth = cal.get(Calendar.MONTH);
					
					int currentDay = cal.get(Calendar.DATE);
					
					String key = String.format("%s/%s/%s",currentYear, currentMonth, currentDay);
					
				   if (map.get(key) == null) {
				      map.put(key, new ArrayList<SmokingActivity>());
				   }
				   map.get(key).add(activity);
				}

				int minCigarettesPerDay = Integer.MAX_VALUE;
				int maxCigarretesPerDay = Integer.MIN_VALUE;
				int countOfCigarretes = list.size();
				

				double averageCigarettesPerDay = 0;
				
				for(Map.Entry<String, List<SmokingActivity>> dailyActivities : map.entrySet()){
					averageCigarettesPerDay += dailyActivities.getValue().size();
					countOfDays++;
					if(minCigarettesPerDay > dailyActivities.getValue().size()){
						minCigarettesPerDay = dailyActivities.getValue().size();
					}
					
					if(maxCigarretesPerDay < dailyActivities.getValue().size()){
						maxCigarretesPerDay = dailyActivities.getValue().size();
					}
				}
				
				if(countOfDays != 0){
					averageCigarettesPerDay /= countOfDays;
				}
				
				PeriodReport report = new PeriodReport();
				report.setMinCigarettensPerDay(minCigarettesPerDay);
				report.setMaxCigarettensPerDay(maxCigarretesPerDay);
				report.setAverrageCigarettesPerDay(averageCigarettesPerDay);
				report.setMoneySpend(moneySpend);
				report.setCigarettesCount(countOfCigarretes);
				
				return report;
			}
			else{
				return null;
			}
		}
		else{
			return null;
		}
		
		
		
	}

	private int getDaysBetweenDays(int yearFrom, int yearTo, int monthFrom, int monthTo, int dayFrom, int dayTo){
		
		Calendar cal = Calendar.getInstance();
		cal.set(yearFrom, monthFrom, dayFrom);
		long dateFromMillis = cal.getTimeInMillis();
		cal.set(yearTo, monthTo, dayTo);
		long dateToMillis = cal.getTimeInMillis();
		
		int countOfDays = 0; 
		if(dateFromMillis != dateToMillis){
			countOfDays	= (int)( (dateToMillis - dateFromMillis) / (1000 * 60 * 60 * 24));
		}
		
		return countOfDays;
	}
}
