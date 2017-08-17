package restassured;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

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
		
		System.out.println("got valid status code");
		 
	}

}
