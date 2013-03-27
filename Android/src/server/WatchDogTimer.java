package server;

import game.GameMechanics;
import game.Player;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

import server.GameAction.PokerAction;

public class WatchDogTimer {

	private Timer timer;
	private BlockingQueue<GameAction> queue;
	private int timeout;
	
	public WatchDogTimer(BlockingQueue<GameAction> queue, int secondsTimeout)
	{
		timer = new Timer();
		this.queue = queue;
		this.timeout = secondsTimeout;
	}
		
	public void cancel()
	{
		this.timer.cancel();
	}
	
	public void startTimer()
	{
		timer = new Timer();
		timer.schedule(new TimeOutTask(queue), this.timeout*1000);
	}
	
	private class TimeOutTask extends TimerTask
	{
		private BlockingQueue<GameAction> queue;
		
		public TimeOutTask(BlockingQueue<GameAction> queue)
		{
			this.queue = queue;
		}
		
		@Override
		public void run() {

			GameAction action = new GameAction(GameMechanics.SERVER_POSITION, PokerAction.TIMEOUT);
			queue.add(action);
			
		}
		
	}
	
	
}
