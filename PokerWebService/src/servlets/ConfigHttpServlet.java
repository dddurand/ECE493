package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import util.ServletConfiguration;

/**
 * An extension to httpServlet that makes the object load its context
 * into ServletConfiguration in order to load the context parameters.
 * 
 * @author dddurand
 *
 */
public class ConfigHttpServlet extends HttpServlet {

	private static final long serialVersionUID = -1581925191750562160L;

	@Override
	/**
	 * On creation, attempt to load context into ServletConfiguration
	 */
	public void init() throws ServletException
     {
		 super.init();
		 ServletConfiguration.loadContext(this.getServletContext());
     }
	
}
