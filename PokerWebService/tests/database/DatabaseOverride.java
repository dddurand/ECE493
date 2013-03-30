package database;

import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.sql.DataSource;

import util.Codes;
import util.PasswordUtil;

/**
 * Specific Class that allows an database to be override. This is specific for
 * the CRON job servlet.
 * 
 * @author dddurand
 *
 */
public class DatabaseOverride extends DatabaseInterface {

	public DatabaseOverride(boolean isReal) throws SQLException, ClassNotFoundException, DatabaseInterfaceException
	{
		super(false);

		passUtil = new PasswordUtil();
		
		if(!isReal)
		{
			Class.forName("com.mysql.jdbc.Driver");

			this.dbConnection = DriverManager.getConnection(
					"jdbc:mysql://localhost:14000/test",
					"dddurand",
					"asdfasdf" );
		}
		else
		{
			try{
				DataSource dataSource = this.getDataSource();
				dbConnection = dataSource.getConnection();
				
			} 
			catch (NamingException e)
			{
				throw new DatabaseInterfaceException(e);
			} catch (SQLException e) {
				throw new DatabaseInterfaceException("Error retrieving connection to database", e, Codes.DATABASE);
			}
		}
	}

}
