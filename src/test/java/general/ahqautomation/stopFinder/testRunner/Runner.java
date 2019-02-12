package general.ahqautomation.stopFinder.testRunner;		

import cucumber.api.CucumberOptions; 
import org.junit.runner.RunWith;		
import cucumber.api.junit.Cucumber;		

@RunWith(Cucumber.class)				
@CucumberOptions(
		features="src\\test\\java\\general\\ahqautomation\\stopFinder\\features",
		glue= {"general.ahqautomation.stopFinder.tests"},
		plugin= {"html:target/cucumber-html-report"})						
public class Runner 				
{		

}