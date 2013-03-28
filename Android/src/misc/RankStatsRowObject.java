package misc;

/**
 * Class that represents a row object in the ranking stats list
 */
public class RankStatsRowObject {
	
	private String pos;
	private String user;
	private String score;
	
	public RankStatsRowObject(String pos, String user, String score){
		this.pos=pos;
		this.user=user;
		this.score=score;
	}

	public String getPos() {
		return pos;
	}

	public String getUser() {
		return user;
	}

	public String getScore() {
		return score;
	}


}
