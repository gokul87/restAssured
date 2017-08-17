package restassured;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.*;
import io.restassured.response.Response;

//import com.jayway.restassured.path.json.JsonPath;

public class TestRestApi {
	
	
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
		    
		    
	}
	
	@Test
	public void testStatusCode() {
		given().
		  get("/").
		then().
		  statusCode(200);
	}
	
	@Test
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
	
	@Test
    public void getResponseHeaders() {
   	 
	   	 Response resp = get("/");
	   	 Headers headers = resp.getHeaders();
	   	 
	   	 for(Header h:headers){
	   		 System.out.println(h.getName()+":"+h.getValue());
	   	 }
    }
	
	@Test
    public void getCookies() {
   	 
	   	 Response resp = get("/");   	 
	   	 Map<String, String> allCookies = resp.getCookies();
	   	 
	   	 System.out.println("List of cookies are "+ allCookies);
    }
	
	@Test
	public void testResponseTime() {
		
		//Verifies if the endpoint responses within 2 seconds
		when(). 
		  get("/"). 
		then().
		  assertThat(). 
		  header("Content-Encoding", "gzip").
		  time(lessThan(2000L));
	}
	
	@Test
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
	    		"                           \"category\": \"fiction\", \n" +
    			" 						    \"price\": 10, \n" +
    			"						    \"title\": \"Post a json\"  \n" +
    			"                   }\n" +
    			"              ]\n" +
    			"          }\n" +
    			"}";
    			
    	given(). 
    	   contentType("application/json").
    	   body(jsonString).
    	when().
    	   post("/").
    	then(). 
    	   statusCode(201).log().all();
	}
	
	
}
