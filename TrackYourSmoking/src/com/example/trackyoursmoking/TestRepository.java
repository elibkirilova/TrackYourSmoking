package com.example.trackyoursmoking;

public class TestRepository implements IRepository {

	private static InitialUserData initialData;
	private static int ciggaretesSomokedToday;
	
	public int getCigarettesSmokedToday() {
		return ciggaretesSomokedToday;
	}

	public int addCigaretteToday() {
		return ciggaretesSomokedToday++;
	}

	
	public boolean setInitialData(InitialUserData userData) {
		initialData = userData;
		return false;
	}

	public InitialUserData getInitialData() {
		return initialData;
	}

}
