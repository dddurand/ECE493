package servlets;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import util.RankCacheCron;

/**
 * Servlet for starting cron jobs.
 * 
 * @author dddurand
 *
 */
@WebServlet("/cron")
public class CronServlet extends ConfigHttpServlet {

	private static final long serialVersionUID = -7672762555376487388L;
	
	private boolean isScheduled = false;
	
	/**
	 * Run once per day
	 */
	final long dayInMili = 86400000;
	
	/**
	 * Timer for controlling rank cron jobs
	 */
	private final Timer rankCronTimer = new Timer();
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		RankCacheCron rankCron = new RankCacheCron(this.getServletContext(), false);
		rankCronTimer.schedule(rankCron, this.getTonightAtMidnight(), dayInMili);
		isScheduled = true;
	}
	
	public boolean isScheduled()
	{
		return isScheduled;
	}
	
	/**
	 * Returns a date object for 5 minutes after midnight
	 * 
	 * @return
	 */
	private Date getTonightAtMidnight()
	{
		Calendar calender = new GregorianCalendar();	
		calender.add(Calendar.DAY_OF_MONTH, 1);
		
		calender.set(Calendar.HOUR_OF_DAY, 0);
		calender.set(Calendar.MINUTE, 5);
		calender.set(Calendar.SECOND, 0);
		calender.set(Calendar.MILLISECOND, 0);
		return calender.getTime();
	}
	
}
