package restassured;

import static io.restassured.RestAssured.*;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import static org.hamcrest.Matchers.equalTo;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static io.restassured.module.jsv.JsonSchemaValidatorSettings.settings;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;


/**
 * Unit test for simple App.
 */
public class TestRestAssuredBasics
{
	

	public static String url = "http://localhost:3000/personal";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TestRestAssuredBasics()
    {
        
    }

    /**
     * test the statuscode
     */
    @Test
    public void testStatusCode()
    {
    	given().
    		get(url).
        then().
         statusCode(200);
    }

    /**
     * 
     */
    @Test
    public void testApp()
    {
        given().
        	get(url).
        then().
        	statusCode(200).
        	log().all();    	
    }
    
    /**
     * Verify if data in response is equal to expected result using hamcrest library
     */
    @Test
    public void testEqualToFunction()
    {
    	given(). 
    		get(url). 
    	then(). 
    		body("search.Housetype[1]",equalTo("independent"));
    }
    
    /**
     *  Verify multiple items in response data using hamcrest
     */
    //@Test
    public void testHasItemFunction()
    {
    	given(). 
    		get(url). 
    	then(). 
    		body("firstname",hasItems("Carmel","Jamaal","Carey"));
    }
    
    /**
     * 
     */
    @Test
    public void testRoot()
    {
    	given(). 
    		get(url). 
    	then(). 
    	  root("search[1]"). 
    	  body("Housetype", is("independent")). 
    	  body("location",is("London")). 
    	  body("minvalue",is(29033));
    }
    
    //@Test
    public void testPostData() throws IOException
    {
    	
    	String jsonString = "{" +
    			" 		\"firstname\": \"Gokul\", \n" +
    			"		\"secondname\": \"Sridharan\", \n" +
    			"       \"city\": \"London\", \n" +
    			"       \"search\": {\n" +
    			"						\"Housetype\": \"independent\", \n" +
    			"                       \"location\": \"London\", \n" +
    			" 						\"minvalue\": 29033, \n" +
    			"						\"maxvalue\": 99999  \n" +
    			"                   }\n" +
    			"}";
    			
    	given(). 
    	   contentType("application/json").
    	   body(jsonString).
    	when().
    	   post(url).
    	then(). 
    	   statusCode(201).log().all();
    	
    }
    
    /**
     * 
     */
    @Test
    public void testResponseAsString(){
    	String responseAsString = get(url+"/50").asString();
    	System.out.println("My Response:\n\n\n"+responseAsString);
    }
    
    /**
     * 
     */
    @Test
    public void testResponseAsInputStream(){
    	InputStream stream = get(url+"/50").asInputStream();
    	System.out.println("Stream length:"+stream.toString());
    }
    
    @Test
    public void testResponseByteAsArray(){
    	byte[] byteArray = get(url).asByteArray();
    	System.out.println("Byte array is:"+byteArray.length);
    }
    
    
    /**
     * Extract data from response
     */
     @Test
     public void testJsonData(){
    	 int val =
    			 when().
    			    get(url+"/50").
    			  then(). 
    			    contentType(ContentType.JSON).
    			    body("id", equalTo(50)).
    			  extract().
    			  	path("search.minvalue");
    	 
    	 System.out.println("The extracted data from json is "+val);
    			  
    	 //assertEquals((Integer)val, 29033, "Verification to check if int values are matching");
    			 
     }
     
     /**
      * Extract response details from json
      */
     @Test
     public void testExtractResponseContents(){
    	 Response resp = 
    			 when(). 
    			   get(url). 
    			  then().
    			  extract().
    			    response();
    	 
    	 System.out.println("Content-type on response is "+ resp.contentType());
    	 System.out.println("Status code for response is "+ resp.statusCode());
     }
     
     /**
      * To get all attributes as list
      */
     @Test
     public void verifyResponseDataFromList(){
    	 
    	 List<String> ls = get(url).path("city");
    	 
    	 System.out.println("List size is "+ls.size());
    	 for(String city:ls){
    		 if(city.equals("Leeburgh")) {
    			 System.out.println("Found my place");
    		 }
    	 }
     }
     
     /**
      * Get response headers
      */
     @Test
     public void getResponseHeaders(){
    	 
    	 Response resp = get(url);
    	 Headers headers = resp.getHeaders();
    	 
    	 for(Header h:headers){
    		 System.out.println(h.getName()+":"+h.getValue());
    	 }
     }
     
     /**
      * Get cookies
      */
     @Test
     public void getCookies(){
    	 
    	 Response resp = get(url);   	 
    	 Map<String, String> allCookies = resp.getCookies();
    	 
    	 System.out.println("List of cookies are "+ allCookies);
     }
}
