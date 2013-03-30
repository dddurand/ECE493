package util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import dataModels.Account;
import dataModels.Filter;
import dataModels.PersonalStatistics;
import database.DatabaseInterface.DatabaseInterfaceException;
import database.DatabaseOverride;

/**
 * The cron process that updates the ranking caches.
 * 
 * @author dddurand
 *
 */
public class RankCacheCron extends TimerTask {

	private ServletContext context;
	private boolean isTest;
	
	public RankCacheCron(ServletContext context, boolean isTest)
	{
		this.context = context;
		this.isTest = isTest;
	}
	
	/** 
	 * For each account with an expired cache, we update their rank cache.
	 * 
	 */
		@Override
		public void run() {
			
			context.log("Ranking Cache Cron Started...");
					
			DatabaseOverride dbInterface;
			try {
				dbInterface = new DatabaseOverride(!isTest);
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
				context.log("Cron Failed Due to Database Failure...");
			} catch (SQLException e) {
				context.log("Cron Failed Due to Database Failure...");
			} catch (ClassNotFoundException e) {
				context.log("Cron Failed Due to Database Failure...");
			}
			
			context.log("Ranking Cache Cron Complete...");
			
		}
		

}
