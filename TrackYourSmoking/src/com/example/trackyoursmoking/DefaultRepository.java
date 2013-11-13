package com.example.trackyoursmoking;

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

}
