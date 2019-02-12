package general.ahqautomation.stopFinder.tests;		

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;		

public class StopFinder {				
    
	Response responseHTTP;
	boolean stationFound = false;
	JSONArray jsonArrayLocations;
	JSONArray jsonArrayModes;
	
    @Given("^Phileas is looking for a stop$")			
    public void checkingStop(){
        RestAssured.baseURI = "https://transportnsw.info";
        RestAssured.basePath = "web/XML_STOPFINDER_REQUEST";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }		
	
	@SuppressWarnings("deprecation")
	@When("^he searches for \"(.*)\"$")
    public void searchForStation(String pStation){
    	Map<String, String> parameters = new HashMap<String, String>();
    	parameters.put("TfNSWSF", "true");
    	parameters.put("language", "en");
    	parameters.put("name_sf", pStation);
    	parameters.put("outputFormat", "rapidJSON");
    	parameters.put("type_sf", "any");
    	parameters.put("version", "10.2.2.48");

		responseHTTP = given().parameters(parameters).when().get();
		responseHTTP.prettyPeek();
    }		
	
	
    @Then("^a stop named \"(.*)\" is found$")					
    public void validateStopName(String pStationName) throws Throwable {
    	int totalLocations =0 ;
    	String stationName = null;
		JSONObject jsonObject = new JSONObject(responseHTTP.asString());
		jsonArrayLocations = (JSONArray)jsonObject.get("locations");
		totalLocations = jsonArrayLocations.length();
		
		for(int i=0; i<totalLocations; i++) {
			stationName = (String) jsonArrayLocations.getJSONObject(i).get("name");
			jsonArrayModes = (JSONArray) jsonArrayLocations.getJSONObject(i).get("modes");
			if(stationName.equals(pStationName)) {
				stationFound = true;
				break;
			}
		}
    	Assert.assertTrue("No stop with name: " + pStationName, stationName.equals(pStationName));
    }		
    
    
    @Then("^the stop provides more than one mode of transport$")					
    public void validateModes() throws Throwable {
    	Assert.assertTrue("Either stop not found or Modes of transport is not more than 1", stationFound && jsonArrayModes.length() > 1);
    }
}