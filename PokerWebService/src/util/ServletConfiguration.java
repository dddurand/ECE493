package util;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletContext;

/**
 * Provides access to a set of context parameters set within web.xml
 * 
 * The object loads the context parameters based on the first ConfigHttpServlet
 * that is loaded.
 * 
 * @author dddurand
 *
 */
public class ServletConfiguration {

	/*
	 * Parameter Names
	 */
	private static final String PARAM_LOCATION = "location";
	private static final String PARAM_DEBUG = "debug";
	
	/*
	 * Set Primitive Values
	 */
	private static final String TRUE_VALUE = "true";
	//private static final String FALSE_VALUE = "false";
	
	/*
	 * Location Specific Values
	 */
	private static final String LOCATION_HOME = "home";
	private static final String LOCATION_UNI = "uni";
	
	/*
	 * Static table that contains loaded context parameter values
	 */
	private static Hashtable<String, Object> paramTable = null;
	
	
	/**
	 * Attempts to load the context. If the context is already loaded,
	 * it is simply ignored. Note: This method is not thread safe on purpose,
	 * and is handled within loadContextParameters.
	 */
	public static void loadContext(ServletContext context)
	{
		if(paramTable == null)
		{
			loadContextParameters(context);
		}
	}
	
	/**
	 * A sync. method that loads the context parameters into the parameters table.
	 * A check is done to ensure thread safeness.(Returns on case when table already completed).
	 * 
	 * Loads all context parameters for the given context into the table.
	 */
	private static synchronized void loadContextParameters(ServletContext context) {
		
		//We are rechecking this to ensure no threading issues. We do this to
		//make loadContext function much faster(not limited to single call) once the singleton has been loaded, while
		//ensuring that threading is not an issue.
		if(paramTable != null) return;
		paramTable = new Hashtable<String, Object>();
		
		
		Enumeration<String> paramNames = context.getInitParameterNames();
		
		while(paramNames.hasMoreElements())
		{
			String parameter = paramNames.nextElement();
			String value = context.getInitParameter(parameter);
			
			processParameter(parameter, value);

		}	
	}
	
	/**
	 * Processes each parameter based on its parameter name.
	 * 
	 * @param param Parameter Name
	 * @param value Parameter Value
	 */
	private static void processParameter(String param, String value)
	{
		if(param.equals(PARAM_LOCATION))
			processLocation(param, value);
		else if(param.equals(PARAM_DEBUG))
			processDebug(param, value);
	}
	
	/**
	 * Processes a location parameter and loads the appropriate value into the
	 * table, based on the string value provided.
	 * 
	 * If no valid location is provided, a default of home is loaded.
	 * @param param Parameter Name
	 * @param value Parameter String Value
	 */
	private static void processLocation(String param, String value)
	{
		if(value.equals(LOCATION_UNI))
			paramTable.put(param, Location.UNIVERSITY);
		else if(value.equals(LOCATION_HOME))
			paramTable.put(param, Location.HOME);
		else//Redundant, but for readability
			paramTable.put(param, Location.HOME);
	}
	
	/**
	 * Process debug parameter and loads its value into the table.
	 * 
	 * Assumed false, if doesn't exactly match true value.
	 * 
	 * @param param Parameter Name
	 * @param value Parameter String Value
	 */
	private static void processDebug(String param, String value)
	{
		if(value.equals(TRUE_VALUE))
			paramTable.put(param, new Boolean(true));
		else
			paramTable.put(param, new Boolean(false));
	}
	
	/**
	 * Location Enum
	 * @author dddurand
	 *
	 */
	public enum Location {
		HOME, UNIVERSITY
	}
	
	/*
	 *  --------Parameter Getters-------------
	 */
	
	/**
	 * Retrieves the location parameter.
	 * @return Location Value
	 */
	public static Location getLocation()
	{
		return (Location) paramTable.get(PARAM_LOCATION);
	}
	
	/**
	 * Retrieves whether the servlet is set as debug.
	 * @return Location Value
	 */
	public static boolean isDebug()
	{
		return (Boolean) paramTable.get(PARAM_DEBUG);
	}
	
}
