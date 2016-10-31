package trelloAPI;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URLEncoder;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class cardManipulation 
{
	private static String key = testSuite.key;
	private static String token = testSuite.token;
	private static String cardUrl = "https://api.trello.com/1/cards/";
	private static String charset = testSuite.charset;
	
	@Rule public TestName testName = new TestName();
	
	/*
	 * Based on the API reference: POST/1/cards
	 * @param name, desc, due, idList
	 */
	@Test public void addCard_response_check_status_test() throws ClientProtocolException, IOException
	{
		System.out.println("\n--------------------------------------------------");
		System.out.println("Start test: " + testName.getMethodName());
		
		//Given
		String idList = testSuite.listId;
		String due = "null";
		String name = "Add card";
		String desc = "API test - Add card through trello API";
		
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
		//Then
		/*
		 * Expect: 200 - status code
		 * */
		assertEquals(response.getStatusLine().getStatusCode(), 200);
		
		//Tear down
		httpClient.close();
		
		System.out.println("Finish test: " + testName.getMethodName());
		System.out.println("--------------------------------------------------\n");
	}
	
	/*
	 * Based on the API reference: DELETE/1/cards/card id
	 * */
	@Test public void delete_card_check_status_test() throws ClientProtocolException, IOException
	{		
		System.out.println("\n--------------------------------------------------");
		System.out.println("Start test: " + testName.getMethodName());
		
		//Given
		//Create a card first
		String idList = testSuite.listId;
		String due = "null";
		String name = "Card need to delete";
		String desc = "API test - create this card and need to delete";
		
		String createQuery = String.format("idList=%s&due=%s&name=%s&desc=%s&key=%s&token=%s", 
										URLEncoder.encode(idList, charset), 
										URLEncoder.encode(due, charset),
										URLEncoder.encode(name, charset), 
										URLEncoder.encode(desc, charset), 
										URLEncoder.encode(key, charset), 
										URLEncoder.encode(token, charset));
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost postRequest = new HttpPost(cardUrl + "?" + createQuery);
		HttpResponse response = httpClient.execute(postRequest);
		if(response.getStatusLine().getStatusCode() == 200)                                								//Add test card successfully      
		{
			JsonReader jsonReader = Json.createReader(response.getEntity().getContent());
			JsonObject cardJson = jsonReader.readObject();
			String cardId = cardJson.getString("id");
			jsonReader.close();
			String query = String.format("key=%s&token=%s", 
					URLEncoder.encode(key, charset), 
					URLEncoder.encode(token, charset));
			//When
			HttpDelete deleteRequest = new HttpDelete(cardUrl + cardId + "?" + query);
			HttpResponse delResponse = httpClient.execute(deleteRequest);
			//Then
			/*
			 * Expect: 200 - Status code
			 * */
			assertEquals(delResponse.getStatusLine().getStatusCode(), 200);
		}
		else
		{
			fail("Adding card is wrong, cannot delete card");
		}

		//Tear down
		httpClient.close();
		
		System.out.println("Finish test: " + testName.getMethodName());
		System.out.println("--------------------------------------------------\n");
	}
	
	/*
	 * Test the default reponse content type should be JSON
	 * */
	@Test public void get_card_ckeck_content_format_is_JSON_test() throws ClientProtocolException, IOException
	{
		
		System.out.println("\n--------------------------------------------------");
		System.out.println("Start test: " + testName.getMethodName());
		
		//Given
		//Add a card to get card content
		String idList = testSuite.listId;
		String due = "null";
		String name = "Card need to get content";
		String desc = "API test - create this card and need to get its content";
		
		String createQuery = String.format("idList=%s&due=%s&name=%s&desc=%s&key=%s&token=%s", 
										URLEncoder.encode(idList, charset), 
										URLEncoder.encode(due, charset),
										URLEncoder.encode(name, charset), 
										URLEncoder.encode(desc, charset), 
										URLEncoder.encode(key, charset), 
										URLEncoder.encode(token, charset));
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost postRequest = new HttpPost(cardUrl + "?" + createQuery);
		HttpResponse response = httpClient.execute(postRequest);
		if(response.getStatusLine().getStatusCode() == 200) 															//Add test card successfully 
		{
			JsonReader jsonReader = Json.createReader(response.getEntity().getContent());
			JsonObject cardJson = jsonReader.readObject();
			String cardId = cardJson.getString("id");
			jsonReader.close();
			String query = String.format("key=%s&token=%s",
					URLEncoder.encode(key, charset), 
					URLEncoder.encode(token, charset));
			//When
			HttpGet getRequest = new HttpGet(cardUrl + cardId + "?" + query);
			HttpResponse getResponse = httpClient.execute(getRequest);
			//Then
			/*
			 * Expect: /application/json - content format
			 * */
			String type = getResponse.getEntity().getContentType().getValue().toString();
			assertThat(type, CoreMatchers.containsString("application/json"));
		}
		else
		{
			fail("Adding card is wrong, cannot get card content format");
		}
		//Tear down
		httpClient.close();
		
		System.out.println("Finish test: " + testName.getMethodName());
		System.out.println("--------------------------------------------------\n");
	}

}
