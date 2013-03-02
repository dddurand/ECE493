package servlets;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.AuthenticationService;
import services.SecureService;
import services.ServiceFactory;

import com.google.gson.Gson;

import database.DatabaseInterface.DatabaseInterfaceException;
import database.ResponseObject;

/**
 * Servlet implementation class RegisterServlet
 * 
 * This Servlet takes in a username and password, and generates
 * an authentication token. If an invalid username/password pair is given
 * and error is returned.
 * 
 * Input: {"username":"asdf","password":"asdf"}
 * Output: {"Success":"TRUE","Message":"TRUE","AuthenticationToken":"019f27e2-61b5-4046-a9c7-ecbb3c10295f"}
 */
//@WebServlet("/LoginServlet")
@WebServlet("/")
public class LoginServlet extends ConfigHttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * For Testing Purposes Only
	 * 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AuthenticationService authService = new AuthenticationService();
		String message = authService.testDatabaseConnection();
		
		response.setContentType("text/html");
		
		Writer writer = response.getWriter();
		writer.write("<html>");
		writer.write(message);
		writer.write("</html>");
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message;
		ServiceFactory factory = new ServiceFactory();
		
		try {
			String data = this.getPostData(request);
			SecureService service = factory.getService(this);
			message = service.loginSecuredProcess(data);
		} catch (DatabaseInterfaceException e) {
			Gson gson = new Gson();
			ResponseObject responseMsg = new ResponseObject(false, e.getMessage());
			message = gson.toJson(responseMsg, ResponseObject.class);
		}
		
		this.outputMessage(response, message);
	}

}
