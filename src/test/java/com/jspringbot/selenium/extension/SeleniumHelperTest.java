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


import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.jspringbot.keyword.selenium.SeleniumHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/spring-selenium-extension-chrome.xml"})
public class SeleniumHelperTest {


    @Autowired
    public WebDriver webdriver;
    @Autowired
    public SeleniumExtensionHelper  extensionHelper;
    @Autowired
    public UtilityHelper  util;
    @Autowired
    public SeleniumHelper  helper;
    @Autowired
    public SSHExtensionHelper  sshHelper;

    static int lport;
    static String rhost;
    static int rport;

    @Test
    public void testSSHForwarding2() throws InterruptedException {
        sshHelper.sshConnectDatabaseByRsa();
        Thread.sleep(60000);
        sshHelper.ssSessionDisconnect();
    }


    public void testSSHForwarding(){
        String user = "robert";
        String password = "Rectawt6";
        String host = "pharos.haveninc.com";
        int port=22;


        try{
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);
            lport = 3309;
            rhost = "stage.db.hvn.io";
            rport = 3306;
            //session.setPassword(password);


            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            jsch.addIdentity("~/.ssh/id_rsa", password);
            System.out.println("Establishing Connection...");

            //jsch.setKnownHosts("~/.ssh/known_hosts");


            session.connect();
            int assinged_port = session.setPortForwardingL(lport, rhost, rport);
            System.out.println("localhost:"+ assinged_port + " -> " + rhost + ":" +rport);
            Thread.sleep(30000);


            session.disconnect();
            System.out.println("DONE!!!");
        }catch (Exception e){
            System.out.println("Error Connecting");
        }
    }


    public void testSSH() throws InterruptedException {

        String host = "pharos.haveninc.com";
        String user = "robert";
        String password = "password";
        String command = "ls -ltr";
        try {
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            //jsch.addIdentity("/zeta/actions-dev.pem");
            // Create a JSch session to connect to the server
            Session session = jsch.getSession(user, host, 22);

            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            //jsch.setKnownHosts("~/.ssh/known_hosts");
            jsch.addIdentity("~/.ssh/id_rsa", "Rectawt6");

            session.setConfig("StrictHostKeyChecking", "no");

            //session.setPassword(password);
            //session.setConfig(config);
            // Establish the connection
            session.connect(30000);
            System.out.println("Connected...");

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    System.out.println("Exit Status: "
                            + channel.getExitStatus());
                    break;
                }
                Thread.sleep(1000);
            }
            channel.disconnect();
            session.disconnect();
            System.out.println("DONE!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void testRegex() throws InterruptedException {
        String value ="Claim BCLM WC 000000000010 has been successfully saved.";
        String regrex = "Claim (.*?) has been successfully saved.";
        util.getMatchString(value, regrex);
    }


    public void testHelpers() throws InterruptedException {
        helper.navigateTo("http://www.google.com");
        extensionHelper.highlightElement("name=q");
        helper.sendKeys("name=q", "BrowserStack");
        helper.sendKeys("name=q", "cord=ENTER");
        System.out.println(extensionHelper.isElementDisplayed("name=q"));
    }


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



    public void parseLocatorBy() throws InterruptedException {
            String locator = "xpath=//[@class='test']";

            String [] stringList = locator.split("=", 2);

            String locatorBy = stringList[0];
            String locatorValue = stringList[1];

            System.out.println("locatorBy: " + locatorBy);
            System.out.println("locatorValue: " +  locatorValue);


            switch (locatorBy){
                case "id":
                    By.id(locatorValue);
                    break;
                case "name":
                    By.name(locatorValue);
                case  "xpath":
                    By.xpath(locatorValue);
                case  "className":
                    By.className(locatorValue);
                case "cssSelector":
                    By.cssSelector(locatorValue);
                case "linkText":
                    By.linkText(locatorValue);
                case "partialLinkText":
                    By.partialLinkText(locatorValue);
            }
    }



}