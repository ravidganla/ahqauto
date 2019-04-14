/*
 * Dashboard.java 16/03/15
 *
 * Copyright (c) 2015 Objective Corporation Limited
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Objective Corporation Limited ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with Objective.
 */
package com.objective.ecc.ui.tests.editor;

import com.objective.ecc.ui.elements.Button;
import com.objective.ecc.ui.elements.Dialog;
import com.objective.ecc.ui.elements.HTMLElement;
import com.objective.ecc.ui.elements.HTMLElement.SearchBy;
import com.objective.ecc.ui.elements.Paragraph;
import com.objective.ecc.ui.elements.annotations.KSFindBy;
import com.objective.ecc.ui.tests.basicPage.DocumentPage;
import com.objective.ecc.ui.tests.documentSettings.DocumentSettingsTab;
import com.objective.ecc.ui.tests.editor.document.DocumentEditor;
import com.objective.ecc.ui.tests.editor.documentStructureTree.DocumentStructure;
import com.objective.ecc.ui.tests.enums.*;
import com.objective.ecc.ui.tests.utils.CleanUpHelper;
import com.objective.ecc.ui.tests.utils.DocumentEntity;
import com.objective.ecc.ui.tests.utils.DocumentScreen.EditorControls.*;
import com.objective.ecc.ui.tests.utils.DocumentScreen.LeftPanel.LeftPanelHelper;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

/**
 * @author dragosd.
 */
public class DocumentEditTab extends DocumentPage {
    public static String elementId;
    static String lastCreatedCopyName = null;
    DocumentEntity entity;
    By rightPanelLocator = By.cssSelector("div.actions-panel__content");
    By htmlEditorIFrame = By.cssSelector("iframe.html-editor__iframe");
    By tocNavigatorLocator = By.cssSelector(".navigator-item__toc");
    By copyEditButton = By.cssSelector("div#content-edit");
    By contentEditButton = By.cssSelector("div#content-edit");
    By contentUnlockButton = By.cssSelector("div#unlock");
    //	By tableRows = By.id("ecc/view/control/NumberSpinner_0_textBox");
//	By tableColumns = By.id("ecc/view/control/NumberSpinner_1_textBox");
    By tableRows = By.ByTagName.cssSelector(".dialog--displaying .dialog__row:nth-of-type(1) .dijitInputContainer input");
    By tableColumns = By.ByTagName.cssSelector(".dialog--displaying .dialog__row:nth-of-type(2) .dijitInputContainer input");
    By confirmTableInsertion = By.cssSelector(".dialog--displaying .insert-table__confirm-button");
    By editorConfirmContentChangesButton = By.cssSelector("div.editor__item--editing div.editor__item__button-group > button.editor__item__confirm-button");
    By editorDiscardContentChangesButton = By.cssSelector("div.editor__item--editing div.editor__item__button-group > button.editor__item__discard-button");
    By editorSaveContentChangesButton = By.cssSelector("div.editor__item--editing div.editor__item__button-group > button.editor__item__save-button");
    By confirmInsertionButton = By.cssSelector("#confirm");
    By discardInsertionButton = By.cssSelector("#discard");
    By notificationMessage = By.cssSelector("Notification--visible div.notification__message");
    By notificationMessageDiscardButton = By.cssSelector("button.notification__button[title = Discard]");
    By newNavigatorItemLocator = By.cssSelector(".navigator-item--selected");

    By contentNameLocator = By.cssSelector(".dialog--displaying .dijitInputContainer input");
    By contentInsertButton = By.cssSelector(".dialog--displaying button");
    By newContentEditorItemLocator = By.cssSelector(".editor__item--selected.editor__item__copy");

    By tableCaptionInputLocator = By.cssSelector("div.actions-pane__buttons > div:first-child div.button-control-table-caption div.string-capture-property__text-capture div.value-capture__edit-container input.dijitInputInner");
    By tableCaptionAcceptButton = By.cssSelector("div.actions-pane__buttons > div:first-child div.button-control-table-caption div.string-capture-property__text-capture div.value-capture__buttons button.value-capture__accept-btn");
    By tableCaptionLocator = By.cssSelector("table[data-test='table-4x4'] > caption");
    By tableLocator = By.cssSelector("table[data-test='table-4x4']");
    By recoverButtonLocator = By.cssSelector("div.editor__item--recovering div.editor-recovery-window__bottom > button.editor-recovery-window__save");
    By recoverConfirmButton = By.cssSelector("div.editor__item__copy--content-loaded button.editor__item__confirm-button");
    By headerCellLocator = By.cssSelector("input.header-cell-property__checkbox");
    By cellFontColorRGBLocator = By.cssSelector("div.dialog--displaying form.colour-picker__mode--rgb div.colour-picker__colour-values input.dijitInputInner");
    By cellFontColorUpdateButton = By.cssSelector("div.dialog--displaying button.colour-dialog__confirm-button");
    By expectedFontColorLocator = By.cssSelector("div.button-control-table-cell-colour span.colour-property__value");
    By expectedBackgroundColorLocator = By.cssSelector("div.button-control-table-cell-background-colour span.colour-property__value");
    By fontCellLocator = By.cssSelector("td[data-test='cell-3x1']");
    By backgroundCellLocator = By.cssSelector("td[data-test='cell-3x1']");
    By allBorderButton = By.id("cell-border-all");
    By cellBorderColorRLocator = By.cssSelector("div.dialog--displaying form.colour-picker__mode--rgb div.colour-picker__colour-values > div:first-child input.dijitInputInner");
    By cellBorderColorGLocator = By.cssSelector("div.dialog--displaying form.colour-picker__mode--rgb div.colour-picker__colour-values > div:nth-child(2) input.dijitInputInner");
    By cellBorderColorBLocator = By.cssSelector("div.dialog--displaying form.colour-picker__mode--rgb div.colour-picker__colour-values > div:nth-child(3) input.dijitInputInner");
    By cellBorderNone = By.id("cell-border-none");
    By splitCellRowsLableLocator = By.xpath("//div[contains(@class,'dialog--displaying')]//label[text()='Rows']");
    By splitCellColumnLableLocator = By.xpath("//div[contains(@class,'dialog--displaying')]//label[text()='Columns']");
    By splitCellConfirmButton = By.cssSelector("div.dialog--displaying button.split-cells__confirm-button");
    By splitCellInputLocator = By.cssSelector("div.dialog--displaying div.dijitFocused input.dijitInputInner");
    By TableWidthMetricUnitLocator = By.cssSelector("div.button-control-table-width table:nth-child(3)");
    By TableWidthValueLocator = By.className("number-spinner");
    By TableWidthInputLocator = By.cssSelector("div.button-control-table-width input[title='Amount']");
    By tableAllCellsLocator = By.cssSelector("table[data-test='table-4x4'] tr td");
    By rowHeightMetricUnitLocator = By.cssSelector("div.button-control-table-row-height table:nth-child(3)");
    By rowHeightValueLocator = By.cssSelector("div.button-control-table-row-height div.number-spinner");
    By rowHeightInputLocator = By.cssSelector("div.button-control-table-row-height input[title='Amount']");
    By columnWidthMetricUnitLocator = By.cssSelector("div.button-control-column-width table:nth-child(3)");
    By columnWidthValueLocator = By.cssSelector("div.button-control-column-width div.number-spinner");
    By columnWidthInputLocator = By.cssSelector("div.button-control-column-width input[title='Amount']");
    By changeCaseDialogLocator = By.className("dialog--displaying");
    By listStyleButtons = By.cssSelector("div.actions-pane--expanded[data-dojo-attach-point='basicPane']");

    // Box elements locator
    By dialogLocator = By.cssSelector(".dialog--displaying");
    By dialogCloseLocator = By.cssSelector(".dialog--displaying .dialog__header__close");
    By boxTitleLocator = By.cssSelector("input[title='Box Name']");
    By boxPrefixLocator = By.ByTagName.cssSelector("input[title='Box Prefix']");
    By insertBoxDialogLocator = By.cssSelector("div.dialog--displaying div.dialog__content");
    By insertBoxButton = By.cssSelector(".dialog--displaying .insert-box__confirm-button");
    By reuseContentDialogLocator = By.cssSelector("div.dialog--displaying div.reusable-content-action-component");
    By reuseInsertButton = By.cssSelector("button.reusable-content-action-component__confirm-button");
    By imageDialogLocator = By.cssSelector("div.dialog--displaying div.insert-image__content");
    By imageInsertButton = By.cssSelector(".dialog--displaying .insert-link__insert-btn");
    By insertSpreadSheetButton = By.cssSelector("button.insert-spreadsheet__confirm-button");
    By paragraphLocator = By.id("div.editor__item__copy-view > p:first-child");
    By numberListLocator = By.cssSelector("div.editor__item__copy-view ol");
    By bulletListLocator = By.cssSelector("div.editor__item__copy-view ul");
    By boxLocator = By.cssSelector("div.editor__item__copy-view > div.box");
    By tableItemLocator = By.cssSelector("div.editor__item__copy-view > table:not([data-ecc-live-ref])");
    By tableCellLocator = By.cssSelector("div.editor__item__copy-view > table:not([data-ecc-live-ref]) tr >td ");
    By imageLocator = By.cssSelector("div.editor__item__copy-view > img");
    By imageAltTextLocator = By.cssSelector("[title='Image Alt Text'] .string-capture-property__text-capture .dijitInputContainer input");
    By imageAltTextAcceptButton = By.cssSelector("[title='Image Alt Text'] .string-capture-property__text-capture .value-capture__accept-btn");
    By imageAltTextValueLocator = By.cssSelector("[title='Image Alt Text'] .string-capture-property__text-capture .value-capture__view");
    By imageCaptionTextLocator = By.cssSelector("[title='Image caption'] .dijitInputContainer input");
    By imageCaptionAcceptButton = By.cssSelector("[title='Image caption'] .string-capture-property__text-capture .value-capture__accept-btn");
    By imageCaptionValueLocator = By.cssSelector("[title='Image caption'] .string-capture-property__text-capture .value-capture__view");
    By imageWidthTextLocator = By.cssSelector(".button-control-image-size > div:nth-child(2) .dijitInputContainer input");
    By imageHeightTextLocator = By.cssSelector(".button-control-image-size > div:nth-child(3) .dijitInputContainer input");

    By reusableContentLocator = By.cssSelector("div.editor__item__copy-view > div[data-ecc-linked-ref]");
    By spreadSheetLocator = By.cssSelector("div.editor__item__copy-view > table[data-ecc-live-ref]");
    By dialogInputBox = By.cssSelector(".dialog--displaying .dijitInputContainer input");
    By commentDialogLocator = By.cssSelector("div.dialog--displaying");
    By commentAddButton = By.cssSelector("div.dialog--displaying button.prompt-string__confirm-btn");
    By commentForElementLocator = By.cssSelector("div.comments-component div.ui-widget-content div.dgrid-row");
    By documentTitleNavigatorLocator = By.cssSelector("div.document-navigator__title");

    // Layout
    By pageBreakLocator = By.cssSelector("hr.page-break");
    By columnBreakLocator = By.cssSelector("hr.column-break");
    By columnSpanLocator = By.cssSelector("div.column-span");
    By a3PageLocator = By.cssSelector("div.a3-page");
    By a3FullPageLocator = By.cssSelector("div.a3-full-page");
    By fullPageLocator = By.cssSelector("div.full-page");
    By landscapeLocator = By.cssSelector("div.landscape");

    // Tasks
    By taskDialogHeaderLocator = By.cssSelector("div.dialog--displaying span.dialog__header__title");
    By taskTitleLocator = By.cssSelector("div.dialog--displaying input[name='title']");
    By taskInstructionLocator = By.cssSelector("div.dialog--displaying textarea[name='description']");
    By taskDueDateLocator = By.cssSelector("div.dialog--displaying div.dijitDateTextBox div.dijitInputField  input:not([name='dueDate'])");
    By taskDueTimeLocator = By.cssSelector("div.dialog--displaying div.dijitTimeTextBox div.dijitInputField  input:not([name='dueTime'])");
    By canFailLoctor = By.xpath("//label[text()='Can fail']");
    //	String uniqueName = findIdOfClass("div.create-task");
//	By canFailInputLocator = By.cssSelector("input#" + uniqueName + "-canfail");
//	By searchGroupLocator = By.cssSelector("input#" + uniqueName + "_searchGroups");
//	By searchUsersLocator = By.cssSelector("input#" + uniqueName + "_searchUsers");
//	By searchUsersDropdown = By.cssSelector("div#widget_" + uniqueName + "_searchUsers_dropdown");
//	By searchGroupsDropdown = By.cssSelector("div#widget_" + uniqueName + "_searchGroups_dropdown");
    By addUserButton = By.cssSelector("[data-dojo-attach-point='_reassignUserOptions'] span.create-task__add-user-btn");
    By addGroupButton = By.cssSelector("[data-dojo-attach-point='_reassignGroupOptions'] span.create-task__add-user-btn");
    By taskUserList = By.cssSelector("span.create-task__user");
    By taskGroupList = By.cssSelector("span.create-task__group");
    By createTaskAddButton = By.cssSelector("button.create-task__confirm-btn");
    By actionPaneTitleBarLocator = By.cssSelector("div#tasks-pane > div.actions-pane__title-bar");
    By allTaskContentLocator = By.cssSelector("div.button-control-tasks-component div.ui-widget-content");
    By taskContentLocator = By.cssSelector("div.button-control-tasks-component div.ui-widget-content div.dgrid-row-even");
    By taskComponentTileLocator = By.cssSelector(".dgrid-row-even header.tasks-component__title");
    By taskInstructionValueLocator = By.cssSelector(".dgrid-row-even header.tasks-component__title + section > p");
    By taskNotStartedLocator = By.cssSelector(".dgrid-row-even section > .tasks-component__tag--not-started");

    // Numbering
    By autoNumberingLocator = By.cssSelector(".dijitMenuActive [aria-label='Auto ']");
    By manualNumberingLocator = By.cssSelector(".dijitMenuPopup .dijitMenuActive [aria-label='Manual ']");
    By noneNumberingLocator = By.cssSelector(".dijitMenuActive [aria-label='None ']");
//	By manualNumberingInputLocator = By.cssSelector(".actions-panel__content div#action-pane_0 .numbering-property .dijitInputContainer > input");


    //	 Verification Metadata form
    By verificationDialogLocator = By.cssSelector(".dialog--displaying.verifications-dialog");
    //	String checkboxUniqueName = findIdOfClass(".verifications-dialog fieldset");
    By checksCheckboxesLocator = By.cssSelector(".dialog--displaying [type='checkbox']");
    By verifySaveButton = By.cssSelector(".dialog--displaying footer > span:first-child [data-dojo-attach-event='ondijitclick:__onClick']");
    By passwordLocator = By.cssSelector(".dialog--displaying input[name='password']");
    By verifyButton = By.cssSelector(".dialog--displaying footer > span:nth-child(2) [data-dojo-attach-event='ondijitclick:__onClick']");
    By verifyMessageLocator = By.cssSelector(".msg-verified");

    /*clickDeleteButton notification elements locator*/
    By notificationDeleteButton = By.cssSelector(".notification [title='Delete']");
    By notificationCancelButton = By.cssSelector(".notification [title='Cancel']");

    // Version Control elements locator
    By versionTitleBarLocator = By.cssSelector("#versions-pane");
    By contentVersions = By.cssSelector(".versions-component .dgrid-content .dgrid-row");
    By version = By.cssSelector(".versions-component .dgrid-content .dgrid-row dl dd");
    By insertBoxTypeArrowButton = By.cssSelector(".dialog--displaying table:nth-of-type(1) .dijitArrowButton");
    By insertBoxNumberingArrowButton = By.cssSelector(".dialog--displaying table:nth-of-type(2) .dijitArrowButton");

    //	Dynamic Control elements locator
    By documentTypeDialogLocator = By.ByTagName.cssSelector("div.dialog--displaying");
    By stakeholderDynamicRadioButton = By.cssSelector(".dialog--displaying [value='stakeholder_dynamic']");
    By assetDynamicRadioButton = By.cssSelector(".dialog--displaying [value='asset_dynamic']");
    By notDynamicRadioButton = By.cssSelector(".dialog--displaying [value='none']");
    By okButton = By.cssSelector(".dialog--displaying [type='submit']");
    By dynamicTypeLabel = By.cssSelector("#dynamic-type-value");

    @KSFindBy(searchby = SearchBy.ID, value = "action-pane_1")
    private InsertPanel insertPanel;


    @KSFindBy(searchby = HTMLElement.SearchBy.CLASS_NAME, value = "dialog--displaying")
    private Dialog dialogElement;

    @KSFindBy(searchby = SearchBy.CSS_SELECTOR, value = ".notification--visible [title='OK']")
    private Button buttonNotificationOk;

    @KSFindBy(searchby = SearchBy.CSS_SELECTOR, value = ".ecc-editor p")
    private Paragraph paragraph;


    BasicPanel basicPanel;

    public DocumentEditTab() {

    }

    public DocumentEditTab(WebDriver driver) {
        waitForElementToBeDisplayed(headerLogoLocator);
        waitForElementToBeDisplayed(headerUserLocator);
//		waitForElementToBeDisplayed(globalMenuLocator);
        waitForElementToBeDisplayed(editTabLocator);
//		waitForElementToBeDisplayed(structureTabLocator);
//		waitForElementToBeDisplayed(tasksTabLocator);
        waitForElementToBeDisplayed(settingsTabLocator);
        waitForElementToBeDisplayed(metadataTabLocator);
        waitForElementToBeDisplayed(rightPanelLocator);

        // Close the walk-me dialogue
        if (isElementVisible(By.cssSelector("div#walkme-overlay-all"))) {
            waitForElementToBeDisplayed(buttonWalkMeClose);
            driver.findElement(buttonWalkMeClose).click();
        }



        // Check that we're on the right page.
        if (!driver.getTitle().startsWith("Objective Keystone - Editor:")) {
            System.out.println("Title" + driver.getTitle());
            throw new IllegalStateException("This is not the edit page");
        }

        // Check that the document name is not empty
        assertThat(LeftPanelHelper.documentName().getText()).isNotEmpty();
        assertThat(new LeftPanelHelper().getDocTOCText()).isEqualTo("Table of Contents");
    }


    public void closeWalkMe() {
        if (isElementVisible(By.cssSelector(".wm-outer-div"))) {
            waitForElementToBeDisplayed(buttonWalkMeClose);
            driver.findElement(buttonWalkMeClose).click();
        }

    }

    public DocumentEditTab expandDocumentStructureTree() {
        if (isElementVisible(By.cssSelector("i.document-navigator__expand-all__down"))) {
            WebElement expandAll = driver.findElement(documentTreeExpandAll);
            expandAll.click();
        }
        // Close walk me
        sleep(1000);
        closeWalkMe();
        // Check that the document structure matches the API
        List<String> apiStructureElements = DocumentEntity.getDocumentElements(CleanUpHelper.documentId());
        List<String> uiStructureElements = new ArrayList<>();
        for (WebElement uiElementsId : driver.findElements(documentStructureElements)) {
            uiStructureElements.add(uiElementsId.getAttribute("data-ecc-id"));
        }
        assertThat(uiStructureElements).containsAll(apiStructureElements);

        // close the recovery Window
        if (isElementVisible(By.cssSelector("div.editor__item--recovering "))) {
            waitForElementToBeDisplayed(recoverButtonLocator);
            driver.findElement(recoverButtonLocator).click();
            driver.findElement(recoverConfirmButton).click();
        }
        return this;
    }

    /**
     * @param regionId    the regionId that is existing region in the document
     * @param regionTitle the regionTitle is a name of the new region to be created in the document
     * @return the id of the new region
     */
    public String insertNewRegionToRegion(String regionId, String regionTitle) {
        // get the new version of the document
        driver.navigate().refresh();
        selectNavigatorItem(regionId);
        new InsertPanel().clickRegionButton();
        String newRegionId;
        newRegionId = nameNavigatorNewItemInTheDocument(regionTitle, "region");
        return newRegionId;
    }

    /**
     * @param title the title of the new item to be created in the document
     * @param item  the item
     *              Section | Region | Subsection
     * @return the id of the created item
     */
    public String nameNavigatorNewItemInTheDocument(String title, String item) {
        waitForElementToBeDisplayed(confirmInsertionButton);
        waitForElementToBeDisplayed(discardInsertionButton);
        driver.findElement(By.cssSelector(".editor__item--selected.editor__item__" + item)).isDisplayed();
        WebElement newRegionNameElement = driver.findElement(By.cssSelector(".editor__item--selected.editor__item__" + item + " .editor__item__content"));
        newRegionNameElement.click();
        newRegionNameElement.clear();
        newRegionNameElement.sendKeys(title);
        driver.findElement(confirmInsertionButton).click();
        WebElement newRegion = driver.findElement(newNavigatorItemLocator);
        assertThat(newRegion.getText()).contains(title);
        String newItemId;
        newItemId = newRegion.getAttribute("data-ecc-id");
        return newItemId;
    }

    /**
     * Inserts the new section under the Region
     *
     * @param regionId     the region id that is existing in the document
     * @param sectionTitle the sectionTitle is the name of the new section to be created
     * @return the id of the new section
     */

    public String insertNewSectionToRegion(String regionId, String sectionTitle) {
        expandDocumentStructureTree();
        switchToMainFrame();
        selectNavigatorItem(regionId);
        new InsertPanel().clickSectionButton();
        String newSectionId;
        newSectionId = nameNavigatorNewItemInTheDocument(sectionTitle, "section");
        return newSectionId;
    }

    /**
     * Inserts the new section under the section
     *
     * @param sectionId    the id of the section that is exist in the document
     * @param sectionTitle the sectionTitle is the name of the new section to be created
     * @return the id of the new section
     */

    public String insertNewSectionIntoSection(String sectionId, String sectionTitle) {
        expandDocumentStructureTree();
        selectNavigatorItem(sectionId);
        new InsertPanel().clickSectionButton();
        String newSectionId;
        newSectionId = nameNavigatorNewItemInTheDocument(sectionTitle, "section");
        return newSectionId;
    }

    /**
     * Inserts the new sub section under the section
     *
     * @param sectionId       the id of the section that is exist in the document
     * @param subSectionTitle the subSectionTitle is the name of the new section to be created
     * @return the id of the new sub section
     */
    public String insertNewSubSectionIntoSection(String sectionId, String subSectionTitle) {
        expandDocumentStructureTree();
        selectNavigatorItem(sectionId);
        new InsertPanel().clickSubsectionButton();
        String newSectionId;
        newSectionId = nameNavigatorNewItemInTheDocument(subSectionTitle, "section");
        WebElement newSubSectionElement = driver.findElement(By.cssSelector(".navigator-item[data-ecc-id='" + newSectionId + "']"));
        assertThat(newSubSectionElement.getAttribute("data-ecc-parentid")).isEqualTo(sectionId);
        return newSectionId;
    }


    /**
     * Inserts the new content item into the section and content
     *
     * @param parentId     the id of the parent that is exist in the document. It could be
     *                     Section | Content
     * @param contentTitle the contentTitle is the name of the new content to be created
     * @return the id of the new content
     */
    public String insertNewContentIntoParentItem(String parentId, String contentTitle) {
        expandDocumentStructureTree();
        selectNavigatorItem(parentId);
        new InsertPanel().clickContentButton();
        WebElement contentNameElement = driver.findElement(contentNameLocator);
        contentNameElement.sendKeys(contentTitle);
        driver.findElement(contentInsertButton).click();
        driver.findElement(newContentEditorItemLocator).isDisplayed();
        new BasicPanel().clickConfirmChangesButton();
        WebElement newContent = driver.findElement(newNavigatorItemLocator);
        assertThat(newContent.getText()).contains(contentTitle);
        String contentId;
        contentId = newContent.getAttribute("data-ecc-id");
        return contentId;
    }

    /**
     * Edit the content from the section and save the content
     *
     * @param contentId the id of the content
     */
    public void editContentInSection(String contentId) {
        expandDocumentStructureTree();
        selectNavigatorItem(contentId);
        new BasicPanel().clickEditButton();
        switchToEditorFrame();
        WebElement fistPara = driver.findElement(By.cssSelector("p"));
        Actions action = new Actions(driver);
        action.sendKeys("Paragraph" + " " + randomString(10)).build().perform();
        action.sendKeys(Keys.RETURN).build().perform();
        switchToMainFrame();
        sleep(100);
        new BasicPanel().clickConfirmChangesButton();
    }

    /**
     * makes the content read only
     *
     * @param contentId the id of the content
     */

    public void readOnly(String contentId) {
        expandDocumentStructureTree();
        selectNavigatorItem(contentId);
        new BasicPanel().clickReadOnlyButton();
        WebElement readOnlyContent = driver.findElement(By.cssSelector("[data-ecc-id='" + contentId + "'] [data-dojo-attach-point='_lockTitle']"));
        assertThat(readOnlyContent.getText()).startsWith("Content is read-only");
    }

    public void removeReadOnly(String contentId) {
        expandDocumentStructureTree();
        selectNavigatorItem(contentId);
        new BasicPanel().clickReadOnlyButton();
        List<WebElement> readOnlyContent = driver.findElements(By.cssSelector("[data-ecc-id='\"+ contentId +\"'] [data-dojo-attach-point='_lockTitle']"));
        assertThat(readOnlyContent.size()).isZero();
    }

    public DocumentEditTab insertSpreadSheet(String sheetName) {
        switchToMainFrame();
        new InsertPanel().clickInsertspreadsheetButton();
        waitForElementToBeDisplayed(insertBoxDialogLocator);
        WebElement searchBox = driver.findElement(dialogInputBox);
        highlightElement(searchBox);
        searchBox.clear();
        searchBox.sendKeys(sheetName);
        waitForElementToBeDisplayed(By.id("widget_ecc/view/dijit/SearchBox_1_dropdown"));
        sleep(10);
        performAction(Keys.DOWN, Keys.TAB);
        // Expand the spread sheet and select relevant sheet
        sleep(1000);
        if (isElementVisible(By.cssSelector(".spreadsheet-tree__tree .dijitTreeExpandoClosed"))) {
            if (isElementVisible(By.cssSelector(".spreadsheet-tree__tree .dijitTreeExpandoClosed"))) {
                WebElement expandSheet = driver.findElement(By.cssSelector(".spreadsheet-tree__tree .dijitTreeExpandoClosed"));
                expandSheet.click();
                driver.findElement(By.cssSelector("span.spreadsheet-tree--icon-range")).click();
            } else if (isElementVisible(By.cssSelector("span.spreadsheet-tree--icon-range"))) {
                driver.findElement(By.cssSelector("span.spreadsheet-tree--icon-range")).click();
            } else {
                fail("sheet not available");
            }
        }
        //click on the sheet
        driver.findElement(insertSpreadSheetButton).click();
        switchToEditorFrame();
        performAction(Keys.LEFT, Keys.RETURN);
        driver.findElement(By.cssSelector("table[data-ecc-live-ref]")).isDisplayed();
        switchToMainFrame();
        return this;
    }

    public DocumentEditTab addCopyInSection(String sectionName) {
        expandDocumentStructureTree();
        switchToMainFrame();
        WebElement parentItem = driver.findElement(By.xpath("//div[@data-ecc-id='" + DocumentEntity.getSectionIdFromTitle(CleanUpHelper.documentId(), sectionName) + "']"));
        parentItem.click();
        new InsertPanel().clickContentButton();
        // Give a name to the inserted content
        WebElement contentNameField = driver.findElement(By.cssSelector("div.dialog--displaying > div.dialog__container div.dijitInputContainer > input"));
        lastCreatedCopyName = randomString(8);
        contentNameField.sendKeys(lastCreatedCopyName);
        driver.findElement(By.cssSelector("div.dialog--displaying > div.dialog__container button[data-dojo-attach-point = _confirm]")).click();
        waitForElementToBeDisplayed(editorConfirmContentChangesButton);
        waitForElementToBeDisplayed(editorDiscardContentChangesButton);
        return this;
    }

    public DocumentEditTab selectCopy(String copyName) {
        expandDocumentStructureTree();

        WebElement copyItem = driver.findElement(By.xpath("//div[@data-ecc-id=" + DocumentEntity.getCopyIdFromTitle(CleanUpHelper.documentId(), copyName) + "]"));
        copyItem.click();
        // I should wait for the javaScript to end?
        return this;
    }

    public DocumentEditTab selectCopy(Long documentCopyId) {
        expandDocumentStructureTree();

        WebElement copyItem = driver.findElement(By.cssSelector(".navigator-item[data-ecc-id='" + documentCopyId + "']"));
        copyItem.click();
        // I should wait for the javaScript to end?
        return this;
    }

    /**
     * Selects the Item from the document structure tree on the editor tab
     *
     * @param itemId the id of the item
     *               TOC | Section | Region | Copy | SubSection
     */

    public DocumentEditTab selectNavigatorItem(String itemId) {
        expandDocumentStructureTree();
        WebElement navigatorItem = driver.findElement(By.cssSelector(".navigator-item[data-ecc-id='" + itemId + "']"));
        navigatorItem.click();
        return this;
    }

    /**
     * Edit the name of the navigator item in the document on the edit tab
     *
     * @param itemId        the id of the item to be edited
     * @param navigatorItem the navigatorItem item in the document, it could be
     *                      Region |Section |SubSection as Section
     * @param newName       new name of the item
     */
    public DocumentEditTab editNavigatorItemName(String itemId, String newName, String navigatorItem) {
        selectNavigatorItem(itemId);
        new BasicPanel().clickEditButton();
        nameNavigatorNewItemInTheDocument(newName, navigatorItem);
        return this;
    }

    public DocumentEditTab deleteNavigatorItem(String itemId) {
        selectNavigatorItem(itemId);
        new BasicPanel().clickDeleteButton();
        waitForElementToBeDisplayed(notificationDeleteButton);
        waitForElementToBeDisplayed(notificationCancelButton);
        driver.findElement(notificationDeleteButton).click();
        List<WebElement> deletedElement = driver.findElements(By.cssSelector("[data-ecc-id='" + itemId + "']"));
        assertThat(deletedElement.size()).isEqualTo(0);
        return this;
    }

    public DocumentEditTab deleteSection(String documentSectionId) {
        selectNavigatorItem(documentSectionId);
        new BasicPanel().clickDeleteButton();
        waitForElementToBeDisplayed(notificationDeleteButton);
        waitForElementToBeDisplayed(notificationCancelButton);
        driver.findElement(notificationDeleteButton).click();

//		Check that the section deleted
        entity = new DocumentEntity();
        entity.checkSectionDeleted(documentSectionId);
        return this;
    }

    public DocumentEditTab selectDocument() {
        expandDocumentStructureTree();

        WebElement copyItem = driver.findElement(documentTitleNavigatorLocator);
        copyItem.click();
        // I should wait for the javaScript to end?
        return this;
    }

    public DocumentEditTab setEditorHtmlTo(String htmlText) {

        String script = "var iframe = document.getElementsByClassName('html-editor__iframe')[0];" +
                "var contentDoc = (iframe.contentDocument || iframe.contentWindow.document);" +
                "var contentWindow = iframe.contentWindow;" +
                "var body = contentDoc.getElementsByClassName('ecc-editor')[0];" +
                "body.innerHTML= '" + htmlText + "';";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(script);
        return this;
    }

    public DocumentEditTab addCopyItems() {
        Actions action = new Actions(driver);
        action.sendKeys("Paragraph" + " " + randomString(10)).build().perform();
        action.sendKeys(Keys.RETURN).build().perform();
        insertBox(BoxTypes.BOX);
        insertReuseContent("concordance");
        insertSpreadSheet("705 test spreadsheet");
        insertImage("Desert.jpg");
        return this;
    }

    public WebElement insertParagraph(String paragraphText) {
        switchToEditorFrame();
        sleep(1000);
        Actions action = new Actions(driver);
        action.sendKeys(Keys.RETURN).build().perform();
        action.sendKeys(paragraphText).build().perform();
        performAction(Keys.RETURN);
        WebElement paragraph = driver.findElement(By.xpath("//*[contains(text(),'" + paragraphText + "')]"));
        paragraph.isDisplayed();
        switchToMainFrame();
        return paragraph;
    }

    public WebElement nextParagraphElement(String paraText) {
        switchToEditorFrame();
        WebElement paragraph = driver.findElement(By.xpath("//*[contains(text(),'" + paraText + "')]/following-sibling::p"));
        paragraph.isDisplayed();
        switchToMainFrame();
        return paragraph;
    }

    public DocumentEditTab edit() {
        // Unlock the content if it's locked
        if (isElementVisible(contentUnlockButton)) {
            new BasicPanel().clickUnlockButtonIfVisible();
            waitForElementToBeDisplayed(contentEditButton);
        }
        new BasicPanel().clickEditButton();
        waitForElementToBeDisplayed(htmlEditorIFrame);
        return this;
    }

    public DocumentEditTab insertParagraphInEditor() {
        Actions action = new Actions(driver);
        action.sendKeys("Paragraph" + " " + randomString(10)).build().perform();
        action.sendKeys(Keys.RETURN).build().perform();
        return this;
    }

    /**
     * Inserts the box into the editor
     *
     * @param types type of the box item
     *              Policy |Statement |Issue |Option |Question |Site | Box
     * @return return the box Web element
     */
    public WebElement insertBox(BoxTypes types) {
        switchToMainFrame();
        sleep(100);
        new InsertPanel().clickInsertBoxButton();
        waitForElementToBeDisplayed(insertBoxDialogLocator);
        sleep(1000);
        Actions action = new Actions(driver);
        action.sendKeys("boxname").build().perform();
        action.clickAndHold().release();
        driver.findElement(insertBoxTypeArrowButton).click();
        driver.findElement(By.cssSelector(".dijitMenuActive [aria-label*='" + types.getBoxTypes() + "']")).click();
        driver.findElement(insertBoxButton).click();
        sleep(1000);
        //move to next line ine the box
        action.sendKeys(Keys.RETURN).sendKeys(Keys.RETURN).build().perform();
        switchToEditorFrame();
        WebElement box = driver.findElement(By.cssSelector("[data-ecc-number='" + (types.getBoxTypes().toLowerCase()).trim() + "']"));
        box.isDisplayed();
        switchToMainFrame();
        return box;
    }

    public DocumentEditTab insertReuseContent(String contentName) {
        switchToMainFrame();
        new InsertPanel().clickInsertReuseContentButton();
        waitForElementToBeDisplayed(reuseContentDialogLocator);
        WebElement searchBox = driver.findElement(dialogInputBox);
        highlightElement(searchBox);
        searchBox.clear();
        searchBox.sendKeys(contentName);
        waitForElementToBeDisplayed(By.id("widget_ecc/view/dijit/SearchBox_4_dropdown"));
        performAction(Keys.DOWN, Keys.TAB);
        driver.findElement(reuseInsertButton).click();
        switchToEditorFrame();
        performAction(Keys.DOWN, Keys.ARROW_LEFT, Keys.ARROW_DOWN, Keys.RETURN);
        driver.findElement(By.cssSelector("div[data-ecc-ui-name='" + contentName + "']")).isDisplayed();
        switchToMainFrame();
        return this;
    }

    public WebElement insertImage(String imageName) {
        Actions actions = new Actions(driver);
        actions.doubleClick();
        switchToMainFrame();
        new InsertPanel().clickInsertImageButton();
        waitForElementToBeDisplayed(imageDialogLocator);
        WebElement searchBox = driver.findElement(dialogInputBox);
        highlightElement(searchBox);
        searchBox.clear();
        searchBox.sendKeys(imageName);
        performAction(Keys.BACK_SPACE);
        sleep(100);
        WebElement searchList = driver.findElement(By.cssSelector(".search-box-menuPopup"));
//		waitForElementToBeDisplayed(By.id("widget_ecc/view/dijit/SearchBox_3_dropdown"));
        assertThat(searchList.isDisplayed()).isTrue();
        performAction(Keys.DOWN);
        performAction(Keys.TAB);
        sleep(100);
        performAction(Keys.TAB);
        performAction(Keys.ENTER);
		/*WebElement insertButton = driver.findElement(imageInsertButton);
		highlightElement(insertButton);
		insertButton.click();*/
        switchToEditorFrame();
        performAction(Keys.DOWN);
        WebElement image = driver.findElement(By.cssSelector("img[alt='" + imageName + "']"));
        image.isDisplayed();
        switchToMainFrame();
        return image;
    }

    public DocumentEditTab selectAndCommentTheCopyItem(CopyItem element, boolean addComment) {
        AtomicReference<WebElement> webElement = new AtomicReference<>();
        switch (element.getCopyItem()) {
            case "para":
                webElement.set(driver.findElement(paragraphLocator));
                break;
            case "number":
                webElement.set(driver.findElement(numberListLocator));
                break;
            case "bullet":
                webElement.set(driver.findElement(bulletListLocator));
                break;
            case "table":
                webElement.set(driver.findElement(tableItemLocator));
                break;
            case "spread sheet":
                webElement.set(driver.findElement(spreadSheetLocator));
                break;
            case "reusable content":
                webElement.set(driver.findElement(reusableContentLocator));
                break;
            case "image":
                webElement.set(driver.findElement(imageLocator));
                break;
			/*case "toc":
				webElement.set(driver.findElement());
				break;*/
            case "box":
                webElement.set(driver.findElement(boxLocator));
                break;
        }
        webElement.get().click();
        elementId = webElement.get().getAttribute("id");
        if (addComment)
            addComment(randomString(5), elementId);
        return this;
    }

    public void selectCopyItem(WebElement element) {
        switchToEditorFrame();
        element.click();
    }

    public void imageLayoutAlign(WebElement element, ImageLayout layout) {
        selectCopyItem(element);
        switchToMainFrame();
        new LayoutPanel().clickImageLayoutButton(layout.getImageId());
        switchToEditorFrame();
        String actualImageClass = element.getAttribute("class");
        assertThat(actualImageClass).isEqualTo(layout.getImageClass());
        switchToMainFrame();
    }

    public void changeImage(WebElement oldImage, String newImageName) {
        selectCopyItem(oldImage);
        switchToMainFrame();
        new ImagePanel().clickChangeImageButton();
        waitForElementToBeDisplayed(imageDialogLocator);
        WebElement searchBox = driver.findElement(dialogInputBox);
        highlightElement(searchBox);
        searchBox.clear();
        searchBox.sendKeys(newImageName);
        sleep(1000);
        performAction(Keys.BACK_SPACE);
        sleep(100);
        waitForElementToBeDisplayed(By.id("widget_ecc/view/dijit/SearchBox_5_dropdown"));
        WebElement searchList = driver.findElements(By.cssSelector("div.search-box-menuPopup")).get(1);
        assertThat(searchList.isDisplayed()).isTrue();
        performAction(Keys.DOWN);
        performAction(Keys.TAB);
        sleep(100);
        performAction(Keys.TAB);
        performAction(Keys.ENTER);
		/*WebElement insertButton = driver.findElement(imageInsertButton);
		highlightElement(insertButton);
		insertButton.click();*/
        switchToEditorFrame();
        performAction(Keys.DOWN);
        WebElement image = driver.findElement(By.cssSelector("img"));
        image.isDisplayed();
        switchToMainFrame();
    }

    public void changeImageAltText(WebElement element, String expectedAltText) {
        selectCopyItem(element);
        switchToMainFrame();
        new ImagePanel().clickImageAltTextEditButton();
        WebElement imageAltText = driver.findElement(imageAltTextLocator);
        highlightElement(imageAltText);
        imageAltText.clear();
        imageAltText.sendKeys(expectedAltText);
        driver.findElement(imageAltTextAcceptButton).click();
        WebElement actualAltText = driver.findElement(imageAltTextValueLocator);
        assertThat(actualAltText.getText()).isEqualTo(expectedAltText);
        switchToEditorFrame();
        assertThat(element.getAttribute("alt")).isEqualTo(expectedAltText);
    }

    public void changeImageCaption(WebElement element, String expectedCaption) {
        selectCopyItem(element);
        switchToMainFrame();
        new ImagePanel().clickImageCaptionEditButton();
        WebElement imageCaption = driver.findElement(imageCaptionTextLocator);
        highlightElement(imageCaption);
        imageCaption.clear();
        imageCaption.sendKeys(expectedCaption);
        driver.findElement(imageCaptionAcceptButton).click();
        WebElement actualCaptionText = driver.findElement(imageCaptionValueLocator);
        assertThat(actualCaptionText.getText()).isEqualTo(expectedCaption);
        switchToEditorFrame();
        assertThat(element.getAttribute("title")).isEqualTo(expectedCaption);
    }

    public void changeImageSize(WebElement element, MetricUnit unit) {
        selectCopyItem(element);
        switchToMainFrame();
        new ImagePanel().clickImageSizeButton();
        driver.findElement(By.cssSelector(".dijitMenuActive [aria-label='" + unit.getMetricUnit() + " ']")).click();
        WebElement imageWidth = driver.findElement(imageWidthTextLocator);
        highlightElement(imageWidth);
        imageWidth.clear();
        imageWidth.sendKeys("" + 20);
        WebElement imageHeight = driver.findElement(imageHeightTextLocator);
        highlightElement(imageHeight);
        imageHeight.clear();
        imageHeight.sendKeys("" + 20);
        switchToEditorFrame();
        assertThat(element.getAttribute("data-ecc-units")).isEqualTo(unit.getMetricUnit());
        switchToMainFrame();
    }

    public void skipElementWithinBox(String skippedElementName, WebElement box, WebElement elementInBox) {
        new BoxPanel().clickSkipButton();
        waitForElementToBeDisplayed(dialogLocator);
        WebElement skippedElement = driver.findElement(By.xpath("//div[contains(@class,'dialog--displaying')]//*[contains(text(),'" + skippedElementName + "')]"));
        if (!(skippedElement.isSelected())) {
            skippedElement.click();
        }
        driver.findElement(dialogCloseLocator).click();
        switchToEditorFrame();
        box.click();
        if (skippedElementName.equals("Paragraph")) {
            assertThat(box.getAttribute("data-ecc-number-skip")).contains("p");
        } else {
            assertThat(box.getAttribute("data-ecc-number-skip")).containsIgnoringCase(skippedElementName);
        }
        // TODO check that the element does not have any numbers
        switchToMainFrame();
    }

    public void boxTitle(WebElement box, String boxNewTitle) {
        selectCopyItem(box);
        switchToMainFrame();
        WebElement boxTitle = driver.findElement(boxTitleLocator);
        highlightElement(boxTitle);
        boxTitle.clear();
        boxTitle.sendKeys(boxNewTitle);
        switchToEditorFrame();
        assertThat(box.getAttribute("title")).isEqualTo(boxNewTitle);
        assertThat(driver.findElement(By.cssSelector("h2")).getText()).isEqualTo(boxNewTitle);

    }

    public void boxPrefix(WebElement box, String boxNewPrefix) {
        selectCopyItem(box);
        switchToMainFrame();
        WebElement boxPrefix = driver.findElement(boxPrefixLocator);
        highlightElement(boxPrefix);
        boxPrefix.clear();
        boxPrefix.sendKeys(boxNewPrefix);
        switchToEditorFrame();
        assertThat(box.getAttribute("data-ecc-prefix")).isEqualTo(boxNewPrefix);
        assertThat(box.getAttribute("data-ecc-viz-overlay")).containsIgnoringCase(boxNewPrefix);
    }

    public void boxType(WebElement box, BoxTypes types) {
        selectCopyItem(box);
        switchToMainFrame();
        new BoxPanel().clickBoxTypeButton();
        driver.findElement(By.cssSelector(".dijitMenuActive [aria-label='" + types.getBoxTypes() + "']")).click();
        switchToEditorFrame();
        WebElement newBox = driver.findElement(By.cssSelector("[data-ecc-number='" + types.getBoxTypes().toLowerCase().trim() + "']"));
        assertThat(newBox.isDisplayed()).isTrue();
        assertThat(newBox.getCssValue("background-color")).isEqualTo(types.getBoxColor());
        switchToMainFrame();
    }

    public void removeBox(WebElement box) {
        selectCopyItem(box);
        switchToMainFrame();
        new BoxPanel().clickRemoveButton();
        switchToEditorFrame();
        List<WebElement> boxes = driver.findElements(By.cssSelector(".box"));
        assertThat(boxes.size()).isZero();
    }

    public WebElement insertPageBreak() {

        switchToMainFrame();
        new LayoutPanel().clickAddPageBreakButton();
        switchToEditorFrame();
        WebElement pageBreak = driver.findElement(pageBreakLocator);
        sleep(1000);
        assertThat(pageBreak.isDisplayed()).isTrue();
        String script = "return window.getComputedStyle(document.querySelector(\".page-break\"),':before').getPropertyValue('content')";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String pageBreakContent = (String) js.executeScript(script);
        assertThat(pageBreakContent).contains("page-break");
        switchToMainFrame();
        return pageBreak;
    }

    public WebElement insertColumnBreak() {
        switchToMainFrame();
        new LayoutPanel().clickAddColumnBreakButton();
        switchToEditorFrame();
        WebElement columnBreak = driver.findElement(columnBreakLocator);
        sleep(1000);
        assertThat(columnBreak.isDisplayed()).isTrue();
        String script = "return window.getComputedStyle(document.querySelector(\".column-break\"),':before').getPropertyValue('content')";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String columnBreakContent = (String) js.executeScript(script);
        assertThat(columnBreakContent).contains("column-break");
        switchToMainFrame();
        return columnBreak;
    }

    public void highlightParagraph(WebElement para) {
        switchToEditorFrame();
        Actions actions = new Actions(driver);
        actions.clickAndHold(para).release(para).build().perform();
//		actions.dragAndDrop(para,para).perform();
//		actions.doubleClick(para).build().perform();
//		para.sendKeys(Keys.chord(Keys.CONTROL, "a"));
//		switchToMainFrame();
    }

    public void highlightParagraph(WebElement para, WebElement nextPara) {
        switchToEditorFrame();
        Actions actions = new Actions(driver);
        actions.dragAndDrop(para, nextPara).build().perform();
        switchToMainFrame();
    }

    public void dragAndDrop(WebElement source, WebElement target) {
        switchToEditorFrame();
        source.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        Actions action = new Actions(driver);
        action.keyDown(Keys.LEFT_SHIFT).sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN).build().perform();
    }


    public WebElement insertColumnSpan() {
        switchToMainFrame();
        new LayoutPanel().clickAddColumnSpanButton();
        switchToEditorFrame();
        (new Actions(driver)).sendKeys(Keys.ENTER, Keys.ENTER);
        WebElement columnSpan = driver.findElement(columnSpanLocator);
        sleep(1000);
        assertThat(columnSpan.isDisplayed()).isTrue();
        String script = "return window.getComputedStyle(document.querySelector(\".column-span\"),':before').getPropertyValue('content')";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String columnSpanContent = (String) js.executeScript(script);
        assertThat(columnSpanContent).contains("column span");
        switchToMainFrame();
        return columnSpan;
    }

    public void logConsoleEntries() {
        LogEntries logEntries;
        logEntries = driver.manage().logs().get(LogType.BROWSER);
        for (LogEntry logEntry : logEntries) {
            System.out.println(String.valueOf("Time Stamp:" + logEntry.getTimestamp()));
            System.out.println(String.valueOf("Log Level:" + logEntry.getLevel()));
            System.out.println(String.valueOf("Log Message:" + logEntry.getMessage()));
        }
    }

    public WebElement insertA3Page() {
        switchToMainFrame();
        new LayoutPanel().clickAddA3PageButton();
        switchToEditorFrame();
        performAction(Keys.ARROW_LEFT, Keys.ARROW_DOWN);
        WebElement a3Page = driver.findElement(a3PageLocator);
        sleep(1000);
        assertThat(a3Page.isDisplayed()).isTrue();
        String script = "return window.getComputedStyle(document.querySelector(\".a3-page\"),':before').getPropertyValue('content')";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String a3PageContent = (String) js.executeScript(script);
        assertThat(a3PageContent).contains("A3 page");
        switchToMainFrame();
        return a3Page;
    }

    public WebElement insertFullPage() {
        switchToMainFrame();
        new LayoutPanel().clickAddFullPageButton();
        switchToEditorFrame();
        performAction(Keys.ARROW_LEFT, Keys.ARROW_DOWN);
        WebElement fullPage = driver.findElement(fullPageLocator);
        sleep(1000);
        assertThat(fullPage.isDisplayed()).isTrue();
        String script = "return window.getComputedStyle(document.querySelector(\".full-page\"),':before').getPropertyValue('content')";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String fullPageContent = (String) js.executeScript(script);
        assertThat(fullPageContent).contains("full page");
        switchToMainFrame();
        return fullPage;
    }

    public WebElement insertA3FullPage() {
        switchToMainFrame();
        new LayoutPanel().clickAddA3FullPageButton();
        switchToEditorFrame();
        performAction(Keys.ARROW_LEFT, Keys.ARROW_DOWN);
        WebElement a3FullPage = driver.findElement(a3FullPageLocator);
        sleep(1000);
        assertThat(a3FullPage.isDisplayed()).isTrue();
        String script = "return window.getComputedStyle(document.querySelector(\".a3-full-page\"),':before').getPropertyValue('content')";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String a3PageContent = (String) js.executeScript(script);
        assertThat(a3PageContent).contains("A3 full page");
        switchToMainFrame();
        return a3FullPage;
    }

    public WebElement insertLandscape() {
        switchToMainFrame();
        new LayoutPanel().clickLandscapeButton();
        switchToEditorFrame();
        performAction(Keys.ARROW_LEFT, Keys.ARROW_DOWN);
        WebElement landscapePage = driver.findElement(landscapeLocator);
        sleep(1000);
        assertThat(landscapePage.isDisplayed()).isTrue();
        String script = "return window.getComputedStyle(document.querySelector(\".landscape\"),':before').getPropertyValue('content')";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String landscapePageContent = (String) js.executeScript(script);
        assertThat(landscapePageContent).contains("landscape page");
        switchToMainFrame();
        return landscapePage;
    }

    public void checkElementInsideTheOtherElement(String elementXpath) {
        switchToEditorFrame();
        WebElement elementInsideA3Page = driver.findElement(By.xpath(elementXpath));
        assertThat(elementInsideA3Page.isDisplayed()).isTrue();
        switchToMainFrame();
    }

    public void removeA3page() {
        switchToMainFrame();
        new LayoutPanel().clickAddA3PageButton();
        switchToEditorFrame();
        List<WebElement> element = driver.findElements(a3PageLocator);
        assertThat(element.size()).isZero();
    }

    public void removeFullpage() {
        switchToMainFrame();
        new LayoutPanel().clickAddFullPageButton();
        switchToEditorFrame();
        List<WebElement> element = driver.findElements(fullPageLocator);
        assertThat(element.size()).isZero();
    }

    public void deleteItem() {
        performAction(Keys.DELETE);
    }

    public DocumentEditTab selectAndCommentTheTableCell(CopyItem element) {
        WebElement webElement = driver.findElement(tableCellLocator);
        webElement.click();
        elementId = webElement.getAttribute("id");
        WebElement tableElement = driver.findElement(By.xpath("//div[@class='editor__item__copy-view']/table[not(@data-ecc-live-ref)]//td[@id='" + elementId + "']/ancestor::table"));
        String tableElementId = tableElement.getAttribute("id");
        CommentPanel commentPanel = new CommentPanel();
        commentPanel.addCommentButton();
        waitForElementToBeDisplayed(commentDialogLocator);
        Actions action = new Actions(driver);
        action.sendKeys(randomString(5)).build().perform();
        driver.findElement(commentAddButton).click();

        WebElement bubblecommentCountElement = driver.findElement(
                By.cssSelector("span.comments-numbering__lbl[data-elementid='" + tableElementId + "']"));
        assertThat(bubblecommentCountElement.isDisplayed()).isTrue();
        // click the bubble count and check the comment for the element is displayed
        bubblecommentCountElement.click();
        List<WebElement> commentForElement = driver.findElements(commentForElementLocator);
        for (WebElement elementt : commentForElement) {
            elementt.isDisplayed();
        }
        // check that the count number and the actual comments are same
        String uicommentCount = bubblecommentCountElement.getText();
        int commentsCount = commentForElement.size();
        assertThat(uicommentCount).isEqualTo("" + commentsCount);
        System.out.println("Comments Count:" + commentsCount + "UICommentCount:" + uicommentCount);
        return this;
    }

    public DocumentEditTab addComment(String comment, String elementId) {

        CommentPanel commentPanel = new CommentPanel();
        commentPanel.addCommentButton();
        waitForElementToBeDisplayed(commentDialogLocator);
        Actions action = new Actions(driver);
        action.sendKeys(comment).build().perform();
        driver.findElement(commentAddButton).click();

        WebElement bubblecommentCountElement = driver.findElement(
                By.cssSelector("span.comments-numbering__lbl[data-elementid='" + elementId + "']"));
        assertThat(bubblecommentCountElement.isDisplayed()).isTrue();
        // click the bubble count and check the comment for the element is displayed
        bubblecommentCountElement.click();
        List<WebElement> commentForElement = driver.findElements(commentForElementLocator);
        for (WebElement element : commentForElement) {
            element.isDisplayed();
        }
        // check that the count number and the actual comments are same
        String uicommentCount = bubblecommentCountElement.getText();
        int commentsCount = commentForElement.size();
        assertThat(uicommentCount).isEqualTo("" + commentsCount);
        System.out.println("Comments Count:" + commentsCount + "UICommentCount:" + uicommentCount);
        return this;
    }


    public DocumentEditTab selectText(String text) {
		/*String script = "var iframe = document.getElementsByClassName('html-editor__iframe')[0];" +
				"var contentDoc = iframe.contentDocument || iframe.contentWindow.document;" +
				"var contentWindow = iframe.contentWindow;" +
				"var para = contentDoc.querySelectorAll(\"[data-test='" + text + "']\")[0];" +
				"var range = contentDoc.createRange();" +
				"range.selectNodeContents(para);" +
				"var sel = contentWindow.getSelection();" +
				"sel.removeAllRanges();" + " sel.addRange(range);" +
				"return para";
		WebElement selectedText = (WebElement)((JavascriptExecutor) driver).executeScript(script);
*/
        switchToEditorFrame();
        WebElement startPara = driver.findElement(By.cssSelector("p[data-test='abcd']"));
        WebElement endPara = driver.findElement(By.cssSelector("p[data-test='" + text + "']"));
        Actions action = new Actions(driver);
        action.clickAndHold(endPara).release().build().perform();
        action.clickAndHold(startPara).moveToElement(endPara).release().build().perform();
        switchToMainFrame();
        return this;
    }

    /*Format text bold, italic, underline, subscript, Superscript, Strikethrough */

    public DocumentEditTab formatText(Format format) {
        selectText("lorem ipsum");
        switch (format.getFormatText()) {
            case "b":
                new BasicPanel().clickBold();
                break;
            case "i":
                new BasicPanel().clickItalic();
                break;
            case "u":
                new BasicPanel().clickUnderline();
                break;
            case "sub":
                new BasicPanel().clickSubscript();
                break;
            case "sup":
                new BasicPanel().clickSuperscript();
                break;
            case "strike":
                new BasicPanel().clickStrike();
                break;
        }
        switchToEditorFrame();
        WebElement formatElement = driver.findElement(By.tagName(format.getFormatText()));
        if (formatElement == null) {
            fail("could not find specified italic element in the html body");
        } else {
            assertThat(formatElement.getTagName()).isEqualTo(format.getFormatText());
            assertThat(formatElement.getText()).isEqualTo("lorem ipsum");
        }
        switchToMainFrame();
        return this;
    }

    private void selectRow(Integer rowNumber) {
        switchToEditorFrame();
        WebElement startCell = driver.findElement(By.cssSelector("tr[data-test='row-" + rowNumber + "'] > td[data-test='cell-" + rowNumber + "x1']"));
        WebElement endCell = driver.findElement(By.cssSelector("tr[data-test='row-" + rowNumber + "'] > td[data-test='cell-" + rowNumber + "x4']"));
        Actions action = new Actions(driver);
        action.clickAndHold(startCell).release().build().perform();
        action.clickAndHold(startCell).moveToElement(endCell).release().build().perform();
        switchToMainFrame();
    }

    private void selectCell(Integer rowNumber, Integer cellNumber) {
        switchToEditorFrame();
        WebElement startCell = driver.findElement(By.cssSelector("tr[data-test='row-" + rowNumber + "'] > td:not([colspan])[data-test='cell-" + rowNumber + "x" + cellNumber + "']"));
        Actions action = new Actions(driver);
        action.clickAndHold(startCell).release().build().perform();
        switchToMainFrame();
    }

    public DocumentEditTab discardChanges() {
        switchToMainFrame();
        new BasicPanel().clickDiscardChangesButton();
        waitForElementToBeDisplayed(notificationMessage);
        driver.findElement(notificationMessageDiscardButton).click();
        assertThat(driver.findElement(notificationMessage).getText()).isEqualTo("Your changes have been discarded");

        waitForElementToBeDisplayed(copyEditButton);
        assertThat(driver.findElement(copyEditButton).isEnabled()).isTrue();

        return this;
    }

    public DocumentEditTab saveAndCloseCopy() {
        switchToMainFrame();
        new BasicPanel().clickConfirmChangesButton();
        return this;
    }

    public DocumentEditTab saveAndContinueToEditCopy() {
        switchToMainFrame();
        new BasicPanel().clickSaveButton();
        waitForElementToBeDisplayed(notificationMessage);
        assertThat(driver.findElement(notificationMessage).getText()).isEqualTo("Your changes have been saved");
        switchToEditorFrame();
        return this;
    }

    /**
     * Switches the selenium context to the main frame (everything that is outside the content edit iframe).
     */
    public void switchToMainFrame() {
        driver.switchTo().defaultContent();
    }

    /**
     * Switches the selenium context to the editor frame (the iframe that performs content text editing).
     */
    public void switchToEditorFrame() {
        driver.switchTo().frame(driver.findElement(htmlEditorIFrame));
    }

    /*
     * Switches the selenium context to the verification frame( the iframe that performs the sources text editing)
     * */
    public DocumentEditTab switchToVerificationSourceFrame(By frameLocator) {
        driver.switchTo().frame(driver.findElement(frameLocator));
        return this;
    }

    public DocumentSettingsTab openDocumentSettingsTab() {
        return super.openDocumentSettingsTab();
    }


    public DocumentEditTab viewAssignedTask(String title, String instruction) {
        switchToMainFrame();
//		driver.findElement(actionPaneTitleBarLocator).click();
        waitForElementToBeDisplayed(allTaskContentLocator);
        WebElement taskContent = driver.findElement(taskContentLocator);
        taskContent.isDisplayed();
        WebElement taskTitle = driver.findElement(taskComponentTileLocator);
        assertThat(taskTitle.getText()).isEqualToIgnoringCase(title);
        WebElement taskInstruction = driver.findElement(taskInstructionValueLocator);
        assertThat(taskInstruction.getText()).isEqualToIgnoringCase(instruction);
        WebElement taskState = driver.findElement(taskNotStartedLocator);
        assertThat(taskState.getText()).isEqualToIgnoringCase("not started");
        return this;
    }

    public DocumentEditTab autoNumbering() {
        BasicPanel.clickNumberingButton();
        driver.findElement(autoNumberingLocator).click();
        return this;
    }


    public DocumentEditTab manualNumbering(WebElement element, CopyItem copyItem) {
        String number = "10";
        By manualNumberingInputLocator = null;
        switchToEditorFrame();
        highlightElement(element);
        Actions action = new Actions(driver);
//		action.moveToElement(element).build().perform();
        action.click(element).build().perform();
        sleep(1000);
        switchToMainFrame();
        switch (copyItem.getCopyItem()) {
            case "para":
                BasicPanel.clickNumberingButton();
                manualNumberingInputLocator = By.cssSelector(".button-control-basic-numbering .dijitInputContainer input");
                break;
            case "box":
                new BoxPanel().clickBoxNumberingButton();
                manualNumberingInputLocator = By.cssSelector(".box-property__manual .dijitInputContainer input");
                break;
            case "table":
                DesignPanel designPanel = new DesignPanel();
                designPanel.clickTableNumberingButton();
                manualNumberingInputLocator = By.cssSelector(".button-control-table-numbering .dijitInputContainer input");
                break;
            case "image":
                new ImagePanel().clickImageNumberingButton();
                manualNumberingInputLocator = By.cssSelector(".button-control-image-numbering .dijitInputContainer input");
                break;
        }
        driver.findElement(manualNumberingLocator).click();
        WebElement manualNumber = driver.findElement(manualNumberingInputLocator);
        highlightElement(manualNumber);
        manualNumber.sendKeys(number);
        switchToEditorFrame();
//		assertThat(element.getAttribute("data-ecc-number")).isEqualTo("manual");
        assertThat(element.getAttribute("data-ecc-manual")).isEqualTo(number);
        performAction(Keys.ARROW_DOWN, Keys.ENTER);
        switchToMainFrame();
        return this;
    }

    public DocumentEditTab autoNumbering(WebElement element, CopyItem copyItem) {
        highlightElement(element);
        Actions action = new Actions(driver);
        action.moveToElement(element).build().perform();
        action.click(element).build().perform();
        sleep(1000);
        switchToMainFrame();
        switch (copyItem.getCopyItem()) {
            case "para":
                BasicPanel.clickNumberingButton();
                break;
            case "box":
                new BoxPanel().clickBoxNumberingButton();
                break;
            case "table":
                DesignPanel designPanel = new DesignPanel();
                designPanel.clickTableNumberingButton();
                break;
            case "image":
                new ImagePanel().clickImageNumberingButton();
                break;
        }
        driver.findElement(autoNumberingLocator).click();
        switchToEditorFrame();
//		assertThat(element.getAttribute("data-ecc-number")).isEqualTo("manual");
        performAction(Keys.ARROW_DOWN, Keys.ENTER);
        switchToMainFrame();
        return this;
    }


    public DocumentEditTab noneNumbering(WebElement element, CopyItem copyItem) {
        switchToEditorFrame();
        highlightElement(element);
        Actions action = new Actions(driver);
        action.click(element).build().perform();
        sleep(1000);
        switchToMainFrame();
        switch (copyItem.getCopyItem()) {
            case "para":
                BasicPanel.clickNumberingButton();
                break;
            case "box":
                new BoxPanel().clickBoxNumberingButton();
                break;
            case "table":
                DesignPanel designPanel = new DesignPanel();
                designPanel.clickTableNumberingButton();
                break;
            case "image":
                new ImagePanel().clickImageNumberingButton();
                break;
        }
        driver.findElement(noneNumberingLocator).click();
        switchToEditorFrame();
        assertThat(element.getAttribute("data-ecc-number")).isEqualTo("none");
        performAction(Keys.ARROW_DOWN, Keys.ENTER);
        switchToMainFrame();
        return this;
    }

    /*
     * Maintain the ongoing verification record for the document
     * */
    public String saveVerificationMetadataForm() {
        new VerificationPanel().clickVerifyButton();
        // check that the verification dialog is displayed
        waitForElementToBeDisplayed(verificationDialogLocator);

        // select the check Legal and financial
        List<WebElement> legalCheckboxes = driver.findElements(checksCheckboxesLocator);
        for (WebElement checkbox : legalCheckboxes) {
            if (checkbox.isSelected()) {
                LOG.info("checkbox is selected");
            } else {
                checkbox.click();
            }
        }
        /* I can not find best way to insert text into the sources editor*/
        String htmlText = "Hello World";
        String sourcesFrameUniqueName = findIdOfClass(".dialog--displaying .value-capture__edit.dijitEditor");
        String script = "var iframe = document.getElementById('" + sourcesFrameUniqueName + "_iframe');" +
                "var contentDoc = (iframe.contentDocument || iframe.contentWindow.document);" +
                "var contentWindow = iframe.contentWindow;" +
                "var editorBody = contentDoc.getElementById('dijitEditorBody');" +
                "editorBody.innerHTML= '" + htmlText + "';";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(script);
        driver.findElement(verifySaveButton).click();
        return htmlText;
    }


    private String findIdOfClass(String className) {
        WebElement classElement = driver.findElement(By.cssSelector(className));
        id = classElement.getAttribute("id");
        return id;
    }

    public void checkOnGoingVerificationRecord(String expectedSourceText) {
        // click the verify button
        new VerificationPanel().clickVerifyButton();
        waitForElementToBeDisplayed(verificationDialogLocator);
        List<WebElement> legalCheckboxes = driver.findElements(checksCheckboxesLocator);
        for (WebElement checkbox : legalCheckboxes) {
            assertThat(checkbox.isSelected()).isTrue();
        }

        // get the text inside the source
        String sourcesFrameUniqueName = findIdOfClass(".dialog--displaying .value-capture__edit.dijitEditor");
        String script = "var iframe = document.getElementById('" + sourcesFrameUniqueName + "_iframe');" +
                "var contentDoc = (iframe.contentDocument || iframe.contentWindow.document);" +
                "var contentWindow = iframe.contentWindow;" +
                "var editorBody = contentDoc.getElementById('dijitEditorBody');" +
                "var sourceText = editorBody.innerHTML;" +
                "return sourceText;";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String sourceText = (js.executeScript(script)).toString();
        System.out.println("the source text" + sourceText);
        assertThat(sourceText).isEqualTo(expectedSourceText);
    }

    public DocumentEditTab verifyContent() {
        WebElement password = driver.findElement(passwordLocator);
        password.sendKeys("lime01");
        driver.findElement(verifyButton).click();
//		check that the content has been verified
        WebElement verifyMessageElement = driver.findElement(verifyMessageLocator);
        String expectedVerifyMessage = "This content has been verified";
        assertThat(verifyMessageElement.getText()).isEqualTo(expectedVerifyMessage);
        return this;
    }

    public void versionControl() {
        WebElement versionTitleBar = driver.findElement(versionTitleBarLocator);
        versionTitleBar.click();
        List<WebElement> versions = driver.findElements(contentVersions);
        for (WebElement version : versions) {
            version.isDisplayed();

        }
    }

    public void checkAutoNumberingAppliedForTable(String query) {
        //Need to use java script to get the CSS values in the pseudo element(:before)
        switchToEditorFrame();
        String script = "return window.getComputedStyle(document.querySelector(\"" + query + "\"),':before').getPropertyValue('content')";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String autoNumber = (String) js.executeScript(script);
        System.out.println("auto numbering for the Item is  " + autoNumber);
        assertThat(autoNumber).contains("#");
    }

    public void checkAutoNumberingAppliedForParagraph() {
        switchToEditorFrame();
//		String script = "return document.evaluate(\"//p[contains(text(),'paragraph')]\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;";
//		JavascriptExecutor js = (JavascriptExecutor) driver;
//		WebElement paraElement = (WebElement) js.executeScript(script);
        String numberScript = "return window.getComputedStyle(document.evaluate(\"//p[contains(text(),'paragraph')]\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue,':before').getPropertyValue('content');";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String autoNumber = (String) js.executeScript(numberScript);
        assertThat(autoNumber).contains("1.0.#");
    }

    public void checkAutoNumberingAppliedForBox(CopyItem item) {
        switchToEditorFrame();
        WebElement box = driver.findElement(By.cssSelector("[data-ecc-number='" + (item.getCopyItem()).toLowerCase().trim() + "']"));
        assertThat(box.getAttribute("data-ecc-viz-overlay")).contains("1.0.#");
    }

    public void showDifference() {

    }

    public DocumentEditTab setStakeholderDocument() {
        DynamicPanel dynamicPanel = new DynamicPanel();
        dynamicPanel.clickChangeButton();
        driver.findElement(documentTypeDialogLocator).isDisplayed();
        WebElement stakeholderRadioButton = driver.findElement(stakeholderDynamicRadioButton);
        if (!stakeholderRadioButton.isSelected()) {
            stakeholderRadioButton.click();
        }
        driver.findElement(okButton).click();
//		Wait<WebDriver> fluentWait = fluentWait(50, 5);
//		fluentWait.until(ExpectedConditions.textToBe(dynamicTypeLabel, "Stakeholder Dynamic"));
        WebElement dynamicType = driver.findElement(dynamicTypeLabel);
        assertThat(dynamicType.getText()).isEqualToIgnoringCase("Stakeholder Dynamic");
        return this;
    }

    public DocumentStructure getDocumentStructure() {
        System.out.println("expand the tree structure");
        return new DocumentStructure(driver);
    }

    public DocumentEditor openDocumentEditor() {
        return new DocumentEditor(driver);

    }

}
