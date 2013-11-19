package com.example.trackyoursmoking;

import java.util.List;

import android.app.Application;

public abstract class BaseRepository implements IRepository {

	protected Application application;
	
	public BaseRepository(Application aplication) {
		this.application = aplication;
	}
	
	@Override
	public abstract boolean isMonthLimitReached();
	@Override
	public abstract SmokingActivity addCigaretteToday();

	@Override
	public abstract List<SmokingActivity> takeCigarettesForGivenDay(int year, int month,
			int day);

	@Override
	public abstract PeriodReport getCigarettesPerDatesPeriod(int yearFrom, int yearTo,
			int monthFrom, int monthTo, int dayFrom, int dayTo);

	@Override
	public abstract PeriodReport getCigarettesPerAndTimePeriod(int yearFrom, int yearTo,
			int monthFrom, int monthTo, int dayFRom, int dayTo, int hourFrom,
			int hourTo, int minutesFrom, int minutesTo,
			List<Integer> excludedDaysOfTheWeek);

	@Override
	public abstract boolean setInitialData(InitialUserData userData);

	@Override
	public abstract InitialUserData getInitialData();

	@Override
	public abstract void removeActivity(int activityId);
	
	@Override
	public abstract PeriodReport getReportByDay(int year, int month, int day );

}
