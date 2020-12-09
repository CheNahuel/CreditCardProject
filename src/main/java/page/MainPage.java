package page;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainPage {
    private String mainSignUp = ".modal-container .tab-list-content > .signup-form form";
    private String loginTabButton = ".modal-container .tab-header > button[aria-selected=false]";
    private String userNameInput = ".tab-list-content form input[name=username]";
    private String passwordInput = ".tab-list-content form input[name=password]";
    private String mainLogin = ".modal-container .tab-list-content > .login-form form";
    private String submitButton = ".modal-container .tab-list-content > .login-form form button[type=submit]";
    private String loginSuccess = ".nav-buttons li div[data-analytics=NavBarProfileDropDown]";

    private String uploadFileButton = ".hr-monaco-editor-with-input button[data-analytics='Upload File']";

    private String alertPopup = "section.modal-container form.confirm-upload-form";
    private String alertPopupYesButton = "section.modal-container button[class~=confirm-button]";
    private String alertPopup2 = "section.modal-container form.confirm-upload";
    private String selectFileButton = "section.modal-container form .fake-input-wrap button";
    private String languageDropdown = "section.modal-container form select";
    private String uploadButton = "section.modal-container form .clearfix button";

    private String runCodeButton = ".hr-monaco-editor-with-input button[class~=hr-monaco-compile]";
    private String codeSuccess = ".code-compile-test-view p[class='status compile-success']";
    private String codeResult = ".tc-container .testcase-wrapper";

    private WebDriver driver;
    private WebDriverWait wait;

    public MainPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 30);
    }

    public void loginIntoPage(String user, String pass){
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(mainSignUp)));
        driver.findElement(By.cssSelector(loginTabButton)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(mainLogin)));
        driver.findElement(By.cssSelector(userNameInput)).sendKeys(user);
        driver.findElement(By.cssSelector(passwordInput)).sendKeys(pass);
        driver.findElement(By.cssSelector(submitButton)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(loginSuccess)));
    }

    public void clickOnUploadFile(){
        driver.findElement(By.cssSelector(uploadFileButton)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(alertPopup)));
        driver.findElement(By.cssSelector(alertPopupYesButton)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(alertPopup2)));
    }

    public void selectFileFromPath(String language, String path){
        driver.findElement(By.cssSelector(selectFileButton)).click();
        try {Thread.sleep(2000);} catch (InterruptedException ignore) {}
        copyPasteAndEnter(path);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(uploadButton)));
        selectLanguage(language);
        driver.findElement(By.cssSelector(uploadButton)).click();
    }

    public void waitAndRefreshPage(){
        try {Thread.sleep(3000);} catch (InterruptedException ignore) {}
        driver.navigate().refresh();
    }

    public void runCodeAndCheckResults(){
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(runCodeButton)));
        driver.findElement(By.cssSelector(runCodeButton)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(codeSuccess)));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(codeResult)));
        saveScreenShot();
    }

    private void selectLanguage(String language){
        Select drpLanguage = new Select(driver.findElement(By.cssSelector(languageDropdown)));
        drpLanguage.selectByValue(language);
    }

    private void copyPasteAndEnter(String path) {
        StringSelection stringSelection = new StringSelection(path);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        Robot r;
        try {
            r = new Robot();
            r.keyPress(KeyEvent.VK_CONTROL);
            r.keyPress(KeyEvent.VK_V);
            r.keyRelease(KeyEvent.VK_CONTROL);
            r.keyRelease(KeyEvent.VK_V);
            r.keyPress(KeyEvent.VK_ENTER);
            r.keyRelease(KeyEvent.VK_ENTER);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void saveScreenShot(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String dateString = format.format(new Date());
        String targetLocation;
        String screenShotName = "CodeResult" + dateString + ".png";
        String fileSeparator = System.getProperty("file.separator");
        String reportsPath = System.getProperty("user.dir") + fileSeparator + "src\\test\\java\\outputTest";
        System.out.println(reportsPath);

        try {
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            targetLocation = reportsPath + fileSeparator + screenShotName;

            File targetFile = new File(targetLocation);
            FileHandler.copy(screenshotFile, targetFile);

        } catch (FileNotFoundException e) {
            System.out.println("File not found exception occurred while taking screenshot " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An exception occurred while taking screenshot " + e.getCause());
        }
    }

}
