package com.jspringbot.selenium.extension;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

import java.util.logging.Level;

public class TestBrowserstack {


    public static final String USERNAME = "param35";
    public static final String AUTOMATE_KEY = "zDxti1ZepVXwBFrrzYez";
    public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";


    public void testBrowserstack() throws Exception {

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browser", "chrome");
        caps.setCapability("browserstack.debug", "true");
        caps.setCapability("build", "First build");

        System.out.println(URL);
        WebDriver driver = new RemoteWebDriver(new URL(URL), caps);
        driver.get("http://www.google.com");
        WebElement element = driver.findElement(By.name("q"));

        element.sendKeys("BrowserStack");
        element.submit();

        System.out.println(driver.getTitle());
        //driver.quit();

    }

    @Test
    public void TestLogging() throws Exception {
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.SEVERE);
        System.setProperty("webdriver.gecko.driver", "/zeta/work/robot-workspace/adchemy-automation-lib/selenium-extension/src/main/resources/drivers/mac/geckodriver");

        //Path path = FileSystems.getDefault().getPath("src/main/resources/geckodriver");
        //System.setProperty("webdriver.gecko.driver",path.toString());


        //Set Firefox Headless mode as TRUE
        FirefoxOptions options = new FirefoxOptions();
        options.setCapability("","");


        //Instantiate Web Driver
        //WebDriver driver = new FirefoxDriver(options);

        //driver.get("https://devratemarketplace.com/a/-/xln_cp_email003?sk=5rmCh");
        WebDriver driver=new FirefoxDriver(options);

        driver.manage().window().maximize();


        driver.get("http://learn-automation.com/");

        Thread.sleep(1000);
        driver.quit();


    }
}
