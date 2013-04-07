package misc;

/**
 * Class that represents a row object in the ranking stats list
 */
public class RankStatsRowObject {
	
	private String pos;
	private String user;
	private String score;
	
	/**
	 * General Constructor
	 * 
	 * @param pos
	 * @param user
	 * @param score
	 */
	public RankStatsRowObject(String pos, String user, String score){
		this.pos=pos;
		this.user=user;
		this.score=score;
	}

	/**
	 * Retrieves position of this rank row
	 * @return
	 */
	public String getPos() {
		return pos;
	}

	/**
	 * Retrives user for this rank row
	 * 
	 * @return
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Retrieves score for rank row
	 * @return
	 */
	public String getScore() {
		return score;
	}


}
