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


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/spring-selenium-extension-firefox.xml"})
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

    public void testHelpers() throws InterruptedException {
        helper.navigateTo("http://www.google.com");
        extensionHelper.highlightElement("name=q");
        helper.sendKeys("name=q", "BrowserStack");
        helper.sendKeys("name=q", "cord=ENTER");
        System.out.println(extensionHelper.isElementDisplayed("name=q"));
    }

    @Test
    public void testHelpers2() throws InterruptedException {
        helper.navigateTo("http://qaauto1.aws.ttxcloud.com/sx200/login.xhtml");
        helper.sendKeys("id=username", "ttx");
        helper.sendKeys("id=password", "skyTrax");
        helper.clickElement("id=loginBtn");
        helper.clickElement("id=nav_unitsmenu");
        Thread.sleep(1000);
        helper.clickElement("id=unitsmenu_sites");
        Thread.sleep(10000);
    }

}