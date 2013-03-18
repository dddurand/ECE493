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

	
	public PokerEngineTask(BlockingQueue<GameAction> queue, GameMechanics gameEngine)
	{
		this.queue = queue;
		this.gameEngine = gameEngine;
	}

	@Override
	public void run() {
		
		while(!canceled)
		{
			try {
				GameAction action = queue.poll(time, timeUnit);
				
				//Give GameEngine the action object
				//For new players, remove players, game actions, start game, end game etc....
				
				//Assuming the game engine will contain the correct queue for the player
				
			} catch (InterruptedException e) {
				//When timeout occurs, just restart listening
				//this allows cancels to stop task
				continue;
			}
		}
		
	}
	
	public void cancel()
	{
		this.canceled = true;
	}

}
