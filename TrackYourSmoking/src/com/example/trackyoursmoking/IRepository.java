package com.example.trackyoursmoking;

import java.util.List;

public interface IRepository {

	abstract int getCigarettesSmokedTodayCount();
	
	abstract List<SmokingActivity> getCigarettesSmokedToday();
	
	abstract SmokingActivity addCigaretteToday();
	
	abstract List<SmokingActivity> takeCigarettesForGivenDay(int year, int month, int day);
	
	abstract PeriodReport getCigarettesPerDatesPeriod
	(int yearFrom, int yearTo, int monthFrom, int monthTo, int dayFrom, int dayTo);
	
	abstract PeriodReport getCigarettesPerAndTimePeriod
	(int yearFrom, int yearTo, int monthFrom, int monthTo, int dayFRom, int dayTo,
			int hourFrom, int hourTo, int minutesFrom, int minutesTo, List<Integer> excludedDaysOfTheWeek);
	
	abstract boolean setInitialData(InitialUserData userData);
	
	abstract InitialUserData getInitialData();
	
	abstract void removeActivity(int activityId);
}
