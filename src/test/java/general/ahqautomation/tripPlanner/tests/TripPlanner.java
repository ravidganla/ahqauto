package general.ahqautomation.tripPlanner.tests;		

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;		

public class TripPlanner {				
	
	private WebDriver driver;
	private Properties allProperties = new Properties();
	private By fromLocationID; 
	private By toLocationID; 
	private By searchButtonID; 
	private By tripsList; 
	private String tripsTimes; 
	WebDriverWait wait;
    
	@Before
    public void setUp(Scenario scenario){
		allProperties = getProperties("ObjectProperties.properties");
		fromLocationID = By.id(allProperties.getProperty("FromLocation"));
		toLocationID = By.id(allProperties.getProperty("ToLocation"));
		searchButtonID = By.id(allProperties.getProperty("SearchButton"));
		tripsList = By.xpath(allProperties.getProperty("TripsList"));
		tripsTimes = allProperties.getProperty("TripsTime");
    }	
	
	@After
    public void cleanUp() throws InterruptedException{
	    driver.quit();
	}	
	
    @Given("^Phileas is planning a trip using \"(.*)\" browser$")				
    public void open_the_browser_and_launch_the_application(String pBrowser){
    	String tripSite = "https://transportnsw.info/trip";
    	switch(pBrowser.toLowerCase()) {
		case "chrome" :
			System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
			driver= new ChromeDriver();
			break; 
	   
		case "ie" :
   	        System.setProperty("webdriver.ie.driver", "src/test/resources/drivers/IEDriverServer.exe");
   	        driver= new InternetExplorerDriver();
   	        break; 
   	        
		default : 
	        System.setProperty("webdriver.gecko.driver", "src/test/resources/drivers/geckodriver.exe");
	        driver= new FirefoxDriver();
    	}
        driver.manage().window().maximize();			
		wait = new WebDriverWait(driver, 10);
        driver.get(tripSite);
    }		

    @When("^he executes a trip plan from \"(.*)\" to \"(.*)\"$")					
    public void enterTripLocations(String pFromLocation, String pToLocation) throws InterruptedException{
       driver.findElement(fromLocationID).sendKeys(pFromLocation);
       driver.findElement(toLocationID).sendKeys(pToLocation);
       Thread.sleep(1000);
       driver.findElement(toLocationID).sendKeys(Keys.TAB);
       driver.findElement(searchButtonID).click();
       Thread.sleep(1000);
    }		
    
    
    @Then("^a list of trips should be provided$")					
    public void validateTripOptions() throws Throwable {
    	System.out.println("Total trips: " + driver.findElements(tripsList).size());
        List<WebElement> totalItems = driver.findElements(tripsList);
    	Assert.assertTrue(driver.findElements(tripsList).size() > 0);
    	for(int i=0; i< totalItems.size(); i++) {
    		System.out.println(driver.findElement(By.xpath(tripsTimes.replace("[OBJINDEX]", i+""))).getText().replaceAll("\n", ":"));
    	}
    }		
    
	private Properties getProperties(String pPropertiesFile) {
		Properties methodProperties = new Properties();
	    InputStream stream = null;
	    ClassLoader loader = Thread.currentThread().getContextClassLoader();
	    stream = loader.getResourceAsStream(pPropertiesFile);
	    
	    try {
	    	methodProperties.load(stream);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return methodProperties;
	}
}