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

import database.DatabaseInterface;
import database.ResponseObject;
import database.DatabaseInterface.DatabaseInterfaceException;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends ConfigHttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message;
		ServiceFactory factory = new ServiceFactory();
		
		try {
			DatabaseInterface dbInterface = new DatabaseInterface();
			String data = this.getPostData(request);
			SecureService service = factory.getService(this, dbInterface);
			message = service.authSecuredProcess(data);
			service.close();
		} catch (DatabaseInterfaceException e) {
			Gson gson = new Gson();
			ResponseObject responseMsg = new ResponseObject(false, e.getMessage(), e.getCode());
			message = gson.toJson(responseMsg, ResponseObject.class);
		}
		
		this.outputMessage(response, message);	
	}

}
