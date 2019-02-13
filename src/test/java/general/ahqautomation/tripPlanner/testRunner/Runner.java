package general.ahqautomation.tripPlanner.testRunner;		

import cucumber.api.CucumberOptions; 
import org.junit.runner.RunWith;		
import cucumber.api.junit.Cucumber;		

@RunWith(Cucumber.class)				
@CucumberOptions(
		features="src\\test\\java\\general\\ahqautomation\\tripPlanner\\features",
		glue= {"general.ahqautomation.tripPlanner.tests"},
		plugin= {"html:target/cucumber-html-report"})						
public class Runner 				
{		

}