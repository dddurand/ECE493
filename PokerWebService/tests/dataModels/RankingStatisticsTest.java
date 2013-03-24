package dataModels;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import dataModels.RankingStatistics.RankedDataRow;

public class RankingStatisticsTest {

	@Test
	public void emptyConstructorTest() {
		@SuppressWarnings("unused")
		RankingStatistics stats = new RankingStatistics();
		assertTrue(true);
	}
	
	@Test
	public void constructorTest() {
		ArrayList<RankedDataRow> ranked_data = new ArrayList<RankedDataRow>();
		ranked_data.add(new RankedDataRow());
		
		RankedDataRow rankedRowData = new RankedDataRow();
		
		RankingStatistics stats = new RankingStatistics(ranked_data, rankedRowData);
		
		RankedDataRow rankedRowData1 = stats.getMyRankData();
		ArrayList<RankedDataRow> ranked_data2 = stats.getRankedData();
		
		
		assertTrue(rankedRowData1.equals(rankedRowData));
		assertTrue(ranked_data2.equals(ranked_data));
	}
	
	@Test
	public void setMyRankDataTest() {
		ArrayList<RankedDataRow> ranked_data = new ArrayList<RankedDataRow>();
		ranked_data.add(new RankedDataRow());
		
		RankedDataRow rankedRowData = new RankedDataRow();
		
		RankingStatistics stats = new RankingStatistics(ranked_data, rankedRowData);
		
		RankedDataRow rankedRowData2 = new RankedDataRow(1,"asd",123);
		stats.setMyRankData(rankedRowData2);
		

		assertTrue(stats.getMyRankData().equals(rankedRowData2));
	}
	
	@Test
	public void rankedDataRowTest() {
		@SuppressWarnings("unused")
		RankedDataRow rankedDataRow = new RankedDataRow();
		assertTrue(true);
	}
	
	@Test
	public void rankedDataRowConstructorTest() {
		@SuppressWarnings("unused")
		RankedDataRow rankedDataRow = new RankedDataRow(1,"asdf",1.345);
		assertTrue(true);
	}

}
