package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.SecureService;
import services.ServiceFactory;

import com.google.gson.Gson;

import database.DatabaseInterface.DatabaseInterfaceException;
import database.ResponseObject;

/**
 * Servlet implementation class RegisterServlet
 * 
 * This Servlet takes in a username and password, and generates
 * a new account. If the username already exists, or cannot be created
 * an error is returned.
 * 
 * Input: {"username":"asdf","password":"asdf"}
 * Output: {"Success":"TRUE","Message":"TRUE","AuthenticationToken":"019f27e2-61b5-4046-a9c7-ecbb3c10295f"}
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends ConfigHttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
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
			message = service.unsecuredProcess(data);
		} catch (DatabaseInterfaceException e) {
			Gson gson = new Gson();
			ResponseObject responseMsg = new ResponseObject(false, e.getMessage());
			message = gson.toJson(responseMsg, ResponseObject.class);
		}
		
		this.outputMessage(response, message);
	}

}
