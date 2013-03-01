package servlets;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import database.ResponseObject;
import database.DatabaseInterface.DatabaseInterfaceException;

import services.AuthenticationService;
import services.SecureService;
import services.ServiceFactory;

/**
 * Servlet implementation class RegisterServlet
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
		
		
		//		AuthenticationService authService = new AuthenticationService();
//		String postData = this.getPostData(request);
//		String result = authService.register(postData);
//		
//		response.setContentType("text/html");
//		
//		Writer writer = response.getWriter();
//		writer.write("<html>");
//		writer.write(result);
//		writer.write("</html>");
	}

}
