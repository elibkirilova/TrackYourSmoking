package com.example.trackyoursmoking;

import java.util.List;

public interface IRepository {

	int getCigarettesSmokedTodayCount();
	
	List<SmokingActivity> getCigarettesSmokedToday();
	
	SmokingActivity addCigaretteToday();
	
	List<SmokingActivity> takeCigarettesForGivenDay(int year, int month, int day);
	
	boolean setInitialData(InitialUserData userData);
	
	InitialUserData getInitialData();
	
	void removeActivity(int activityId);
}
