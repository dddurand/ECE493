package database;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import dataModels.CommunityStatistics;
import dataModels.PersonalStatistics;
import dataModels.RankingStatistics;

/**
 * The object used to generate the JSON response from a webservice call.
 * 
 * @SRS 3.2.2.1 -> 3.2.2.8
 * @author dddurand
 *
 */
public class ResponseObject {

	public final static String SUCCESS = "TRUE";
	public final static String FAILURE = "FALSE";
	
	private boolean success;
	private String message;
	private int code;
	private ArrayList<String> uploadGameSuccess;
	private ArrayList<Integer> uploadMiscSuccess;
	private PersonalStatistics personalStatistics;
	private RankingStatistics rankingStatistics;
	private CommunityStatistics communityStatistics;
	
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
	public ResponseObject(boolean success, String message, int code)
	{
		this.success = success;
		this.message = message;
		this.code = code;
	}
	
	
	
	/**
	 * Getter for response code
	 * @return
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Setter for response code
	 * @return
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * Community stats getter
	 * @return
	 */
	public CommunityStatistics getCommunityStatistics() {
		return communityStatistics;
	}

	/**
	 * Community stats setter
	 * 
	 * @param communityStatistics
	 */
	public void setCommunityStatistics(CommunityStatistics communityStatistics) {
		this.communityStatistics = communityStatistics;
	}

	/**
	 * Ranking Stats Getter
	 * 
	 * @return
	 */
	public RankingStatistics getRankingStatistics() {
		return rankingStatistics;
	}

	/**
	 * Ranking Statistics Setter
	 * 
	 * @param rankingStatistics
	 */
	public void setRankingStatistics(RankingStatistics rankingStatistics) {
		this.rankingStatistics = rankingStatistics;
	}

	/**
	 * Getter for uploadGameSuccess
	 * @return
	 */
	public ArrayList<String> getUploadGameSuccess() {
		return uploadGameSuccess;
	}

	/**
	 * Setter for uploadGameSuccess
	 * @return
	 */
	public void setUploadGameSuccess(ArrayList<String> uploadGameSuccess) {
		this.uploadGameSuccess = uploadGameSuccess;
	}

	/**
	 * Getter for uploadMiscSuccess
	 * @return
	 */
	public ArrayList<Integer> getUploadMiscSuccess() {
		return uploadMiscSuccess;
	}

	/**
	 * Setter for uploadMiscSuccess
	 * @return
	 */
	public void setUploadMiscSuccess(ArrayList<Integer> miscUploadResults) {
		this.uploadMiscSuccess = miscUploadResults;
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
	 * Getter for personal stats
	 * 
	 * @return
	 */
	public PersonalStatistics getPersonalStatistics() {
		return personalStatistics;
	}

	/**
	 * Setter for personal stats
	 * 
	 * @return
	 */
	public void setPersonalStatistics(PersonalStatistics personalStatistics) {
		this.personalStatistics = personalStatistics;
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
			ArrayList<String> gameResults = response.getUploadGameSuccess();
			ArrayList<Integer> miscResults = response.getUploadMiscSuccess();
			PersonalStatistics personalStatistics = response.getPersonalStatistics();
			RankingStatistics rankingStatistics = response.getRankingStatistics();
			CommunityStatistics communityStatistics = response.getCommunityStatistics();
			
			jsonObject.add("Code", new JsonPrimitive(response.getCode()));
			
			if(response.isSuccess())
				jsonObject.add("Success", new JsonPrimitive(SUCCESS));
			else
				jsonObject.add("Success", new JsonPrimitive(FAILURE));
			
			if(message!=null && !message.isEmpty())
				jsonObject.add("Message", new JsonPrimitive(message));
			
			if(authenticationToken!=null && !authenticationToken.isEmpty())
				jsonObject.add("AuthenticationToken", new JsonPrimitive(authenticationToken));
			
			if(gameResults!= null && !gameResults.isEmpty())
			{
				JsonArray results = new JsonArray();
				for(String gameID : gameResults)
					results.add(new JsonPrimitive(gameID));
				
				jsonObject.add("game_success_uploads", results);
					
			}
			
			if(miscResults!= null && !miscResults.isEmpty())
			{
				JsonArray results = new JsonArray();
				for(Integer position : miscResults)
					results.add(new JsonPrimitive(position));
				
				jsonObject.add("misc_success_uploads", results);
			}
			
			if(personalStatistics != null)
			{
				JsonElement element = context.serialize(personalStatistics);
				jsonObject.add("personal_statistics", element);
			}
			
			if(rankingStatistics != null)
			{
				JsonElement rankedDataElement = context.serialize(rankingStatistics.getRankedData());
				JsonElement myRankedDataElement = context.serialize(rankingStatistics.getMyRankData());
				jsonObject.add("ranked_statistics", rankedDataElement);
				jsonObject.add("my_ranked_statistics", myRankedDataElement);
			}
			
			if(communityStatistics != null)
			{
				JsonElement commDataElement = context.serialize(communityStatistics);
				jsonObject.add("community_statistics", commDataElement);
			}
			
			
			return jsonObject;
		}
		
	}
	
}
