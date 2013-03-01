package database;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ResponseObject {

	public final static String SUCCESS = "TRUE";
	public final static String FAILURE = "FALSE";
	
	private boolean success;
	private String message;
	
	private String authenticationToken;
	
	/**
	 * Generic Constructor
	 * For GSON
	 */
	public ResponseObject() { }
	
	/**
	 * Standard Constructor
	 * 
	 * @param success Represents if the operation was successful or not.
	 * @param message A message detailing errors, or general messages.
	 */
	public ResponseObject(boolean success, String message)
	{
		this.success = success;
		this.message = message;
	}
	
	
	/**'
	 * Returns success parameter
	 * @return
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * Sets success parameter
	 * @param success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * Returns message parameter
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets message parameter
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Get Auth token parameter
	 * 
	 * @return
	 */
	public String getAuthenticationToken() {
		return authenticationToken;
	}

	/**
	 * Set auth parameter
	 * 
	 * @param authenticationToken
	 */
	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}


	
	

	/**
	 * GSON Serializer for the ResponseObject
	 * @author dddurand
	 *
	 */
	public static class ResponseSerializer implements JsonSerializer<ResponseObject> {

		/**
		 * Takes a response object and seralizes it into a JsonElement
		 */
		@Override
		public JsonElement serialize(ResponseObject response, Type type,
				JsonSerializationContext context) 
		{
			JsonObject jsonObject = new JsonObject();
			String message = response.getMessage();
			String authenticationToken = response.getAuthenticationToken();
			
			if(response.isSuccess())
				jsonObject.add("Success", new JsonPrimitive(SUCCESS));
			else
				jsonObject.add("Success", new JsonPrimitive(FAILURE));
			
			if(message!=null && !message.isEmpty())
				jsonObject.add("Message", new JsonPrimitive(message));
			
			if(authenticationToken!=null && !authenticationToken.isEmpty())
				jsonObject.add("AuthenticationToken", new JsonPrimitive(authenticationToken));
			
			
			return jsonObject;
		}
		
	}
	
}
