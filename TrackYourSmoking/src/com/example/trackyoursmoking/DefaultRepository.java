package com.example.trackyoursmoking;

import java.util.Date;
import java.util.List;

public class DefaultRepository implements IRepository {

	
	

	public boolean setInitialData(InitialUserData userData) {
		// TODO Auto-generated method stub
		return false;
	}

	public InitialUserData getInitialData() {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public int getCigarettesSmokedTodayCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<SmokingActivity> takeCigarettesForGivenDay(int year, int month,
			int day) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SmokingActivity> getCigarettesSmokedToday() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SmokingActivity addCigaretteToday() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeActivity(int activityId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PeriodReport getCigarettesPerDatesPeriod(int yearFrom, int yearTo,
			int monthFrom, int monthTo, int dayFrom, int dayTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PeriodReport getCigarettesPerAndTimePeriod(int yearFrom, int yearTo,
			int monthFrom, int monthTo, int dayFRom, int dayTo, int hourFrom,
			int hourTo, int minutesFrom, int minutesTo,
			List<Integer> includedDaysOfTheWeek) {
		// TODO Auto-generated method stub
		return null;
	}

}
