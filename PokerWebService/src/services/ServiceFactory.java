package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import database.DatabaseInterface;
import database.ResponseObject;
import database.DatabaseInterface.DatabaseInterfaceException;

import servlets.ConfigHttpServlet;
import servlets.LoginServlet;
import servlets.LogoutServlet;
import servlets.RegisterServlet;

/**
 * A class that acts as a factory of SecureService objects. The factory builds
 * the service object absed on the servlet that has been provided to the factory.
 * 
 * @author dddurand
 *
 */
public class ServiceFactory {

	/**
	 * Generic Constructor
	 */
	public ServiceFactory() { }
	
	/**
	 * The various supported servlets that will generate
	 * a specialized SecureService
	 * 
	 * @author dddurand
	 *
	 */
	public enum ServletType
	{
		LOGIN, LOGOUT, REGISTER, UNKNOWN
	}
	
	/**
	 * Builds and retrieves the correct SecureService for a specific Servlet.
	 * 
	 * @param servlet The servlet the service if for.
	 * @return The specifically built SecureService.
	 * @throws DatabaseInterfaceException
	 */
	public SecureService getService(ConfigHttpServlet servlet) throws DatabaseInterfaceException
	{
		ServletType servletType = getServletType(servlet);
		Gson gson = generateGSON(servletType);
		DatabaseInterface dbInterface = new DatabaseInterface();
		
		ServiceDelegate delegate = generateServiceDelegate(servletType,gson, dbInterface);
		
		return new SecureService(delegate, gson, dbInterface);
	}
	
	
	/**
	 * Generates the appropriate delegate object, based on which Servlet the service is for.
	 * 
	 * @param servletType The type of servlet the delegate is for.
	 * @param gson The gson object to be used in the delegate.
	 * @param dbInterface The database interface to be used in the delegate.
	 * @return
	 */
	private ServiceDelegate generateServiceDelegate(ServletType servletType, Gson gson, DatabaseInterface dbInterface)
	{

		switch(servletType)
		{	
		case LOGIN:
			return new LoginDelegate(gson, dbInterface);
		case LOGOUT:
			return new LogoutDelegate(gson, dbInterface);
		case REGISTER:
			return new RegisterDelegate(gson, dbInterface);
		default:
			return new ServiceDelegate(gson, dbInterface);
		}
	}
	
	/**
	 * Builds the gson object, that will be used by the service.
	 * Custom additions are added to the gson object as needed
	 * by the specific servlet types.
	 * 
	 * @param servletType The type of servlet the gson will be used in.
	 * @return The built gson object.
	 */
	private Gson generateGSON(ServletType servletType)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		
		switch(servletType)
		{	
		case LOGIN:
		case LOGOUT:
		case REGISTER:
			gsonBuilder.registerTypeAdapter(ResponseObject.class, new ResponseObject.ResponseSerializer());
			break;
		default:
			break;
		}
		
		return gsonBuilder.create();
	}
	
	/**
	 * Determines what type of servlet has been provided
	 * to the factory.
	 * 
	 */
	private ServletType getServletType(ConfigHttpServlet servlet)
	{
		if(servlet instanceof LoginServlet)
			return ServletType.LOGIN;
		else if(servlet instanceof LogoutServlet)
			return ServletType.LOGOUT;
		else if(servlet instanceof RegisterServlet)
			return ServletType.REGISTER;
		else
			return ServletType.UNKNOWN;
	}
}
