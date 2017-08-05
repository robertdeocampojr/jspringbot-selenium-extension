package com.jspringbot.selenium.extension;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;


public class DesiredCapabilitiesBean implements InitializingBean, DisposableBean {
    private static final Logger LOGGER = Logger.getLogger(org.jspringbot.keyword.selenium.DesiredCapabilitiesBean.class);

    private DesiredCapabilities capabilities;


    @Override
    public void destroy() throws Exception {
    }

    public void afterPropertiesSet() throws Exception {
    }

    public DesiredCapabilitiesBean(DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    public void setBrowserName(String browserName) {
        if (!StringUtils.equalsIgnoreCase(browserName, "none")) {
            capabilities.setCapability("browser", browserName);
        }
    }
    public void setPlatform(String platform) {
        if (!StringUtils.equalsIgnoreCase(platform, "none")) {
            capabilities.setCapability(CapabilityType.PLATFORM, platform);
        }
    }
    public void setVersion(String version) {
        if (!StringUtils.equalsIgnoreCase(version, "none")) {
            capabilities.setCapability(CapabilityType.VERSION, version);
        }
    }


    //Browserstack-specific capabilities
    public void setBrowser(String browser) {
        if (!StringUtils.equalsIgnoreCase(browser, "none")) {
            capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
        }
    }
    public void setBrowserVersion(String browserVersion) {
        if (!StringUtils.equalsIgnoreCase(browserVersion, "none")) {
            capabilities.setCapability("browser_version", browserVersion);
        }
    }
    public void setOs(String os) {
        if (!StringUtils.equalsIgnoreCase(os, "none")) {
            capabilities.setCapability("os", os);
        }
    }
    public void setOsVersion(String osVersion) {
        if (!StringUtils.equalsIgnoreCase(osVersion, "none")) {
            capabilities.setCapability("os_version", osVersion);
        }
    }
    public void setDeviceName(String deviceName) {
        if (!StringUtils.equalsIgnoreCase(deviceName, "none")) {
            capabilities.setCapability("device", deviceName);
        }
    }
    public void setBrowserstackDebug(boolean browserstackDebug) {
        if (!StringUtils.equalsIgnoreCase(String.valueOf(browserstackDebug), "none")) {
            capabilities.setCapability("browserstack.debug", browserstackDebug);
        }
    }
    public void setProject(String project) {
        if (!StringUtils.equalsIgnoreCase(project, "none")) {
            capabilities.setCapability("project", project);
        }
    }
    public void setBuild(String build) {
        if (!StringUtils.equalsIgnoreCase(build, "none")) {
            capabilities.setCapability("build", build);
        }
    }
}
