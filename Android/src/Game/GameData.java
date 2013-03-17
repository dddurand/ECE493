package game;

/**
 * Data Object that will passed to the View to update the screen.
 * Will be called from game mechanics.
 */
public class GameData {
	
	private int totalPlayers;
//	private String[] names;
	//Array of Players?
	
	public GameData(int totalPlayers){
		this.totalPlayers=totalPlayers;
	}

	/*******Total Players******/
	public int getTotalPlayers() {
		return totalPlayers;
	}
	public void setTotalPlayers(int totalPlayers) {
		this.totalPlayers = totalPlayers;
	}
	
}
