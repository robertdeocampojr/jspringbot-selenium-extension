# Selenium Extension for Jspringbot

"jspringbot-selenium-extension" - This is a supporting library for jspringbot to use Browserstack

## Getting Started
    <dependency>
      <groupId>org.jspringbot</groupId>
      <artifactId>jspringbot-selenium-extension</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>

### Prerequisites

Your project is using jspringbot libraries.

### Installing
    Highlight Element           ${locator}
    Set Implicit Wait Time      0
    
    In jspringbot-global.xml:
    <import resource="classpath:spring/spring-selenium-browserstack.xml"/>
    
    In jspringbot.properties:
    selenium.browserstack.browser=${selenium.browserstack.browser}
    selenium.browserstack.browserVersion=${selenium.browserstack.browserVersion}
    selenium.browserstack.os=${selenium.browserstack.os}
    selenium.browserstack.os.version=${selenium.browserstack.os.version}
    selenium.browserstack.debug=true
    selenium.browserstack.project=TEST.PROJECT
    selenium.browserstack.build=TEST.BUILD
    