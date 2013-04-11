package serverTest.mock;

import game.GameMechanics;
import server.GameAction;

public class MockGameMechanics extends GameMechanics {

	private boolean processCalled = false;
	private GameAction action;
	
	public MockGameMechanics() {
		super(0, 0, null, null, null);
	}

	@Override
	public void processGameAction(GameAction action) {
		this.action = action;
		this.processCalled = true;
	}

	/**
	 * @return the processCalled
	 */
	public boolean isProcessCalled() {
		return processCalled;
	}

	/**
	 * @param processCalled the processCalled to set
	 */
	public void setProcessCalled(boolean processCalled) {
		this.processCalled = processCalled;
	}

	/**
	 * @return the action
	 */
	public GameAction getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(GameAction action) {
		this.action = action;
	}
	
	

}
