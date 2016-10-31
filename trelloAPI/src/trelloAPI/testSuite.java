package trelloAPI;

import java.io.IOException;
import java.net.URLEncoder;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({cardManipulation.class, EditCard.class})

public class testSuite 
{
	public static String boardId;
	public static String listId;
	public static String key;
	public static String token;
	public static String charset = "UTF-8";
	public static String boardUrl = "https://api.trello.com/1/boards/";
	
	@BeforeClass
	public static void setUp() throws ClientProtocolException, IOException
	{
		System.out.println("Start to set up--------------");
		//Create a new test board
		/*
		 * Based on API reference: POST/1/boards
		 * */
		String name = "Test Board";
		String desc = "API test";
		String query = String.format("name=%s&desc=%s&key=%s&token=%s",
										URLEncoder.encode(name, charset),
										URLEncoder.encode(desc, charset),
										URLEncoder.encode(key, charset), 
										URLEncoder.encode(token, charset));
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost postRequest = new HttpPost(boardUrl + "?" + query);
		HttpResponse response = httpClient.execute(postRequest);
		if(response.getStatusLine().getStatusCode() == 200)
		{
			JsonReader jsonReader = Json.createReader(response.getEntity().getContent());
			JsonObject boardJson = jsonReader.readObject();
			boardId = boardJson.getString("id");
			jsonReader.close();		
			
			//Get list id for adding cards
			String getQuery = String.format("key=%s&token=%s",
												URLEncoder.encode(key, charset), 
												URLEncoder.encode(token, charset));
			HttpGet getRequest = new HttpGet(boardUrl + boardId + "/lists" + "?" + getQuery);
			HttpResponse getResponse = httpClient.execute(getRequest);
			if(getResponse.getStatusLine().getStatusCode() == 200)
			{
				System.out.println("Create a test board...");
				JsonReader getJsonReader = Json.createReader(getResponse.getEntity().getContent());
				JsonObject listJson = getJsonReader.readArray().getJsonObject(0);
				listId = listJson.getString("id");

			}
			httpClient.close();
		}
		else
		{
			System.out.println("Key or token is wrong.");
		}
		
	}
	
	@AfterClass
	public static void tearDown() throws ClientProtocolException, IOException
	{
		System.out.println("Start to tear down-----------");
		//Close the new test board
		/*
		 * Based on API reference: PUT/1/boards/[board_id]/closed
		 * */
		String value = "true";
		String query = String.format("value=%s&key=%s&token=%s",
										URLEncoder.encode(value, charset),
										URLEncoder.encode(key, charset), 
										URLEncoder.encode(token, charset));
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPut putRequest = new HttpPut(boardUrl + boardId + "/closed" + "?" + query);
		HttpResponse response = httpClient.execute(putRequest);
		if(response.getStatusLine().getStatusCode() == 200)
		{
			System.out.println("Test board is closed...Clean up");
			httpClient.close();
		}
	}
}
