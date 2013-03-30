package misc;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import mockObjects.MockServletContext;

import org.junit.Test;

import servlets.CronServlet;
import util.RankCacheCron;
import dataModels.Account;
import database.DatabaseInterface.DatabaseInterfaceException;
import database.DatabaseOverride;

public class CronTest {

	@Test
	public void test() {
		CronServlet servlet = new CronServlet();
		ServletConfig config = new ServletConfig() {
			
			@Override
			public String getServletName() {
				return null;
			}
			
			@Override
			public ServletContext getServletContext() {
				return new MockServletContext();
			}
			
			@Override
			public Enumeration<String> getInitParameterNames() {
				return (new Hashtable<String, String>()).keys();
			}
			
			@Override
			public String getInitParameter(String arg0) {
				return "";
			}
		};
		
		try {
			servlet.init(config);
		} catch (ServletException e) {
			fail();
		}
		
		assertTrue(servlet.isScheduled());
		
		
	}
	
	@Test
	public void test2()
	{
		MockServletContext context = new MockServletContext("home", "true");
		RankCacheCron cron = new RankCacheCron(context, true);
		
		try {
			DatabaseOverride db = new DatabaseOverride(false);
			Account account = new Account(UUID.randomUUID().toString(), "test", false);
			db.addAccount(account);
			
			boolean cache = db.isRankCacheCurrent(account);
			assertTrue(!cache);
			
			cron.run();
			
			cache = db.isRankCacheCurrent(account);
			assertTrue(cache); 
			
			
		} catch (DatabaseInterfaceException e) {
			fail(); 
		} catch (SQLException e) {
			fail(); 
		} catch (ClassNotFoundException e) {
			fail(); 
		}
		
	}
	
	

}
