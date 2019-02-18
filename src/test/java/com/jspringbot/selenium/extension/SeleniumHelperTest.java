/*
 * Copyright (c) 2012. JSpringBot. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The JSpringBot licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jspringbot.selenium.extension;


import org.jspringbot.keyword.selenium.SeleniumHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Paths;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/spring-selenium-extension-chrome.xml"})
public class SeleniumHelperTest {


    @Autowired
    public WebDriver webdriver;
    @Autowired
    public SeleniumExtensionHelper  extensionHelper;
    @Autowired
    public SeleniumHelper  helper;





    public void testGetTitle() throws InterruptedException {
        webdriver.get("http://www.google.com");
        WebElement element = webdriver.findElement(By.name("q"));

        Thread.sleep(5000);
        element.sendKeys("BrowserStack");
        element.submit();

        System.out.println(webdriver.getTitle());
        webdriver.quit();
    }

    public void testHelpers3() throws InterruptedException {
        Paths.get("classpath:/drivers/mac/geckodriver");
    }



    public void testHelpers() throws InterruptedException {
        helper.navigateTo("http://www.google.com");
        extensionHelper.highlightElement("name=q");
        helper.sendKeys("name=q", "BrowserStack");
        helper.sendKeys("name=q", "cord=ENTER");
        System.out.println(extensionHelper.isElementDisplayed("name=q"));
    }

    @Test
    public void testHelpers2() throws Exception {
        //System.getProperty("user.dir");

        helper.navigateTo("https://robert.app.haveninc.com/login");
        helper.sendKeys("xpath=//input[@ng-model='signin.credentials.email']", "robertadmin@haveninc.com");
        helper.sendKeys("xpath=//input[@ng-model='signin.credentials.password']", "haven123!");
        helper.clickElement("xpath=//button[contains(text(), 'Log In')]");

        helper.waitTillElementFound("xpath=//div[@id='user-dropdown'][@class='user-nav-dropdown-btn ng-binding']");
        Thread.sleep(5000);

        helper.clickElement("xpath=//a[@class='dashboard-dropdown' and contains(.,'Shipments')]");
        helper.clickElement("id=nav-orders");
        helper.waitTillElementFound("xpath=//div[@class='filter-input']/input");
        helper.sendKeys("xpath=//div[@class='filter-input']/input", "331544");
        helper.clickElement("xpath=//a[contains(.,'331544')]");
        helper.waitTillElementFound("xpath=//h2[contains(.,'Shipment Details')]");

        Thread.sleep(5000);
        helper.clickElement("xpath=//div[@class='tab' and contains(.,'Documents')]");
        Thread.sleep(2000);
        //helper.clickElement("xpath=//button[@title='Click to upload a Test QA Name - No assignee' and @type='file']");
        Thread.sleep(2000);

        //helper.sendKeys("xpath=//button[@title='Click to upload a Test QA Name - No assignee' and @type='file']","/Users/robert/Pictures/2019-02-15_1821b.png");
        extensionHelper.fileUploadByJS("","/Users/robert/Pictures/2019-02-15_1821b.png");
        helper.waitTillElementFound("xpath=//button[@class='btn btn-primary pull-right ng-scope']");
        webdriver.quit();
    }





}