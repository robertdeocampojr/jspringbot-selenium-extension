package com.jspringbot.selenium.extension;

import org.jspringbot.keyword.selenium.ElementFinder;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by robertdeocampo on 9/30/15.
 */
public class SeleniumExtensionHelper{
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(SeleniumExtensionHelper.class);

    protected WebDriver driver;
    protected ElementFinder finder;
    protected JavascriptExecutor executor;


    public SeleniumExtensionHelper(WebDriver driver) {
        this.driver = driver;
        this.executor = (JavascriptExecutor) driver;
        this.finder = new ElementFinder(driver);
    }

    public void highlightElement(String locator){
        LOG.keywordAppender().appendLocator(locator);
        WebElement element = finder.find(locator);

        JavascriptExecutor js=(JavascriptExecutor)driver;
        js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);

        try{
            Thread.sleep(500);
        }
        catch (InterruptedException e) {
            LOG.keywordAppender().appendArgument("Message", e.getMessage());
        }

        js.executeScript("arguments[0].setAttribute('style','border: solid 2px white');", element);
    }

    public void setImplicitWaitTime(long time){
        driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
    }




    public void switchToActiveWindow() {
        driver.switchTo().activeElement();
    }

    public List<String> getListLabels(String locator){
        LOG.keywordAppender().appendLocator(locator);
        ArrayList values = new ArrayList();

        WebElement selectEl = finder.find(locator);
        Select select = new Select(selectEl);
        List options  = select.getOptions();

        for(int i = 0; i < options.size(); ++i) {
            WebElement option = (WebElement)options.get(i);
            LOG.keywordAppender().appendArgument(String.format("value=%s", new Object[]{option.getAttribute("value")}), option.getText());
            values.add(option.getText());
        }

        LOG.keywordAppender().appendLocator("Values:", values);
        return values;
    }

    public String getSelectSize(String locator){
        LOG.keywordAppender().appendLocator(locator);

        WebElement selectEl = finder.find(locator);
        Select select = new Select(selectEl);
        List options  = select.getOptions();

        LOG.keywordAppender().appendLocator("Size", options.size());
        return String.valueOf(options.size());
    }

    public List<String> getElementAttributesByXpath(String locator, String attribute){
        LOG.keywordAppender().appendLocator(locator);
        ArrayList values = new ArrayList();

        List<WebElement> elements = driver.findElements(By.xpath(locator));

        if(elements.isEmpty()){
            return null;
        }
        for(WebElement element : elements) {
            String value = element.getAttribute(attribute);
            LOG.keywordAppender().appendArgument("value= ", value);
            values.add(value);
        }

        LOG.keywordAppender().appendLocator("Values:", values);
        return values;
    }

    public boolean isSelectListContainsValue(String locator, String value){
        LOG.keywordAppender().appendLocator(locator);

        WebElement selectEl = finder.find(locator);
        Select select = new Select(selectEl);
        List options  = select.getOptions();

        for(int i = 0; i < options.size(); ++i) {
            WebElement option = (WebElement) options.get(i);
            LOG.keywordAppender().appendArgument(String.format("value=%s", new Object[]{option.getAttribute("value")}), option.getText());
            if(option.getAttribute("value").equals(value)){
                return true;
            }
        }
        return false;
    }

    public boolean isElementDisplayed(String locator){
        LOG.keywordAppender().appendLocator(locator);

        boolean isDisplayed = finder.find(locator).isDisplayed();
        LOG.keywordAppender().appendArgument("Displayed", Boolean.valueOf(isDisplayed));
        return isDisplayed;
    }

    public boolean isSelectListContainsLabel(String locator, String value){
        LOG.keywordAppender().appendLocator(locator);
        WebElement selectEl = finder.find(locator);
        Select select = new Select(selectEl);
        List options  = select.getOptions();

        for(int i = 0; i < options.size(); ++i) {
            WebElement option = (WebElement) options.get(i);
            LOG.keywordAppender().appendArgument(String.format("value=%s", new Object[]{option.getAttribute("value")}), option.getText());
            if(option.getText().equals(value)){
                return true;
            }
        }
        return false;
    }

    public boolean isStringContains(String toCompare, String value){
        String str1 = String.valueOf(toCompare);
        String str2 = String.valueOf(value);
        LOG.keywordAppender().append(String.format("Compare: %s and %s", str1, str2));
        return str1.contains(str2);
    }

}
