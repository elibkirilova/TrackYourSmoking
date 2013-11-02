package com.example.trackyoursmoking;

public class TestRepository implements IRepository {

	private static InitialUserData initialData;
	private int ciggaretesSomokedToday;
	
	public int getCigarettesSmokedToday() {
		return this.ciggaretesSomokedToday;
	}

	public int addCigaretteToday() {
		return this.ciggaretesSomokedToday++;
	}

	
	public boolean setInitialData(InitialUserData userData) {
		initialData = userData;
		return false;
	}

	public InitialUserData getInitialData() {
		return initialData;
	}

}
