package dataModels;

/**
 * A data representation of a single ranked statistic.
 * This represents an user, their rank value, and associated rank position,
 * in the context of being in a ranked order based on rankValue.
 * 
 * @SRS 3.2.1.10.2
 * @author dddurand
 *
 */
public class SimpleRankStatistic {

		private String username;
		private Number rankValue;
		private int rankPosition;
		
		/**
		 * General Constructor
		 * 
		 * @param username The username of the user
		 * @param value The value of the ranking statistic
		 * @param rankPosition The position of this user in regards to all other users for this stat
		 */
		public SimpleRankStatistic(String username, Number value, int rankPosition)
		{
			this.username = username;
			this.rankValue = value;
			this.rankPosition = rankPosition;
		}


		/**
		 * @return the username
		 */
		public String getUsername() {
			return username;
		}

		/**
		 * @param username the username to set
		 */
		public void setUsername(String username) {
			this.username = username;
		}

		/**
		 * @return the rankValue
		 */
		public Number getRankValue() {
			return rankValue;
		}

		/**
		 * @param rankValue the rankValue to set
		 */
		public void setRankValue(Number rankValue) {
			this.rankValue = rankValue;
		}

		/**
		 * @return the rankPosition
		 */
		public int getRankPosition() {
			return rankPosition;
		}

		/**
		 * @param rankPosition the rankPosition to set
		 */
		public void setRankPosition(int rankPosition) {
			this.rankPosition = rankPosition;
		}


}
