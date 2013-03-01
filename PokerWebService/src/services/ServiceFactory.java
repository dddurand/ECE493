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

public class ServiceFactory {

	
	public ServiceFactory() { }
	
	public enum ServletType
	{
		LOGIN, LOGOUT, REGISTER, UNKNOWN
	}
	
	public SecureService getService(ConfigHttpServlet servlet) throws DatabaseInterfaceException
	{
		ServletType servletType = getServletType(servlet);
		Gson gson = generateGSON(servletType);
		DatabaseInterface dbInterface = new DatabaseInterface();
		
		ServiceDelegate delegate = generateServiceDelegate(servletType,gson, dbInterface);
		
		return new SecureService(delegate, gson, dbInterface);
	}
	
	
	
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
	 * @TODO FIND BETTER WAY TO DO THIS
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
