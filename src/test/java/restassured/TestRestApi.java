package restassured;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.lessThan;
import static org.testng.Assert.assertEquals;

import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;

//import com.jayway.restassured.path.json.JsonPath;


/**
 * This is a test class which gets executed against fake rest api data created by json-server
 * I have created a maven build file using which you can run the whole test suite used just one command
 * 
 * mvn exec:exec@Shellscript
 * @author gokul
 * @param <Posts>
 *
 */
public class TestRestApi<Posts> {
	
	public int uniqueNo;
	ResponseSpecBuilder builder;
	static ResponseSpecification rspec;
	
	public TestRestApi() {
		
	}
	
	@BeforeClass
	public static void setUp() {
		String port = System.getProperty("server.port");
		if(port == null){
			RestAssured.port = Integer.valueOf(3000);
		}
		
		String basePath = System.getProperty("server.base");
		if(basePath == null){
			basePath = "store";
		}
		    RestAssured.basePath = basePath;
		
		String baseHost = System.getProperty("server.host");
		if(baseHost == null){
			baseHost = "http://localhost";
		}
		    RestAssured.baseURI = baseHost;
		    
		    //Response spec builder helps to reuse the code in different parts of the class
		    ResponseSpecBuilder builder = new ResponseSpecBuilder();
		    builder.expectContentType(ContentType.JSON);
		    builder.expectStatusCode(200);
		    
		    rspec = builder.build();
	}
	
	@Test(priority=2)
	public void testStatusCode() {
		given().
		  get("/").
		then(). 
		  spec(rspec);
	}
	
	@Test(priority=3)
	public void testResponseDataUsingGroovy() {
		//Fetch the first author from the response data		
		String authorTitle = 
			when().
		     get("/"). 
		then(). 
		   contentType(ContentType.JSON).
		   extract().
		   path("store.book[0].title[0]");
		
		//Compare if the author is filtered in the below response data
		when(). 
		  get("/"). 
		then(). 
		  body("store.book[0].findAll { it.price < 10 }.title", hasItem(authorTitle));
		
		
		System.out.println("The response should have author "+ authorTitle);
	}
	
	@Test(priority=4)
    public void getResponseHeaders() {
   	 
		 //Loop through the list of headers and print them
	   	 Response resp = get("/");
	   	 Headers headers = resp.getHeaders();
	   	 
	   	 for(Header h:headers){
	   		 System.out.println(h.getName()+":"+h.getValue());
	   	 }
    }
	
	@Test(priority=5)
    public void getCookies() {
   	 
		 //Collects the cookies and assign it to a variable
	   	 Response resp = get("/");   	 
	   	 Map<String, String> allCookies = resp.getCookies();
	   	 
	   	 System.out.println("List of cookies are "+ allCookies);
    }
	
	@Test(priority=6)
	public void testResponseTime() {
		
		//Verifies if the endpoint responses within 2 seconds
		when(). 
		  get("/"). 
		then().
		  assertThat(). 
		  header("Content-Encoding", "gzip").
		  time(lessThan(2000L)). 
		  log(). 
          ifError(). 
		  spec(rspec);
	}
	
	@Test(priority=1)
	public void testPostData() {
		
		String jsonString = "{" +
    			"       \"city\": \"London\", \n" +
    			"       \"store\": {\n" +
    			"           \"book\": [\n" +
    			"                   { \n" +
	    		"						\"author\": \"Gokul\", \n" +
	    		"                       \"category\": \"fiction\", \n" +
	    		" 						\"price\": 20, \n" +
	    		"						\"title\": \"Post a json\"  \n" +
    			"                   },\n" +
	    		"                   { \n" +
	    		"                      		\"author\": \"Sridharan\", \n" +	
	    		"                           \"category\": \"Comedy\", \n" +
    			" 						    \"price\": 10, \n" +
    			"						    \"title\": \"Post a json\"  \n" +
    			"                   }\n" +
    			"              ]\n" +
    			"          }\n" +
    			"}";
    			
    	uniqueNo = given(). 
    					contentType("application/json").
    					body(jsonString).
    			   when().
    			   	    post("/").
    			   then(). 
    			   		extract(). 
    			   		path("id");
    	
    	System.out.println("The unique identifier for the posted data is "+ uniqueNo);
	}
	
	@Test(priority=7)
	public void testUpdateData() {
		
		//Create a json object which would be sent as a PUT request in RestAssured framework to 
		//update a payload 
		JsonNodeFactory factory = JsonNodeFactory.instance;
		
		ObjectNode pushContent = factory.objectNode();
		ObjectNode store = factory.objectNode();
		ObjectNode bookObj = factory.objectNode();
		
		ArrayNode books = factory.arrayNode();
		
		//Includes values to books array
		bookObj.put("author", "Update test");
		bookObj.put("category", "Updated Rest API");
		bookObj.put("price", 11);
		bookObj.put("title", "Verify if update works in api");

		
		books.add(bookObj);
		store.put("book",books);
		
		//Includes values to fake json data created
		pushContent.put("city", "Chennai");
		pushContent.put("store", store);
		pushContent.put("id", uniqueNo);
		
		System.out.println("The json array looks like" + pushContent);

		//Fetch the category from PUT endpoint
		String updatedCategory = given(). 
									contentType("application/json").
									body(pushContent).
								when().
								    put("/"+uniqueNo).
								then().
									extract(). 
							   		path("store.book[0].category");  
		
		//Verifies if the payload is being updated as expected
		assertEquals(updatedCategory, "Updated Rest API");
	}
	
	
	@Test(priority=8)
	public void testDeleteData() {
		
		//Deletes the above posted data
		given(). 
			contentType("application/json").
		when().
			delete("/"+uniqueNo).
	    then().
	        statusCode(200).log().all();
	}
	
}
