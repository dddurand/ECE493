package system;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.xml.sax.SAXException;

import util.Codes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

/**
 * Requires the server to be running with a test database
 * 
 * @author dddurand
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SystemTest {

	private static final String server = "http://localhost:8080/PokerWebService/";
	private static final String contentType = "application/gson";
	
	private static String user;
	private static String password;
	private static String auth;
	
	private static JsonParser parser;
	
	@BeforeClass
	public static void setup() {
		user = UUID.randomUUID().toString();
		password = UUID.randomUUID().toString();

		parser = new JsonParser();
		
	}
	
	@Test
	public void aRegisterTest() {
		
	
		WebConversation wc = new WebConversation();
		
		String string = "{\"username\":\""+user+"\",\"password\":\""+password+"\"}";
		ByteArrayInputStream inStream = new ByteArrayInputStream(string.getBytes());
		
	    WebRequest req = new PostMethodWebRequest(server+"RegisterServlet", inStream, contentType);
	    
	    try {
			WebResponse resp = wc.getResponse( req );
			String response = resp.getText();

			JsonObject jsonObj = (JsonObject) parser.parse(response);
			
			assertTrue(jsonObj.get("Code").getAsInt() == Codes.SUCCESS);
			
			
		} catch (IOException e) {
			fail();
		} catch (SAXException e) {
			fail();
		}
	}
	
	@Test
	public void bRegisterTest() {
		WebConversation wc = new WebConversation();
		
		String string = "{\"username\":\""+user+"\",\"password\":\""+password+"\"}";
		ByteArrayInputStream inStream = new ByteArrayInputStream(string.getBytes());
		
	    WebRequest req = new PostMethodWebRequest(server+"RegisterServlet", inStream, contentType);
	    
	    try {
			WebResponse resp = wc.getResponse( req );
			String response = resp.getText();

			JsonObject jsonObj = (JsonObject) parser.parse(response);
			assertTrue(jsonObj.get("Code").getAsInt() == Codes.DUPLICATE_USERNAME);
			
			
		} catch (IOException e) {
			fail();
		} catch (SAXException e) {
			fail();
		}
	}
	
	@Test
	public void cLoginTest() {
		WebConversation wc = new WebConversation();
		
		String string = "{\"username\":\""+user+"\",\"password\":\""+password+"\"}";
		ByteArrayInputStream inStream = new ByteArrayInputStream(string.getBytes());
		
	    WebRequest req = new PostMethodWebRequest(server+"LoginServlet", inStream, contentType);
	    
	    try {
			WebResponse resp = wc.getResponse( req );
			String response = resp.getText();
			
			JsonObject jsonObj = (JsonObject) parser.parse(response);
			assertTrue(jsonObj.get("Code").getAsInt() == Codes.SUCCESS);

			auth = jsonObj.get("AuthenticationToken").getAsString();
			
			assertTrue(auth!=null);
			
		} catch (IOException e) {
			fail();
		} catch (SAXException e) {
			fail();
		}
	}
	
	@Test
	public void dUploadTest() {
		WebConversation wc = new WebConversation();
		
		String string = "{\"username\":\""+user+"\",\"authenticationToken\":\""+auth+"\",\"games\":[{\"gameID\":123,\"gameActions\":[{\"action\":\"START\",\"pot\":0,\"bet\":0,\"hand\":\"DA;S2\",\"communityCards\":\"\"},{\"action\":\"CHECK\",\"pot\":0,\"bet\":0,\"hand\":\"DA;S2\",\"communityCards\":\"CA;C2;C5\"},{\"action\":\"CALL\",\"pot\":10,\"bet\":10,\"hand\":\"DA;S2\",\"communityCards\":\"CA;C2;C5;C9\"},{\"action\":\"RAISE\",\"pot\":20,\"bet\":10,\"hand\":\"DA;S2\",\"communityCards\":\"CA;C2;C5;C9;HT\"},{\"action\":\"LOSS\",\"pot\":20,\"bet\":20,\"hand\":\"DA;S2\",\"communityCards\":\"CA;C2;C5;C9;HT\"},{\"action\":\"END\",\"pot\":20,\"bet\":0,\"hand\":\"DA;S2\",\"communityCards\":\"CA;C2;C5;C9;HT\"}]}],\"miscDatas\":[{\"name\":\"MoneyGenerated\",\"value\":200}]}";
		ByteArrayInputStream inStream = new ByteArrayInputStream(string.getBytes());
		
	    WebRequest req = new PostMethodWebRequest(server+"UploadServlet", inStream, contentType);
	    
	    try {
			WebResponse resp = wc.getResponse( req );
			String response = resp.getText();
			
			JsonObject jsonObj = (JsonObject) parser.parse(response);
			assertTrue(jsonObj.get("Code").getAsInt() == Codes.SUCCESS);

			String gameID = jsonObj.get("game_success_uploads").getAsJsonArray().get(0).getAsString();
			
			assertTrue(gameID.equals("123"));
			
			int positionID = jsonObj.get("misc_success_uploads").getAsJsonArray().get(0).getAsInt();
			assertTrue(positionID == 0);
			
			
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void ePersonalTest() {
		WebConversation wc = new WebConversation();
		
		String string = "{\"timeframe\": \"ALL\",\"username\":\""+user+"\",\"authenticationToken\":\""+auth+"\"}";
		ByteArrayInputStream inStream = new ByteArrayInputStream(string.getBytes());
		
	    WebRequest req = new PostMethodWebRequest(server+"PersonalStatisticsServlet", inStream, contentType);
	    
	    try {
			WebResponse resp = wc.getResponse( req );
			String response = resp.getText();
			
			JsonObject jsonObj = (JsonObject) parser.parse(response);
			assertTrue(jsonObj.get("Code").getAsInt() == Codes.SUCCESS);

			JsonObject statsJson = jsonObj.get("personal_statistics").getAsJsonObject();
			
			assertTrue(statsJson.get("totalDollarsBetOnCalls").getAsInt() == 10);
			assertTrue(statsJson.get("totalDollarsBetOnRaises").getAsInt() == 10);
			assertTrue(statsJson.get("totalDollarsBet").getAsInt() == 40);
			assertTrue(statsJson.get("avgDollarsBetOnRaises").getAsInt() == 10);
			assertTrue(statsJson.get("totalNumberOfChecks").getAsInt() == 1);
			assertTrue(statsJson.get("totalNumberOfCalls").getAsInt() == 1);
			assertTrue(statsJson.get("totalNumberOfFolds").getAsInt() == 0);
			assertTrue(statsJson.get("totalNumberOfPotsLoss").getAsInt() == 1);
			assertTrue(statsJson.get("totalNumberOfPots").getAsInt() == 1);
			assertTrue(statsJson.get("avgPotOnRaises").getAsInt() == 20);
			assertTrue(statsJson.get("moneyGenerated").getAsInt() == 200);
			assertTrue(statsJson.get("netMoney").getAsInt() == -220);
			assertTrue(statsJson.get("avgBet").getAsInt() == 13);
			assertTrue(statsJson.get("optimality").getAsDouble() == 50);
			
			
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void fCommunityTest() {
		WebConversation wc = new WebConversation();
		
		String string = "{\"timeframe\": \"ALL\",\"username\":\""+user+"\",\"authenticationToken\":\""+auth+"\"}";
		ByteArrayInputStream inStream = new ByteArrayInputStream(string.getBytes());
		
	    WebRequest req = new PostMethodWebRequest(server+"CommunityStatisticsServlet", inStream, contentType);
	    
	    try {
			WebResponse resp = wc.getResponse( req );
			String response = resp.getText();
			
			JsonObject jsonObj = (JsonObject) parser.parse(response);
			assertTrue(jsonObj.get("Code").getAsInt() == Codes.SUCCESS);

			JsonObject statsJson = jsonObj.get("community_statistics").getAsJsonObject();
			
			assertTrue(statsJson.get("totalDollarsBetOnCalls").getAsInt() >= 10);
			assertTrue(statsJson.get("totalDollarsBetOnRaises").getAsInt() >= 10);
			assertTrue(statsJson.get("totalDollarsBet").getAsInt() >= 40);
			assertTrue(statsJson.get("totalNumberOfChecks").getAsInt() >= 1);
			assertTrue(statsJson.get("totalNumberOfCalls").getAsInt() >= 1);
			assertTrue(statsJson.get("totalNumberOfFolds").getAsInt() >= 0);
			assertTrue(statsJson.get("totalNumberOfPotsLoss").getAsInt() >= 1);
			assertTrue(statsJson.get("totalNumberOfPots").getAsInt() >= 1);
			assertTrue(statsJson.get("moneyGenerated").getAsInt() >= 200);
			
			
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void fRankTest() {
		WebConversation wc = new WebConversation();
		
		String string = "{\"timeframe\": \"ALL\",\"username\":\""+user+"\",\"authenticationToken\":\""+auth+"\"}";
		ByteArrayInputStream inStream = new ByteArrayInputStream(string.getBytes());
		
	    WebRequest req = new PostMethodWebRequest(server+"RankingStatisticsServlet", inStream, contentType);
	    
	    try {
			WebResponse resp = wc.getResponse( req );
			String response = resp.getText();
			
			JsonObject jsonObj = (JsonObject) parser.parse(response);
			assertTrue(jsonObj.get("Code").getAsInt() == Codes.SUCCESS);

			JsonArray statsJson = jsonObj.get("ranked_statistics").getAsJsonArray();
			JsonObject statsJson2 = jsonObj.get("my_ranked_statistics").getAsJsonObject();
			
			int position = statsJson2.get("position").getAsInt();
			String username = statsJson2.get("username").getAsString();
			double value = statsJson2.get("rankValue").getAsDouble();
			
			assertTrue(position > 0);
			assertTrue(username.equals(user));
			
			JsonObject statsJson3 = statsJson.get(position-1).getAsJsonObject();
			
			assertTrue(statsJson3.get("position").getAsInt() == position);
			assertTrue(statsJson3.get("username").getAsString().equals(user));
			assertTrue(statsJson3.get("rankValue").getAsDouble() == value);
			
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void logoutTest() {
		WebConversation wc = new WebConversation();
		
		String string = "{\"username\":\""+user+"\",\"authenticationToken\":\""+auth+"\"}";
		ByteArrayInputStream inStream = new ByteArrayInputStream(string.getBytes());
		
	    WebRequest req = new PostMethodWebRequest(server+"LogoutServlet", inStream, contentType);
	    
	    try {
			WebResponse resp = wc.getResponse( req );
			String response = resp.getText();
			
			JsonObject jsonObj = (JsonObject) parser.parse(response);
			assertTrue(jsonObj.get("Code").getAsInt() == Codes.SUCCESS);
			
		} catch (Exception e) {
			fail();
		}
	}

}
