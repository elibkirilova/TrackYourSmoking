package com.example.trackyoursmoking;

import java.util.Date;
import java.util.List;

public interface IRepository {

	int getCigarettesSmokedTodayCount();
	
	List<SmokingActivity> getCigarettesSmokedToday();
	
	SmokingActivity addCigaretteToday();
	
	List<SmokingActivity> takeCigarettesForGivenDay(int year, int month, int day);
	
	PeriodReport getCigarettesPerDatesPeriod
	(int yearFrom, int yearTo, int monthFrom, int monthTo, int dayFrom, int dayTo);
	
	PeriodReport getCigarettesPerAndTimePeriod
	(int yearFrom, int yearTo, int monthFrom, int monthTo, int dayFRom, int dayTo,
			int hourFrom, int hourTo, int minutesFrom, int minutesTo, List<Integer> excludedDaysOfTheWeek);
	
	boolean setInitialData(InitialUserData userData);
	
	InitialUserData getInitialData();
	
	void removeActivity(int activityId);
}
