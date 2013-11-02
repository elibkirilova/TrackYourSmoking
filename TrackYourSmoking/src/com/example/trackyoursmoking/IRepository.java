package com.example.trackyoursmoking;

public interface IRepository {

	int getCigarettesSmokedToday();
	
	int addCigaretteToday();
	
	boolean setInitialData(InitialUserData userData);
	
	InitialUserData getInitialData();
}
