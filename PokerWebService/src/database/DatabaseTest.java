package database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import util.PasswordUtil;
import util.ServletConfiguration;
import util.ServletConfiguration.Database;

import com.google.gson.Gson;

import dataModels.Account;

/**
 * Garbage Test File
 * @author dddurand
 *
 */
public class DatabaseTest {

	public static String Test()
	{
		String message = "";
		try 
        {
            // Get DataSource
            Context initContext  = new InitialContext();
            
            message+= "Lookup Starting...<br/>";
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            message+= "Success<br/><br/>";
            
            message+= "Getting Datasource...<br/>";
            DataSource dataSource;
            
            if(ServletConfiguration.getDatabase() == Database.UNIVERSITY)
            	dataSource = (DataSource)envContext.lookup("jdbc/DatabaseUofA");
            
            else if(ServletConfiguration.getDatabase() == Database.HOME)
            	dataSource = (DataSource)envContext.lookup("jdbc/DatabaseHome");
            
            else
            	throw new SQLException("Invalid Location");
            	
            message+= "Success<br/><br/>";
            
            message+= "Getting Connection...<br/>";
            Connection connection = dataSource.getConnection();
            message+= "Success<br/><br/>";
            
            String timezone = String.valueOf(connection.isValid(5));
            message+= "Valid Connection: <br/>" + timezone+"<br/><br/>";
            
            //Random random = new Random();
            
//            String insertSQL = "INSERT INTO user_table (username, password) VALUES (?,?);";
//            CallableStatement insertStatement = connection.prepareCall(insertSQL);
//            insertStatement.setString(1, "TestUser"+random.nextLong());
//            insertStatement.setString(2, "Password"+random.nextLong());
//            insertStatement.execute();
            
            String sql = "select * from user_table";
            CallableStatement statement = connection.prepareCall(sql);
            ResultSet set = statement.executeQuery();
            
            message+= "<b>DATABASE</b><br/>";
            message+= "User Table: </br>";
            
            while(set.next())
            {
            	String user = set.getString("username");
            	String pass = set.getString("password");
            	String auth = set.getString("auth_token");
            	message+= "<b>User:</b> " + user + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Password:</b> "+ pass + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Auth:</b> "+ auth +"</br>";
            	
            }
            message+="<br/>";
            
            connection.close();
            
            message+= "<b>Parameters:<b> <br/>";
            
            Database location = ServletConfiguration.getDatabase();
            message+= "Location: ";
            if(location == ServletConfiguration.Database.UNIVERSITY)
            	message+="UNI<br/><br/>";
            else
            	message+="HOME<br/><br/>";
            
            message+= "Debug: " + ServletConfiguration.isDebug()+"<br/><br/>";
            
           String pass = "bob's_your_uncle";
            message+= "Password Plain: " + pass + "<br/>";
            PasswordUtil passUtil = new PasswordUtil();
            
            message+= "Password Encyp: " + passUtil.encrypt(pass) + "<br/>";

            message+= "Password MATCH: " + passUtil.compare(pass, passUtil.encrypt(pass)) + "<br/>";
            message+= "Password BAD: " + passUtil.compare(pass, passUtil.encrypt(pass+" ")) + "<br/>";
            message+= "Password BAD: " + passUtil.compare(pass, "asdfasdfasdfasdf") + "<br/><br/>";

            
            Account account = new Account("bob", "is", "coolio.");
            Gson gson = new Gson();
            String json = gson.toJson(account, Account.class);
            message+= json + "<br/><br/>";
            
           Account account2 = gson.fromJson(json, Account.class);
           
           message+= "Equal JSON Conversion: " + account2.exactCompare(account);
           
            
        } 
         catch (NamingException e) 
         {
        	 message += e.getMessage();
        } catch (SQLException e) {

        	 message += e.getMessage();
		}
		return message;
		
	}
	
}
