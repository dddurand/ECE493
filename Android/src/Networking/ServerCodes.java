package networking;

import android.content.Context;

import com.example.bluetoothpoker.R;


public class ServerCodes {

	private Context context;
	
	public ServerCodes(Context context)
	{
		this.context = context;
	}
	
	public String getErrorMessage(int error_code)
	{
		int errorMessageID = getStringID(error_code);	
		return context.getString(errorMessageID);	
	}
	
	private int getStringID(int error_code)
	{
		switch(error_code)
		{
		case Codes.INVALID_LOGIN:
			return R.string.server_invalid_login;
		case Codes.DUPLICATE_USERNAME:
			return R.string.server_duplicate_user;
		case Codes.INVALID_USERNAME:
			return R.string.server_invalid_username;
		case Codes.INVALID_PASSWORD:
			return R.string.server_invalid_password;
		case Codes.NO_ACCOUNT:
			return R.string.server_unregistered;
		default:
			return R.string.server_unknown;	
			
		}
	}
	
	public class Codes {

		public static final int SUCCESS = 200;

		public static final int INVALID_LOGIN = 300;
		public static final int DUPLICATE_USERNAME = 301;
		public static final int INVALID_USERNAME = 302;
		public static final int INVALID_PASSWORD = 303;
		public static final int NO_ACCOUNT = 304;
		
		public static final int INVALID_AUTH = 305;
		public static final int NOT_AUTHED = 306;
		
		public static final int INVALID_DATA = 400;
		
		public static final int UNKNOWN = 500;
		public static final int DATABASE = 501;
		
	}
}
