package dataModels;

import java.util.ArrayList;

public class RankingStatistics {
		
		private ArrayList<RankedDataRow> rankedData;
		private RankedDataRow myRankData;

		/**
		 * General Constructor
		 */
		public RankingStatistics() { }
			
		/**
		 * General Constructor
		 */
		public RankingStatistics(ArrayList<RankedDataRow> ranked_data, RankedDataRow myRankData)
		{
			this.rankedData = ranked_data;
			this.myRankData = myRankData;
		}
		
		
		/**
		 * A standalone Rank Data. In our case it will be used for the calling user.
		 */
		public RankedDataRow getMyRankData() {
			return myRankData;
		}

		/**
		 * Updates the Rank Data for the current user.
		 * 
		 * @param myPosition
		 */
		public void setMyRankData(RankedDataRow myRankData) {
			this.myRankData = myRankData;
		}

		/**
		 * Returns the Ranked List of Users
		 * 
		 * @return
		 */
		public ArrayList<RankedDataRow> getRankedData() {
			return rankedData;
		}

		/**
		 * Data Class representing a username, their rank position, and their rank value
		 * 
		 * @author dddurand
		 *
		 */
		public static class RankedDataRow
		{
			@SuppressWarnings("unused")
			private int position;
			@SuppressWarnings("unused")
			private String username;
			@SuppressWarnings("unused")
			private double rankValue;
			
			/**
			 * General Constructor
			 */
			public RankedDataRow() {}
			
			/**
			 * General Constructor
			 * 
			 * @param position The position of the RankedDataRow
			 * @param username The username of the account
			 * @param rankValue The value of the rank type for the current account
			 */
			public RankedDataRow(int position, String username, double rankValue)
			{
				this.position = position;
				this.username = username;
				this.rankValue = rankValue;
			}
			
		}

}
