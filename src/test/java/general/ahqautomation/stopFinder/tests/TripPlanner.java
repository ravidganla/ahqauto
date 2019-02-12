package general.ahqautomation.stopFinder.tests;		

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
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

public class TripPlanner {				
    
	Response responseHTTP;
	boolean stationFound = false;
	JSONArray jsonArrayLocations;
	JSONArray jsonArrayModes;
	
    @Given("^Phileas is planning a trip$")			
    public void checkingTrips(){
        RestAssured.baseURI = "https://transportnsw.info";
        RestAssured.basePath = "web/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }		
	
	@SuppressWarnings("deprecation")
	@When("^he executes a trip plan from \"(.*)\" to \"(.*)\"$")
    public void getTripsForGivenStops(String pStationFrom, String pStationTo) throws Exception{
    	Map<String, String> parameters = new HashMap<String, String>();
    	parameters.put("TfNSWSF", "true");
    	parameters.put("language", "en");
    	parameters.put("name_sf", pStationFrom);
    	parameters.put("outputFormat", "rapidJSON");
    	parameters.put("type_sf", "any");
    	parameters.put("version", "10.2.2.48");
    	responseHTTP = given().parameters(parameters).when().get("XML_STOPFINDER_REQUEST");
    	String stopIDFrom = getStopID(responseHTTP, pStationFrom);
    	
    	parameters.put("name_sf", pStationTo);
    	responseHTTP = given().parameters(parameters).when().get("XML_STOPFINDER_REQUEST");
    	String stopIDTo = getStopID(responseHTTP, pStationTo);
    	
    	parameters = new HashMap<String, String>();
    	parameters.put("language", "en");
    	parameters.put("name_origin", stopIDFrom);
    	parameters.put("name_destination", stopIDTo);
    	parameters.put("outputFormat", "rapidJSON");
    	parameters.put("type_origin", "any");
    	parameters.put("type_destination", "any");
    	parameters.put("version", "10.2.2.48");
    	responseHTTP = null;
		responseHTTP = given().parameters(parameters).when().get("XML_TRIP_REQUEST2");
    }		
	
    @Then("^a list of trips should be provided$")					
    public void validateTripsCount() throws Throwable {
    	ArrayList<JSONObject> list = responseHTTP.jsonPath().getJsonObject("journeys");
    	Assert.assertTrue("Total trips: " + list.size(), list.size() > 1);
	}
	
	private String getStopID(Response pResponse, String pStopName) throws Exception {
		JSONObject jsonObject = new JSONObject(pResponse.asString());
		jsonArrayLocations = (JSONArray)jsonObject.get("locations");
		int totalLocations = jsonArrayLocations.length();
		
		for(int i=0; i<totalLocations; i++) {
			String stationName = (String) jsonArrayLocations.getJSONObject(i).get("name");
			if(stationName.equals(pStopName)) {
				stationFound = true;
				return (String) jsonArrayLocations.getJSONObject(i).get("id");
			}
		}
		return null;
	}

}