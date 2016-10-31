package trelloAPI;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URLEncoder;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class EditCard 
{
	private static String key = testSuite.key;
	private static String token = testSuite.token;
	private static String cardUrl = "https://api.trello.com/1/cards/";
	private static String charset = testSuite.charset;
	
	@Rule public TestName testName = new TestName();
	
	/*
	 * Based on API reference: PUT/1/cards/card id/desc
	 * @param value
	 * */
	@Test public void editCard_change_desc_status_check_test() throws ClientProtocolException, IOException 
	{
		
		System.out.println("\n--------------------------------------------------");
		System.out.println("Start test: " + testName.getMethodName());
		
		//Given
		//Create a test card first
		String idList = testSuite.listId;
		String due = "null";
		String name = "Change this card description";
		String desc = "This description need to change";
		
		String query = String.format("idList=%s&due=%s&name=%s&desc=%s&key=%s&token=%s", 
										URLEncoder.encode(idList, charset), 
										URLEncoder.encode(due, charset),
										URLEncoder.encode(name, charset), 
										URLEncoder.encode(desc, charset), 
										URLEncoder.encode(key, charset), 
										URLEncoder.encode(token, charset));
		//When
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost postRequest = new HttpPost(cardUrl + "?" + query);
		HttpResponse response = httpClient.execute(postRequest);
		if(response.getStatusLine().getStatusCode() == 200)
		{
			JsonReader jsonReader = Json.createReader(response.getEntity().getContent());
			JsonObject cardJson = jsonReader.readObject();
			String cardId = cardJson.getString("id");
			jsonReader.close();
			String value = "Description has changed";
			
			String descQuery = String.format("value=%s&key=%s&token=%s",
											URLEncoder.encode(value, charset), 
											URLEncoder.encode(key,charset), 
											URLEncoder.encode(token, charset));
			//When
			HttpPut putRequest = new HttpPut(cardUrl + cardId + "/desc" + "?" + descQuery);
			HttpResponse putResponse = httpClient.execute(putRequest);
			//Then
			assertEquals(putResponse.getStatusLine().getStatusCode(), 200);
		}
		else
		{
			fail("Adding card is wrong, cannot change card description");
		}
		
		//Tear down
		httpClient.close();
		
		System.out.println("Finish test: " + testName.getMethodName());
		System.out.println("--------------------------------------------------\n");
		
	}
	
	/*
	 * Based on API reference: GET/1/cards/[card id]/field
	 * Get description: GET/1/cards/[card id]/desc
	 * */
	@Test public void editCard_change_desc_content_check_test() throws ClientProtocolException, IOException
	{
		System.out.println("\n--------------------------------------------------");
		System.out.println("Start test: " + testName.getMethodName());
		
		//Given
		//Create a test card first
		String idList = testSuite.listId;
		String due = "null";
		String name = "Change description then check content";
		String desc = "This description need to change";
		
		String query = String.format("idList=%s&due=%s&name=%s&desc=%s&key=%s&token=%s", 
										URLEncoder.encode(idList, charset), 
										URLEncoder.encode(due, charset),
										URLEncoder.encode(name, charset), 
										URLEncoder.encode(desc, charset), 
										URLEncoder.encode(key, charset), 
										URLEncoder.encode(token, charset));
		//When
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost postRequest = new HttpPost(cardUrl + "?" + query);
		HttpResponse response = httpClient.execute(postRequest);
		if(response.getStatusLine().getStatusCode() == 200)
		{
			JsonReader jsonReader = Json.createReader(response.getEntity().getContent());
			JsonObject cardJson = jsonReader.readObject();
			String cardId = cardJson.getString("id");
			jsonReader.close();
			String value = "Description has changed";
			String descQuery = String.format("value=%s&key=%s&token=%s",
											URLEncoder.encode(value, charset), 
											URLEncoder.encode(key,charset), 
											URLEncoder.encode(token, charset));
			//When
			HttpPut putRequest = new HttpPut(cardUrl + cardId + "/desc" + "?" + descQuery);
			httpClient.execute(putRequest);
			//Then
			//Get changed card description
			String keyTokenQuery = String.format("key=%s&token=%s",
													URLEncoder.encode(key, charset), 
													URLEncoder.encode(token, charset));
			HttpGet getDescRequest = new HttpGet(cardUrl + cardId + "/desc" + "?" + keyTokenQuery);
			HttpResponse getDescResponse = httpClient.execute(getDescRequest);
			if(getDescResponse.getStatusLine().getStatusCode() == 200)
			{
				JsonReader descJsonReader = Json.createReader(getDescResponse.getEntity().getContent());
				JsonObject descJson = descJsonReader.readObject();
				descJsonReader.close();
				String descValue = descJson.getString("_value");
				assertEquals(descValue, value);
			}
			else
			{
				fail("test fail, cannot get description through API");
			}
		}
		else
		{
			fail("Adding card is wrong, cannot change card description");
		}
		//Tear down
		httpClient.close();
		
		
		System.out.println("Finish test: " + testName.getMethodName());
		System.out.println("--------------------------------------------------\n");
	}
	
	/*
	 * Based on API reference: POST/1/cards/card id/labels
	 * @param color
	 * */
	@Test public void editCard_add_lable_status_check_test() throws ClientProtocolException, IOException
	{
		System.out.println("\n--------------------------------------------------");
		System.out.println("Start test: " + testName.getMethodName());
		
		//Given
		//Create a card to change label
		String idList = testSuite.listId;
		String due = "null";
		String name = "Change this card label";
		String desc = "The label of this card need to change";
		
		String query = String.format("idList=%s&due=%s&name=%s&desc=%s&key=%s&token=%s", 
										URLEncoder.encode(idList, charset), 
										URLEncoder.encode(due, charset),
										URLEncoder.encode(name, charset), 
										URLEncoder.encode(desc, charset), 
										URLEncoder.encode(key, charset), 
										URLEncoder.encode(token, charset));
		//When
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost postRequest = new HttpPost(cardUrl + "?" + query);
		HttpResponse response = httpClient.execute(postRequest);
		if(response.getStatusLine().getStatusCode() == 200)                                                             //Add card successfully
		{
			JsonReader jsonReader = Json.createReader(response.getEntity().getContent());
			JsonObject cardJson = jsonReader.readObject();
			String cardId = cardJson.getString("id");
			jsonReader.close();
			String color = "red";
			
			String labelQuery = String.format("color=%s&key=%s&token=%s",
											URLEncoder.encode(color, charset), 
											URLEncoder.encode(key, charset), 
											URLEncoder.encode(token, charset));
			//When
			HttpPost postLabelRequest = new HttpPost(cardUrl + cardId + "/labels" + "?" + labelQuery);
			HttpResponse postLabelresponse = httpClient.execute(postLabelRequest);
			//Then
			assertEquals(postLabelresponse.getStatusLine().getStatusCode(), 200);
		}
		else
		{
			fail("Adding card is wrong, cannot change the label");
		}
		//Tear down
		httpClient.close();
		
		System.out.println("Finish test: " + testName.getMethodName());
		System.out.println("--------------------------------------------------\n");
	}
	
	/*
	 * Based on API reference: Get/1/cards/[card id]/field
	 * Get label color: GET/1/cards/[card id]/labels
	 * */
	@Test public void editCard_add_label_content_check_test() throws ClientProtocolException, IOException
	{
		System.out.println("\n--------------------------------------------------");
		System.out.println("Start test: " + testName.getMethodName());
		
		//Given
		//Create a card to change label
		String idList = testSuite.listId;
		String due = "null";
		String name = "Change label, then check label color";
		String desc = "The label of this card need to change";
		
		String query = String.format("idList=%s&due=%s&name=%s&desc=%s&key=%s&token=%s", 
										URLEncoder.encode(idList, charset), 
										URLEncoder.encode(due, charset),
										URLEncoder.encode(name, charset), 
										URLEncoder.encode(desc, charset), 
										URLEncoder.encode(key, charset), 
										URLEncoder.encode(token, charset));
		//When
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost postRequest = new HttpPost(cardUrl + "?" + query);
		HttpResponse response = httpClient.execute(postRequest);
		if(response.getStatusLine().getStatusCode() == 200)                                                             //Add card successfully
		{
			JsonReader jsonReader = Json.createReader(response.getEntity().getContent());
			JsonObject cardJson = jsonReader.readObject();
			String cardId = cardJson.getString("id");
			jsonReader.close();
			String color = "red";
			
			String labelQuery = String.format("color=%s&key=%s&token=%s",
											URLEncoder.encode(color, charset), 
											URLEncoder.encode(key, charset), 
											URLEncoder.encode(token, charset));
			//When
			HttpPost postLabelRequest = new HttpPost(cardUrl + cardId + "/labels" + "?" + labelQuery);
			HttpResponse postLabelresponse = httpClient.execute(postLabelRequest);
			//Then
			String keyTokenQuery = String.format("key=%s&token=%s",
													URLEncoder.encode(key, charset),
													URLEncoder.encode(token, charset));
			HttpGet getColorRequest = new HttpGet(cardUrl + cardId + "/labels" + "?" + keyTokenQuery);
			HttpResponse labelResponse = httpClient.execute(getColorRequest);
			if(labelResponse.getStatusLine().getStatusCode() == 200)
			{
				JsonReader labelReader = Json.createReader(labelResponse.getEntity().getContent());
				JsonArray labelJson = labelReader.readArray();
				labelReader.close();
				String labelColor = labelJson.getJsonObject(0).getString("color");
				assertEquals(labelColor, color);
			}
			else
			{
				fail("Unable to get label color, test failes");
			}
		}
		else
		{
			fail("Adding card is wrong, cannot change the label");
		}
		//Tear down
		httpClient.close();
		
		System.out.println("Finish test: " + testName.getMethodName());
		System.out.println("--------------------------------------------------\n");
	}
}
