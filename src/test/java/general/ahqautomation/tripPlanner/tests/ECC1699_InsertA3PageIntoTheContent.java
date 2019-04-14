package com.objective.ecc.ui.tests.editor;

import com.beust.jcommander.internal.Lists;
import com.objective.ecc.ui.tests.basicPage.DocumentPage;
import com.objective.ecc.ui.tests.dashboard.DashboardPage;
import com.objective.ecc.ui.tests.enums.BoxTypes;
import com.objective.ecc.ui.tests.enums.User;
import com.objective.ecc.ui.tests.userLogin.LoginPage;
import com.objective.ecc.ui.tests.utils.AbstractBase;
import com.objective.ecc.ui.tests.utils.CleanUpHelper;
import com.objective.ecc.ui.tests.utils.DocumentEntity;
import com.objective.ecc.ui.tests.utils.Relationship;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Tests the functionality of the story, "ECC-1699 As a user, I want to insert an A3 page into the document content"
 *
 * @author saranyav on 07/04/2016
 */
public class ECC1699_InsertA3PageIntoTheContent extends AbstractBase {
	private static DocumentEntity testDocument = null;
	private final List<Integer> createdDocuments = Lists.newArrayList();
	private final String image = "bananasinpyjamas";
	private final String spreadsheet = "705 test spreadsheet";
	private String paragraph1 = "A document is a written, drawn, presented or recorded representation of thoughts. " +
			"Originating from the Latin Documentum meaning lesson - the verb doce? means to teach, and is pronounced similarly," +
			" in the past it was";

	private String paragraph2 = "Insert this paragraph inside the A3 Page";

	@BeforeMethod
	public void setUp() {
		super.setUpWithBrowsers("firefox");
		authenticateToApi(User.SUPPORT_QA);
		testDocument = new DocumentEntity(randomString(5), 145848);
		testDocument.addSection(testDocument.regionId(1), Relationship.PARENT);
		testDocument.createAndAddCopy(testDocument.sectionId(0), Relationship.PARENT);
		createdDocuments.add(testDocument.id());
	}

	@AfterMethod
	public void tearDown() {
		super.tearDown();
	}


	@AfterClass
	public void cleanUpTestData() {
		CleanUpHelper.cleanUpDocuments(createdDocuments);
	}

	@Test
	public void testThatYouCanInsertAnA3Page() {
		LoginPage loginPage = new LoginPage(driver);
		DashboardPage dashboardPage = loginPage.loginAs(User.ADMIN_QA);
		DocumentPage documentPage = dashboardPage.openDocument(testDocument.name());
		DocumentEditTab document = documentPage.openDocumentEditTab();
		document.selectCopy(testDocument.contentId(0)).edit();
		document.insertParagraph(paragraph1);
		document.insertA3Page();
		document.saveAndCloseCopy();
	}

	@Test
	public void testThatYouCanSelectTheParagraphAndInsertAnA3Page() {
		LoginPage loginPage = new LoginPage(driver);
		DashboardPage dashboardPage = loginPage.loginAs(User.ADMIN_QA);
		DocumentPage documentPage = dashboardPage.openDocument(testDocument.name());
		DocumentEditTab document = documentPage.openDocumentEditTab();
		document.selectCopy(testDocument.contentId(0)).edit();
		WebElement paragraph = document.insertParagraph(paragraph1);
		document.highlightParagraph(paragraph);
		sleep(1000);
		document.insertA3Page();
		document.checkElementInsideTheOtherElement("//div[@class='a3-page']//p[contains(text(),'" + paragraph1 + "')]");
		document.saveAndCloseCopy();
	}

	@Test
	public void testThatYouCanInsertAnA3PageAndTypeParagraph() {
		LoginPage loginPage = new LoginPage(driver);
		DashboardPage dashboardPage = loginPage.loginAs(User.ADMIN_QA);
		DocumentPage documentPage = dashboardPage.openDocument(testDocument.name());
		DocumentEditTab document = documentPage.openDocumentEditTab();
		document.selectCopy(testDocument.contentId(0)).edit();
		document.insertParagraph(paragraph1);
		WebElement a3Page = document.insertA3Page();
		document.selectCopyItem(a3Page);
		document.switchToMainFrame();
		document.insertParagraph(paragraph2);
		document.saveAndCloseCopy();
	}


	@Test
	public void testThatYouCanInsertTableInsideAnA3Page() {
		LoginPage loginPage = new LoginPage(driver);
		DashboardPage dashboardPage = loginPage.loginAs(User.ADMIN_QA);
		DocumentPage documentPage = dashboardPage.openDocument(testDocument.name());
		DocumentEditTab document = documentPage.openDocumentEditTab();
		document.selectCopy(testDocument.contentId(0)).edit();
		document.insertParagraph(paragraph1);
		WebElement a3Page = document.insertA3Page();
		document.selectCopyItem(a3Page);
		document.switchToMainFrame();
		document.checkElementInsideTheOtherElement("//div[@class='a3-page']//table");
		document.saveAndCloseCopy();
	}

	@Test
	public void testThatYouCanInsertImageInsideAnA3Page() {
		LoginPage loginPage = new LoginPage(driver);
		DashboardPage dashboardPage = loginPage.loginAs(User.ADMIN_QA);
		DocumentPage documentPage = dashboardPage.openDocument(testDocument.name());
		DocumentEditTab document = documentPage.openDocumentEditTab();
		document.selectCopy(testDocument.contentId(0)).edit();
		document.insertParagraph(paragraph1);
		WebElement a3Page = document.insertA3Page();
		document.selectCopyItem(a3Page);
		document.switchToMainFrame();
		document.insertImage(image);
		document.checkElementInsideTheOtherElement("//div[@class='a3-page']//img");
		sleep(1000);
		document.saveAndCloseCopy();
	}

	@Test
	public void testThatYouCanInsertSpreadsheetInsideAnA3Page() {
		LoginPage loginPage = new LoginPage(driver);
		DashboardPage dashboardPage = loginPage.loginAs(User.ADMIN_QA);
		DocumentPage documentPage = dashboardPage.openDocument(testDocument.name());
		DocumentEditTab document = documentPage.openDocumentEditTab();
		document.selectCopy(testDocument.contentId(0)).edit();
		document.insertParagraph(paragraph1);
		WebElement a3Page = document.insertA3Page();
		document.selectCopyItem(a3Page);
		document.switchToMainFrame();
		document.insertSpreadSheet(spreadsheet);
		document.checkElementInsideTheOtherElement("//div[@class='a3-page']//table");
		document.saveAndCloseCopy();
	}

	@Test
	public void testThatYouCanInsertBoxInsideAnA3Page() {
		LoginPage loginPage = new LoginPage(driver);
		DashboardPage dashboardPage = loginPage.loginAs(User.ADMIN_QA);
		DocumentPage documentPage = dashboardPage.openDocument(testDocument.name());
		DocumentEditTab document = documentPage.openDocumentEditTab();
		document.selectCopy(testDocument.contentId(0)).edit();
		document.insertParagraph(paragraph1);
		WebElement a3Page = document.insertA3Page();
		document.selectCopyItem(a3Page);
		document.switchToMainFrame();
		document.insertBox(BoxTypes.ISSUE);
		document.checkElementInsideTheOtherElement("//div[@class='a3-page']//div");
		document.saveAndCloseCopy();
	}

	@Test
	public void testThatYouCanInsertInsideAnA3Page() {
		LoginPage loginPage = new LoginPage(driver);
		DashboardPage dashboardPage = loginPage.loginAs(User.ADMIN_QA);
		DocumentPage documentPage = dashboardPage.openDocument(testDocument.name());
		DocumentEditTab document = documentPage.openDocumentEditTab();
		document.selectCopy(testDocument.contentId(0)).edit();
		document.insertParagraph(paragraph1);
		WebElement a3Page = document.insertA3Page();
		document.selectCopyItem(a3Page);
		document.switchToMainFrame();
		document.insertBox(BoxTypes.ISSUE);
		document.checkElementInsideTheOtherElement("//div[@class='a3-page']//div");
		document.saveAndCloseCopy();
	}

	@Test
	public void testThatYouCanRemoveAnA3Page() {
		LoginPage loginPage = new LoginPage(driver);
		DashboardPage dashboardPage = loginPage.loginAs(User.ADMIN_QA);
		DocumentPage documentPage = dashboardPage.openDocument(testDocument.name());
		DocumentEditTab document = documentPage.openDocumentEditTab();
		document.selectCopy(testDocument.contentId(0)).edit();
		WebElement paragraph = document.insertParagraph(paragraph1);
		document.highlightParagraph(paragraph);
		sleep(1000);
		document.insertA3Page();
		document.removeA3page();
		document.saveAndCloseCopy();
	}
}
