package com.jspringbot.selenium.extension;

import org.jspringbot.keyword.selenium.ElementFinder;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.awt.datatransfer.*;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.Robot;

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

    public void closeSession(){
        this.driver.quit();
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

    public List<String> getLElementsTextByXpath(String locator){
        LOG.keywordAppender().appendLocator(locator);
        ArrayList values = new ArrayList();

        List<WebElement> elements = driver.findElements(By.xpath(locator));

        if(elements.isEmpty()){
            return null;
        }
        for(WebElement element : elements) {
            String value = element.getText();
            LOG.keywordAppender().appendArgument("value= ", value);
            values.add(value);
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

    public int getElementWidth(String locator){
        LOG.keywordAppender().appendLocator(locator);

        WebElement element = finder.find(locator);

        LOG.keywordAppender().appendArgument("Width", element.getSize().getWidth());
        return element.getSize().getWidth();
    }

    public int getElementHeight(String locator){
        LOG.keywordAppender().appendLocator(locator);

        WebElement element = finder.find(locator);

        LOG.keywordAppender().appendArgument("Height", element.getSize().getHeight());
        return element.getSize().getHeight();
    }

    private static void setClipboardData(String string) {
        // StringSelection is a class that can be used for copy and paste
        // operations.
        StringSelection stringSelection = new StringSelection(string);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

    }

    public void upload(String fileLocation) {
        // Setting clipboard with file location
        setClipboardData(fileLocation);

        Robot robot;
        try {
            // native key strokes for CTRL, V and ENTER keys
            robot = new Robot();
            Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();

            String platform = cap.getCapability("platform").toString();
            String browserName = cap.getBrowserName();


            LOG.info("Browser: " + browserName);
            Thread.sleep(2000);

            // Pass your OS platform name here, I am using properties file where OS name is saved you can as a string.
            switch (platform.toLowerCase()) {

                case "mac":
                    LOG.info("OS: " + platform);
                    // In mac machine for chrome you need to switch focus to upload dialog again
                    // I have saved browser name in properties file you can pass it as string.
                    if (browserName.equalsIgnoreCase("chrome")) {
                        robot.keyPress(KeyEvent.VK_META);
                        robot.keyPress(KeyEvent.VK_TAB);
                        robot.keyRelease(KeyEvent.VK_META);
                        robot.keyRelease(KeyEvent.VK_TAB);
                    }
                    robot.delay(2000);
                    robot.keyPress(KeyEvent.VK_META);
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(KeyEvent.VK_G);
                    robot.keyRelease(KeyEvent.VK_META);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                    robot.keyRelease(KeyEvent.VK_G);
                    robot.keyPress(KeyEvent.VK_META);
                    robot.keyPress(KeyEvent.VK_V);
                    robot.keyRelease(KeyEvent.VK_META);
                    robot.keyRelease(KeyEvent.VK_V);
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.keyRelease(KeyEvent.VK_ENTER);
                    robot.delay(2000);
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.keyRelease(KeyEvent.VK_ENTER);
                    break;
                case "win":
                    LOG.info("OS: " + platform);
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    robot.keyPress(KeyEvent.VK_V);
                    robot.keyRelease(KeyEvent.VK_V);
                    robot.keyRelease(KeyEvent.VK_CONTROL);
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.keyRelease(KeyEvent.VK_ENTER);
                    break;
                case "linux":
                    LOG.info("OS: " + platform);
                    robot.keyPress(KeyEvent.VK_META);
                    robot.delay(500);                    // 200~500
                    robot.keyPress(KeyEvent.VK_V);
                    robot.keyRelease(KeyEvent.VK_V);
                    robot.keyRelease(KeyEvent.VK_META);
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.keyRelease(KeyEvent.VK_ENTER);
                    break;
                default:
                    // In mac machine for chrome you need to switch focus to upload dialog again
                    // I have saved browser name in properties file you can pass it as string.
                    if (browserName.equalsIgnoreCase("chrome")) {
                        robot.keyPress(KeyEvent.VK_META);
                        robot.keyPress(KeyEvent.VK_TAB);
                        robot.keyRelease(KeyEvent.VK_META);
                        robot.keyRelease(KeyEvent.VK_TAB);
                    }
                    robot.delay(2000);
                    robot.keyPress(KeyEvent.VK_META);
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(KeyEvent.VK_G);
                    robot.keyRelease(KeyEvent.VK_META);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                    robot.keyRelease(KeyEvent.VK_G);
                    robot.keyPress(KeyEvent.VK_META);
                    robot.keyPress(KeyEvent.VK_V);
                    robot.keyRelease(KeyEvent.VK_META);
                    robot.keyRelease(KeyEvent.VK_V);
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.keyRelease(KeyEvent.VK_ENTER);
                    robot.delay(2000);
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.keyRelease(KeyEvent.VK_ENTER);
                    break;
            }
        } catch (Exception e) {
            LOG.keywordAppender().appendArgument("Message", e.getMessage());
        }
    }

    public void fileUploadByJS(String locator, String fileLocation) {
        File filePath = new File(fileLocation);

        LOG.keywordAppender().appendLocator(locator);
        WebElement element = finder.find(locator);

        String JS_DROP_FILE =
                "var target = arguments[0]," +
                        "    offsetX = arguments[1]," +
                        "    offsetY = arguments[2]," +
                        "    document = target.ownerDocument || document," +
                        "    window = document.defaultView || window;" +
                        "" +
                        "var input = document.createElement('INPUT');" +
                        "input.type = 'file';" +
                        "input.style.display = 'none';" +
                        "input.onchange = function () {" +
                        "  var rect = target.getBoundingClientRect()," +
                        "      x = rect.left + (offsetX || (rect.width >> 1))," +
                        "      y = rect.top + (offsetY || (rect.height >> 1))," +
                        "      dataTransfer = { files: this.files };" +
                        "" +
                        "  ['dragenter', 'dragover', 'drop'].forEach(function (name) {" +
                        "    var evt = document.createEvent('MouseEvent');" +
                        "    evt.initMouseEvent(name, !0, !0, window, 0, 0, 0, x, y, !1, !1, !1, !1, 0, null);" +
                        "    evt.dataTransfer = dataTransfer;" +
                        "    target.dispatchEvent(evt);" +
                        "  });" +
                        "" +
                        "  setTimeout(function () { document.body.removeChild(input); }, 25);" +
                        "};" +
                        "document.body.appendChild(input);" +
                        "return input;";

        WebElement input =  (WebElement)this.executor.executeScript(JS_DROP_FILE, element, 0, 0);
        input.sendKeys(filePath.getAbsoluteFile().toString());
    }

    public boolean waitElementToBePresent(String locator, int timeOut){
        LOG.keywordAppender().appendLocator(locator);
        boolean isPresent= false;

        try{
            (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(parseLocatorBy(locator)));
            isPresent = true;
        }catch (Exception e){
            isPresent = false;
        }finally {
            LOG.keywordAppender().append(String.format("%s", isPresent));
        }

        return isPresent;
    }

    public boolean waitElementToBeVisible(String locator, int timeOut){
        LOG.keywordAppender().appendLocator(locator);
        boolean isVisible = false;

        try{
            (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.visibilityOfElementLocated(parseLocatorBy(locator)));
            isVisible = true;
        }catch (Exception e){
            isVisible = false;
        }finally {
            LOG.keywordAppender().append(String.format("%s", isVisible));
        }

        return isVisible;
    }

    public boolean waitElementToBeClickable(String locator, int timeOut){
        LOG.keywordAppender().appendLocator(locator);
        boolean isClickable= false;

        try{
            (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.elementToBeClickable(parseLocatorBy(locator)));
            isClickable = true;
        }catch (Exception e){
            isClickable = false;
        }finally {
            LOG.keywordAppender().append(String.format("%s", isClickable));
        }

        return isClickable;
    }

    public boolean waitElementToBeInvisible(String locator, int timeOut){
        LOG.keywordAppender().appendLocator(locator);
        boolean isInvisible = false;

        try{
            (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.invisibilityOfElementLocated(parseLocatorBy(locator)));
            isInvisible = true;
        }catch (Exception e){
            isInvisible = false;
        }finally {
            LOG.keywordAppender().append(String.format("%s", isInvisible));
        }

        return isInvisible;
    }



    private By parseLocatorBy(String locator){
        String [] stringList = locator.split("=", 2);

        String locatorBy = stringList[0];
        String locatorValue = stringList[1];

        System.out.println("by: " + locatorBy);
        System.out.println("value: " +  locatorValue);


        switch (locatorBy){
            case "id":
                return By.id(locatorValue);
            case "name":
                return By.name(locatorValue);
            case  "xpath":
                return By.xpath(locatorValue);
            case  "className":
                return By.className(locatorValue);
            case "css":
                return By.cssSelector(locatorValue);
            case "linkText":
                return By.linkText(locatorValue);
            case "partialLinkText":
                return By.partialLinkText(locatorValue);
            default:
                LOG.keywordAppender().append("Invalid locatorBy: " + locatorBy);
                return null;
        }
    }
}
