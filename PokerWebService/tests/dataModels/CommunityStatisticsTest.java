package dataModels;

import static org.junit.Assert.*;

import mockObjects.MockDbInterface;

import org.junit.Test;

import database.DatabaseInterface.DatabaseInterfaceException;

public class CommunityStatisticsTest {

	@Test
	public void constructor() {
		MockDbInterface db;
		
		try {
			db = new MockDbInterface(false);
			
			Filter filter = new Filter();
			@SuppressWarnings("unused")
			CommunityStatistics stats = new CommunityStatistics(db, filter);
			
		} catch (DatabaseInterfaceException e) {
			fail();
		}	
	}
	
	@Test
	public void generateAllStatisticsTest()
	{
		MockDbInterface db;
		try {
			db = new MockDbInterface(false);
			Filter filter = new Filter();
			CommunityStatistics stats = new CommunityStatistics(db, filter);
			
			stats.generateAllStatistics();
			
			assertTrue(stats.getAvgBet() == 20);
			assertTrue(stats.getAvgDollarsBetOnBets() == 20);
			assertTrue(stats.getAvgDollarsBetOnCalls() == 20);
			assertTrue(stats.getAvgDollarsBetOnRaises() == 20);
			assertTrue(stats.getAvgDollarsBetOnReRaises() == 20);
			assertTrue(stats.getAvgDollarsPotOnBets() == 20);
			assertTrue(stats.getAvgDollarsPotOnCalls() == 20);
			assertTrue(stats.getAvgDollarsPotOnChecks() == 20);
			assertTrue(stats.getAvgDollarsPotOnFolds() == 20);
			assertTrue(stats.getAvgDollarsPotOnLoses() == 20);
			assertTrue(stats.getAvgDollarsPotOnRaises() == 20);
			assertTrue(stats.getAvgDollarsPotOnReRaises() == 20);
			assertTrue(stats.getAvgDollarsPotOnWins() == 20);
			
			assertTrue(stats.getGamesPlayed() == 5);
			assertTrue(stats.getMoneyGenerate() == 20);
			
			assertTrue(stats.getNetMoney() == -40);
			
			assertTrue(stats.getTotalDollarsBet() == 20);
			assertTrue(stats.getTotalDollarsBetOnBets() == 20);
			assertTrue(stats.getTotalDollarsBetOnCalls() == 20);
			assertTrue(stats.getTotalDollarsBetOnRaises() == 20);
			assertTrue(stats.getTotalDollarsBetOnReRaises() == 20);
			assertTrue(stats.getTotalDollarsFolded() == 20);
			assertTrue(stats.getTotalDollarsLoss() == 20);
			assertTrue(stats.getTotalDollarsWon() == 20);
			assertTrue(stats.getTotalNumberOfBets() == 60);
			assertTrue(stats.getTotalNumberOfCalls() == 60);
			assertTrue(stats.getTotalNumberOfChecks() == 60);
			assertTrue(stats.getTotalNumberOfFolds() == 60);
			assertTrue(stats.getTotalNumberOfPots() == 180);
			assertTrue(stats.getTotalNumberOfPotsLoss() == 60);
			assertTrue(stats.getTotalNumberOfPotsWon() == 60);
			
			
			
			
		} catch (DatabaseInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
