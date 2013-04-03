package server;

import game.GameMechanics;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Consumer than takes all events, and provides them to the game engine.
 * The resulting state objects should be generated in the game engine and passed
 * to the appropriate user.
 * 
 * @author dddurand
 *
 */
public class PokerEngineTask implements Runnable {

	private final BlockingQueue<GameAction> queue;
	private TimeUnit timeUnit = TimeUnit.SECONDS;
	private long time = 5;
	private boolean canceled = false;
	private GameMechanics gameEngine;

	/**
	 * General Constructor
	 * @param queue The Queue of GameActions that will be waited on for GameActions
	 * @param gameEngine The main engine of the application
	 */
	public PokerEngineTask(BlockingQueue<GameAction> queue, GameMechanics gameEngine)
	{
		this.queue = queue;
		this.gameEngine = gameEngine;
	}

	/**
	 * Waits on GameActions to be present, and then feeds them into the game engine for processing
	 * The queue fails every 5 seconds and restarts.
	 * This allows a cancel variable to be present.
	 */
	@Override
	public void run() {
		
		while(!canceled)
		{
			try {
				GameAction action = queue.poll(time, timeUnit);
				
				if(action == null) continue;
				
				gameEngine.processGameAction(action);

			} catch (InterruptedException e) {
				//When timeout occurs, just restart listening
				//this allows cancels to stop task
				continue;
			}
		}
		
	}
	
	/**
	 * Causes the thread to be canceled.
	 */
	public void cancel()
	{
		this.canceled = true;
	}

}
