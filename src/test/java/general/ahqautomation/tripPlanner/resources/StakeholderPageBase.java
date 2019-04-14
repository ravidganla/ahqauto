/*
 * 
 *
 * Copyright (c) 2014 Objective Corporation Limited
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Objective Corporation Limited ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with Objective.
 */

package com.objective.kse.ui.base;
import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.RestAssuredConfig.newConfig;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.testng.FileAssert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.fest.assertions.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.JavascriptExecutor;

import com.google.common.base.Function;
import com.objective.ecc.ui.elements.HTMLElement;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.SessionConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;

/**
 * @author Ravi
 */
 
public class StakeholderPageBase {

    private static final String V5_SESSION_COOKIE_NAME = "JSESSIONIDV5";

    // Get a logger
    public static final Logger LOG = LoggerFactory.getLogger(StakeholderPageBase.class);
    protected static final Properties prop = new Properties();
    protected Properties objectLocator = new Properties();
    protected Properties applicationMessages = new Properties();
    public static WebDriver driver;
    public static Integer documentId = null;
    public static String id;
    protected static Integer currentLoggedInUserId = null;
    private static Integer timeout = null;
    protected static Integer rootFolderId = null;
    protected static Integer rootConsultFolderId = null;

    private static Cookie currentCookie = null;
    public static Integer testFolderId = null;
    protected static List<Integer> defaultTemplates = null;
    public Long snapshotId;
    public String commonText = "rednivar";
    public static String adminUser = null;
    public static String adminPassword = null;

    /**
     * Authenticates the user.
     *
     * @param user user to be used
     */
    protected static void authenticateToApi(String userName, String password) {
//    	System.out.println("userName: " + userName + ";password: " + password);
        if (currentLoggedInUserId != null) logoutFromApi();

        Response r = given().body("{ \"username\" : \"" + userName + "\", \"password\" : \"" + password + "\" }").post();

        if (r.statusCode() != 201) {
            LOG.error("There was a problem when authenticating: '{}'", r.getStatusLine());
            Assertions.fail("Login was not successful...");
        } else {
            currentLoggedInUserId = r.jsonPath().getInt("user.id");
            currentCookie = r.getDetailedCookie(V5_SESSION_COOKIE_NAME);
            String cookieHeader = r.getHeader("Set-Cookie");
            if (cookieHeader != null && cookieHeader.length() > 0) {
                RestAssured.config = RestAssured.config().sessionConfig(new SessionConfig("JSESSIONIDV5",
                        cookieHeader.substring(cookieHeader.indexOf("=") + 1, cookieHeader.indexOf(";"))));
            } else {
                LOG.error("There was a problem obtaining the session cookie...");
                Assertions.fail("Login was not successful...");
            }
            LOG.info("Successfully logged in with user '" + userName + "' (userId '{}') from customer '{}'",
                    r.jsonPath().getString("user.id"), r.jsonPath().getString("customer.id"));
        }
    }


    /**
     * Logs out the current logged in user from the system.
     */
    protected static void logoutFromApi() {
        Response r = given().contentType(ContentType.JSON).header("Accept", "application/json").delete();

        if (r.statusCode() != 200) {
            LOG.error("There was a problem when logging out: {}", r.getStatusLine());
            Assertions.fail("Logout was not successful...");
        } else {
            LOG.info("Successfully logged out user {}", currentLoggedInUserId);
            currentLoggedInUserId = null;
        }
    }
    
   /**
     * Wait for an element to be displayed
     */
    public static WebElement waitForElementToBeDisplayed(By element) {

        Long fluentTimeout = 50L;
        return HTMLElement.getWait(Optional.of(fluentTimeout)).until(ExpectedConditions.visibilityOfElementLocated(element));
    }


    /**
     * Pauses the test for the specified time (in milliseconds). Best not to use this in tests unless everything else fails.
     *
     * @param milliseconds amount of time to sleep.
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pageBack() {
    	driver.navigate().back();
    }

    public void pageForward() {
    	driver.navigate().forward();
    }

    public void pageRefresh() {
    	driver.navigate().refresh();
    }
    
	public WebElement checkElement(By pLocator) {
    	Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(20, TimeUnit.SECONDS).pollingEvery(100, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);
		WebElement ele = wait.until(new Function<WebDriver, WebElement>() 
		{
			public WebElement apply(WebDriver driver) {
				return driver.findElement(pLocator);
			}
		});	
		return ele;
	}

	public boolean checkForElement(By pLocator) {
		try {
	    	Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(3, TimeUnit.SECONDS).pollingEvery(100, TimeUnit.MILLISECONDS);
			WebElement ele = wait.until(new Function<WebDriver, WebElement>() 
			{
				public WebElement apply(WebDriver driver) {
					return driver.findElement(pLocator);
				}
			});	
		}
		catch(NoSuchElementException e) {
			return false;
		}
		return true;
	}

	public void clickButton(By pLocator) {
		clickButton(checkElement(pLocator));
	}

	public void clickButton(WebElement pElement) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", pElement);
    	Actions action = new Actions(driver);
    	action.moveToElement(pElement).click().perform();
	}

    /**
     * Checks if an element is present and displayed.
     *
     * @param element the element to be checked.
     * @return true if the element is present and displayed, false otherwise.
     */
    public static boolean isElementVisible(By element) {
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        boolean isVisible = false;
        if (driver.findElements(element).size() > 0) {
            System.out.printf("At least one element [%s] is present ", element);
            int i = 0;
            while (!driver.findElement(element).isDisplayed()) {
                sleep(50);
                i++;
                if (i > 10) break;
            }
            if (driver.findElement(element).isDisplayed()) {
                System.out.println("and is displayed.");
                isVisible = true;
            } else {
                System.out.println("but is not displayed.");
            }
        } else {
            System.out.printf("Element [%s] is not present in the page.\n", element);
        }
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        return isVisible;
    }

    public String getNewWindow() {
        String url = null;
        String originalWindowHandle = driver.getWindowHandle();
        java.util.Set<java.lang.String> windowHandles = driver.getWindowHandles();
        System.out.println(windowHandles);
        int count = windowHandles.size();

        for (String window : windowHandles) {
            if (!window.equals(originalWindowHandle)) {
                driver.switchTo().window(window);
                url = driver.getCurrentUrl();
                driver.switchTo().window(originalWindowHandle);
                System.out.println("current url is: " + url);
            }
            //to go back to original
        }
        return url;
    }

    public String dayAndDate() {
        LocalDateTime now = LocalDateTime.now();
        return now.atZone(ZoneId.of("GMT")).format(DateTimeFormatter.ofPattern("EEE dd/MM/yyyy HH:"));
    }

    public void verifyTextEquals(final String actual, final String expected) {
        assertThat(actual).isEqualToIgnoringCase(expected);
    }

    public <T> void verifyText(T actual, T expected) {
        assertThat(actual).isEqualTo(expected);
    }

    
    public void navigateToEvent(String newEvent) {
        driver.navigate().to(newEvent);
    }


    public WebDriverWait getWait(long timeOut) {
        return new WebDriverWait(driver, timeOut);

    }

    public void tearDown() {
        if (driver != null) {
//            VideoRecordingHelper.stopRecordings();
            driver.quit();
        }
    }

    public void removePermissionFromRole(String roleName, String permission) {
        authenticateToApi("userName", "password");
        expect().statusCode(204).when().delete("people/roles/" + roleName + "/permissions/" + permission);

    }

    public void addPermissionToRole(String roleName, String permission) {
        authenticateToApi("userName", "password");
        given().body("{\"name\":\"" + permission + "\"}").expect().statusCode(201).when().post("people/roles/" + roleName + "/permissions");
    }

    public void loadObjectLocators() {
        InputStream stream = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        stream = loader.getResourceAsStream("objectLocator.properties");
        try {
            objectLocator.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public void setUpSEBrowsers(String browser) {
        InputStream stream = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        stream = loader.getResourceAsStream("user.properties");
        
        try {
            prop.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        stream = loader.getResourceAsStream("appMessagesConstants.properties");
        try {
        	applicationMessages.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (System.getProperty("os.name").equals("Linux")) {
            prop.setProperty("BASE_FILE_PATH", "/tmp/tempSelenium/");
        } else {
            prop.setProperty("BASE_FILE_PATH", "C:\\tempSelenium\\");
        }
        timeout = Integer.parseInt(prop.getProperty("TIMEOUT"));
        
        String baseUrl = prop.getProperty("SE_URL");
        
    	System.out.println("SE_URL: " + baseUrl);
        String [] adminCredentials = prop.getProperty("SUPPORT_QA").split(",");
        adminUser = adminCredentials[0];
        adminPassword = adminCredentials[1];
        
        RestAssured.baseURI = System.getProperty("testEndpoint");
        if (System.getProperty("profileId").equals("dev")) {
            RestAssured.port = Integer.parseInt(System.getProperty("testPort"));
        }
        RestAssured.basePath = System.getProperty("basePath");
        RestAssured.config = newConfig().encoderConfig(encoderConfig().defaultContentCharset("UTF-8"));
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        rootFolderId = Integer.parseInt(System.getProperty("rootFolder"));
        testFolderId = Integer.parseInt(System.getProperty("testFolder"));
        
        
        LOG.info("BASE_URL was set to {}", baseUrl);
        switch (browser) {
            case "chrome":
                setUpChromeBrowserAndCapabilities();
                break;
            case "firefox":
                setUpFirefoxCapabilities();
                break;
            case "ie":
                setUpIEDriverAndCapabilities();
                break;
            default:
                fail("Unsupported browser was requested!");
                break;
        }
        driver.get(baseUrl);
    }

    private void setUpIEDriverAndCapabilities() {
        System.out.println("Tests running on IE");
        System.setProperty("webdriver.ie.driver", "src/test/resources/drivers/IEDriverServer.exe");
        InternetExplorerOptions options = new InternetExplorerOptions();
        options.setCapability("ignoreZoomSetting", true);
        options.setCapability("requiredWindowsFocus", true);
        options.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        options.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
        options.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, false);
        driver = new InternetExplorerDriver(options);
        driver.manage().window().maximize();
    }

    private void setUpChromeBrowserAndCapabilities() {
        System.out.println("Tests Running on Chrome");
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--lang=en-GB");
        driver = new ChromeDriver(options);
    }

    private void setUpFirefoxCapabilities() {
        System.out.println("Tests Running on Firefox");
        System.setProperty("webdriver.gecko.driver", "src/test/resources/drivers/geckodriver.exe");
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setPreference("browser.download.folderList", 2);
        firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
        //firefoxProfile.setPreference("browser.download.dir", prop.getProperty("BASE_FILE_PATH"));
        firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "");
        firefoxProfile.setPreference("browser.download.useDownloadDir", true);
        firefoxProfile.setPreference("security.insecure_password.ui.enabled", false);
        firefoxProfile.setPreference("security.insecure_field_warning.contextual.enabled", false);
        firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setProfile(firefoxProfile);
        driver = new FirefoxDriver(firefoxOptions);
        driver.manage().window().maximize();

    }


    public void setUp() {
        RestAssured.baseURI = System.getProperty("testEndpoint");
        RestAssured.port = Integer.parseInt(System.getProperty("testPort"));
        RestAssured.basePath = System.getProperty("basePath");
        RestAssured.config = newConfig().encoderConfig(encoderConfig().defaultContentCharset("UTF-8"));
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        rootFolderId = Integer.parseInt(System.getProperty("rootFolder"));
        rootConsultFolderId = Integer.parseInt(System.getProperty("rootConsultFolder"));

        LOG.info("BASE_URL was set to {}", RestAssured.baseURI);
        LOG.info("HTTP_PORT was set to {}", RestAssured.port);
        LOG.info("BASE_PATH was set to {}", RestAssured.basePath);
    }
}