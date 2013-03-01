package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import database.ResponseObject;
import database.DatabaseInterface.DatabaseInterfaceException;

import services.SecureService;
import services.ServiceFactory;
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
	
	/**
	 * Retrieves the data provided in a post request.
	 * This function should only be called for getPost requests.
	 * 
	 * @param request getPost Request
	 * @return Data provided in a Post HttpServletRequest.
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	protected String getPostData(HttpServletRequest request) 
										throws ServletException, IOException 
	{

		BufferedReader reader = request.getReader();
		
		StringBuilder builder = new StringBuilder();
		char[] charBuffer = new char[500];
		while(true)
		{
			int charsRead = reader.read(charBuffer);
			if(charsRead < 0) break;
			builder.append(charBuffer, 0, charsRead);
			
		}
		
		return builder.toString();
	}
	
	protected void outputMessage(HttpServletResponse response, String message) throws IOException
	{
		
		Writer writer = response.getWriter();
		response.setContentType("text/html");
		writer.write(message);
	}
	
}
