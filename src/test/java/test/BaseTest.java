package test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BaseTest{
    protected WebDriver webDriver;
    protected String code;
    protected String path;
    protected String url = "https://www.hackerrank.com/challenges/validating-credit-card-number/problem";

    @BeforeClass
    public static void prepareWebDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void setUp() {
        createWebDriver();
        readFileCode();
        getPageAndWaitForLoad(url);
    }

    @After
    public void afterEachBaseSeleniumTest() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }


    private void createWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        webDriver = new ChromeDriver(options);

        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);
    }


    private void readFileCode(){
        String filePath = "src\\test\\resources\\inputCode.txt";
        Path pathToFile = Paths.get(filePath);
        path = pathToFile.toAbsolutePath().toString();
        try {
            code = Files.lines(Paths.get(filePath))
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getPageAndWaitForLoad(String url){
        webDriver.get(url);
        WebDriverWait wait2 = new WebDriverWait(webDriver, 90);
        ExpectedCondition<Boolean> pageLoadCondition = x ->
                x != null && ((JavascriptExecutor) x).executeScript("return document.readyState").equals("complete");
        wait2.until(pageLoadCondition);
    }
}
