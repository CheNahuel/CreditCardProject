package test;

import org.junit.Test;
import page.MainPage;

public class ExecutorTest  extends  BaseTest{
    /**
     * Valid credentials
     */
    String user="";
    String pass="";

    //Language should match with inputCode.txt
    String language="python3";

    @Test
    public void CodeExecution(){
        MainPage page = new MainPage(webDriver);

        //Login
        page.loginIntoPage(user, pass);

        //ClickOnUpload File
        page.clickOnUploadFile();

        //Select File from project, language and save
        page.selectFileFromPath(language, path);

        //Refresh to reload the login
        page.waitAndRefreshPage();

        //Save screenshot results
        page.runCodeAndCheckResults();
    }
}
