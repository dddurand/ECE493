package util;

import java.util.ArrayList;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import dataModels.Account;
import dataModels.Filter;
import dataModels.PersonalStatistics;
import database.DatabaseInterface;
import database.DatabaseInterface.DatabaseInterfaceException;

/**
 * The cron process that updates the ranking caches.
 * 
 * @author dddurand
 *
 */
public class RankCacheCron extends TimerTask {

	private ServletContext context;
	
	public RankCacheCron(ServletContext context)
	{
		this.context = context;
	}
	
	/**
	 * For each account with an expired cache, we update their rank cache.
	 * 
	 */
		@Override
		public void run() {
			
			context.log("Ranking Cache Cron Started...");
					
			DatabaseInterface dbInterface;
			try {
				dbInterface = new DatabaseInterface();
				ArrayList<Account> accounts = dbInterface.getAllAccounts();
				
				for(Account account : accounts)
				{
					if(!dbInterface.isRankCacheCurrent(account))
					{
						Filter filter = new Filter();
						PersonalStatistics stats = new PersonalStatistics(account, dbInterface, filter);
						stats.updateRankings();
					}
				}
				
				dbInterface.close();
				
				
			} catch (DatabaseInterfaceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			context.log("Ranking Cache Cron Complete...");
			
		}
		

}
