package com.example.trackyoursmoking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TestRepository implements IRepository {

	private static InitialUserData initialData = new InitialUserData();
	
	
	
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
		newActivity.setDateAndTime(new Date());
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

}
