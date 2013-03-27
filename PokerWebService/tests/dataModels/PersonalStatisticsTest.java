package dataModels;

import static org.junit.Assert.*;

import mockObjects.MockDbInterface;

import org.junit.Test;

import dataModels.Filter.RankType;
import dataModels.Filter.TimeFrame;
import dataModels.RankingStatistics.RankedDataRow;
import database.DatabaseInterface.DatabaseInterfaceException;

public class PersonalStatisticsTest {

	@Test
	public void constructor() {
		MockDbInterface db;
		
		try {
			db = new MockDbInterface(false);
			
			Filter filter = new Filter();
			Account account = new Account("bob");
			
			@SuppressWarnings("unused")
			PersonalStatistics stats = new PersonalStatistics(account, db, filter);
			
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
			Account account = new Account("bob");
			
			PersonalStatistics stats = new PersonalStatistics(account, db, filter);
			
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
			assertTrue(stats.getMoneyGenerate() == 5);
			
			assertTrue(stats.getNetMoney() == -25);
			
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
	
	@Test
	public void optimalityTest()
	{
		MockDbInterface db;
		try {
			db = new MockDbInterface(false);
			Filter filter = new Filter();
			Account account = new Account("bob");
			
			PersonalStatistics stats = new PersonalStatistics(account, db, filter);
			
			stats.generateAllStatistics();
			
			stats.updateRankings();
			
			assertTrue(db.getUserNetMoneyRanking(account, filter)==0);
			assertTrue(db.getUserOptimality(account, filter)==-25);
			assertTrue(db.isCacheUpdated());
			
		} catch (Exception e) {
			fail();
		}
		
	}
	
	@Test
	public void getMyRatingTest()
	{
		MockDbInterface db;
		try {
			db = new MockDbInterface(false);
			Filter filter = new Filter();
			filter.setRankType(RankType.NET_MONEY);
			Account account = new Account("bob");
			
			PersonalStatistics stats = new PersonalStatistics(account, db, filter);
			RankedDataRow data = stats.getMyRating();
			
			assertTrue(data != null);
			
			filter = new Filter();
			filter.setRankType(RankType.OPTIMALITY);
			account = new Account("bob");
			
			stats = new PersonalStatistics(account, db, filter);
			data = stats.getMyRating();
			
			assertTrue(data != null);
			
		} catch (Exception e) {
			fail();
		}
		
	}
	
	@Test
	public void getWinPercentageTest()
	{
		MockDbInterface db;
		try {
			db = new MockDbInterface(false);
			Filter filter = new Filter();
			filter.setTimeFrame(TimeFrame.MONTH);
			filter.setRankType(RankType.NET_MONEY);
			Account account = new Account("bob");
			
			PersonalStatistics stats = new PersonalStatistics(account, db, filter);
			assertTrue(stats.getWinPercentage()==0);

			
			
		} catch (Exception e) {
			fail();
		}
		
	}
	
}
