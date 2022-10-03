//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jspringbot.selenium.extension;

import com.google.common.io.Files;
import com.saucelabs.common.Utils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.jspringbot.keyword.selenium.OsCheck;
import org.jspringbot.keyword.selenium.OsCheck.OSType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DesiredCapabilitiesBean implements InitializingBean, DisposableBean {
    private static final Logger LOGGER = Logger.getLogger(DesiredCapabilitiesBean.class);
    private DesiredCapabilities capabilities;
    private Proxy proxy;
    private File baseDir;
    private Map<String, Object> chromeOptions = new HashMap();
    private Map<String, Object> mobileEmulation;
    private LoggingPreferences logPrefs;
    private String chromeDriverVersion;
    private String ieDriverVersion;
    private boolean archAutodetect = false;
    private String archValue = "32";
    private File tempDir;
    private File chromeDriverFile;

    public DesiredCapabilitiesBean(DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    public void setFirefoxProfile(FirefoxProfile profile) {
        this.capabilities.setCapability("firefox_profile", profile);
    }

    public void setChromeDriverVersion(String chromeDriverVersion) {
        this.chromeDriverVersion = chromeDriverVersion;
    }

    public void setIeDriverVersion(String ieDriverVersion) {
        this.ieDriverVersion = ieDriverVersion;
    }

    public void setBaseDir(String baseStrDir) {
        if (StringUtils.isNotBlank(baseStrDir) && !StringUtils.equalsIgnoreCase(baseStrDir, "none")) {
            this.baseDir = new File(baseStrDir);
            if (!this.baseDir.isDirectory()) {
                this.baseDir.mkdirs();
            }
        }

    }

    public void setArchAutodetect(boolean archAutodetect) {
        this.archAutodetect = archAutodetect;
    }

    public void setArchValue(String archValue) {
        this.archValue = archValue;
    }

    public void setChromeDrivers(Map<OSType, Resource> chromeDrivers) throws IOException {

        OSType osType = OsCheck.getOperatingSystemType();
        Resource chromeDriver = (Resource)chromeDrivers.get(osType);
        if (chromeDriver == null) {
            throw new IllegalArgumentException("Unsupported OS " + osType.name());
        } else {
            File driverDir = this.createDriverDir();
            File downloadedFile = new File(driverDir, chromeDriver.getFilename());

            if (!downloadedFile.isFile()) {
                LOGGER.info("Chrome driver version: " + this.chromeDriverVersion);
                System.out.println("Downloading driver: " + chromeDriver.getURL());
                IOUtils.copy(chromeDriver.getInputStream(), new FileOutputStream(downloadedFile));
            }

            LOGGER.info("Chrome driver file: " + downloadedFile.getAbsolutePath());
            System.out.println(downloadedFile.getAbsolutePath());
            this.tempDir = Files.createTempDir();
            this.chromeDriverFile = unzip(new FileInputStream(downloadedFile), this.tempDir);
            this.chromeDriverFile.setExecutable(true);
            System.setProperty("webdriver.chrome.driver", chromeDriverFile.getAbsolutePath());
            System.out.println(this.chromeDriverFile.getAbsolutePath());

        }
    }

    public void destroy() throws Exception {
        if (this.chromeDriverFile != null && this.chromeDriverFile.isFile() && this.chromeDriverFile.isFile()) {
            this.chromeDriverFile.delete();
        }

        if (this.tempDir != null && this.tempDir.isDirectory()) {
            this.tempDir.delete();
        }

    }

    private File createDriverDir() {
        File driverDir;
        if (this.baseDir != null) {
            driverDir = this.baseDir;
        } else {
            String userHome = System.getProperty("user.home");
            driverDir = new File(userHome, "jspringbot");
            if (!driverDir.isDirectory()) {
                driverDir.mkdirs();
            }
        }

        return driverDir;
    }

    public void setIeDriver(Map<String, Resource> resourceMap) throws IOException {
        File driverDir = this.createDriverDir();
        String arch = this.archValue;
        if (this.archAutodetect) {
            arch = System.getProperty("sun.arch.data.model");
        }

        Resource resource = (Resource)resourceMap.get(arch);
        File downloadedFile = new File(driverDir, resource.getFilename());
        if (!downloadedFile.isFile()) {
            LOGGER.info("Internet driver version" + this.ieDriverVersion);
            LOGGER.info("Downloading driver: " + resource.getURL());
            IOUtils.copy(resource.getInputStream(), new FileOutputStream(downloadedFile));
        }

        LOGGER.info("IE driver file: " + downloadedFile.getAbsolutePath());
        File driver = unzip(new FileInputStream(downloadedFile), driverDir);
        driver.setExecutable(true);
        System.setProperty("webdriver.ie.driver", driver.getAbsolutePath());
    }

    public void setChromeOptions(Map<String, Object> chromeOptions) {
        this.chromeOptions = chromeOptions;
    }

    public void setChromeDeviceMetrics(String deviceMetrics) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (StringUtils.isNotBlank(deviceMetrics) && !StringUtils.equalsIgnoreCase(deviceMetrics, "none")) {
            if (this.mobileEmulation == null) {
                this.mobileEmulation = new HashMap();
            }

            String[] metrics = StringUtils.split(deviceMetrics, "x");
            if (metrics.length < 2) {
                throw new IllegalArgumentException("Expected <width>x<height>x<pixel_ratio> but was " + deviceMetrics);
            }

            String[] NAMES = new String[]{"width", "height", "pixelRatio"};
            Class[] CLASSES = new Class[]{Integer.class, Integer.class, Double.class};
            Map<String, Object> deviceMetricsMap = new HashMap();

            for(int i = 0; i < metrics.length && i < NAMES.length; ++i) {
                deviceMetricsMap.put(NAMES[i], this.classValueOf(CLASSES[i], metrics[i]));
            }

            this.mobileEmulation.put("deviceMetrics", deviceMetricsMap);
        }

    }

    private Object classValueOf(Class clazz, String item) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = clazz.getDeclaredMethod("valueOf", String.class);
        return method.invoke(clazz, item);
    }

    public void setChromeDeviceEmulation(String deviceEmulation) {
        if (StringUtils.isNotBlank(deviceEmulation) && !StringUtils.equalsIgnoreCase(deviceEmulation, "none")) {
            if (this.mobileEmulation == null) {
                this.mobileEmulation = new HashMap();
            }

            this.mobileEmulation.put("deviceName", deviceEmulation);
        }

    }

    public void setChromeDeviceUserAgent(String userAgent) {
        if (StringUtils.isNotBlank(userAgent) && !StringUtils.equalsIgnoreCase(userAgent, "none")) {
            if (this.mobileEmulation == null) {
                this.mobileEmulation = new HashMap();
            }

            this.mobileEmulation.put("userAgent", userAgent);
        }

    }

    public void setChromeBrowserLog(String level) {
        if (StringUtils.isNotBlank(level) && !StringUtils.equalsIgnoreCase(level, "none")) {
            if (this.logPrefs == null) {
                this.logPrefs = new LoggingPreferences();
            }

            this.logPrefs.enable("browser", Level.parse(level));
        }

    }

    public void setChromePerformanceLog(String level) {
        if (StringUtils.isNotBlank(level) && !StringUtils.equalsIgnoreCase(level, "none")) {
            if (this.logPrefs == null) {
                this.logPrefs = new LoggingPreferences();
            }

            this.logPrefs.enable("performance", Level.parse(level));
        }

    }

    public static File unzip(InputStream in, File dir) throws IOException {
        ZipInputStream zin = null;
        byte[] buf = new byte[2048];
        File entryFile = null;

        try {
            zin = new ZipInputStream(in);

            ZipEntry entry;
            while((entry = zin.getNextEntry()) != null) {
                FileOutputStream out = null;
                final File zipEntryFile = new File(dir,entry.getName());
                if (!zipEntryFile.toPath().normalize().startsWith(dir.toPath().normalize())) {
                    throw new IOException("Bad zip entry");
                }
                entryFile = zipEntryFile;

                try {
                    out = new FileOutputStream(entryFile);

                    int len;
                    while((len = zin.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                } finally {
                    IOUtils.closeQuietly(out);
                }
            }
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(zin);
        }

        return entryFile;
    }

    public void setChromeLogFile(String logFile) {
        if (StringUtils.isNotBlank(logFile) && !StringUtils.equalsIgnoreCase(logFile, "none")) {
            File file = new File(logFile);
            File dir = file.getParentFile();
            if (dir != null && !dir.isDirectory()) {
                dir.mkdirs();
            }

            System.setProperty("webdriver.chrome.logfile", file.getAbsolutePath());
        }

    }

    public void setBrowserName(String browserName) {
        if (!StringUtils.equalsIgnoreCase(browserName, "none")) {
            this.capabilities.setCapability("browserName", browserName);
        }

    }

    public void setVersion(String version) {
        if (!StringUtils.equalsIgnoreCase(version, "none")) {
            this.capabilities.setCapability("version", version);
        }

    }

    public void setPlatform(String platform) {
        if (!StringUtils.equalsIgnoreCase(platform, "none")) {
            this.capabilities.setCapability("platform", platform);
        }

    }

    public void setPlatformVersion(String platformVersion) {
        if (!StringUtils.equalsIgnoreCase(platformVersion, "none")) {
            this.capabilities.setCapability("platformVersion", platformVersion);
        }

    }

    public void setDeviceName(String deviceName) {
        if (!StringUtils.equalsIgnoreCase(deviceName, "none")) {
            this.capabilities.setCapability("deviceName", deviceName);
        }

    }

    public void setDeviceOrientation(String deviceOrientation) {
        if (!StringUtils.equalsIgnoreCase(deviceOrientation, "none")) {
            if (StringUtils.equals(String.valueOf(this.capabilities.getCapability("deviceType")), "phone")) {
                this.capabilities.setCapability("deviceOrientation", deviceOrientation);
            } else {
                this.capabilities.setCapability("device-orientation", deviceOrientation);
            }
        }

    }

    public void setDeviceType(String deviceType) {
        if (!StringUtils.equalsIgnoreCase(deviceType, "none")) {
            this.capabilities.setCapability("deviceType", deviceType);
        }

    }

    public void setAppiumVersion(String appiumVersion) {
        if (!StringUtils.equalsIgnoreCase(appiumVersion, "none")) {
            this.capabilities.setCapability("appiumVersion", appiumVersion);
        }

    }

    public void setName(String name) {
        if (!StringUtils.equalsIgnoreCase(name, "none")) {
            this.capabilities.setCapability("name", name);
        }

    }

    public void setBuild(String build) {
        if (!StringUtils.equalsIgnoreCase(build, "none")) {
            this.capabilities.setCapability("build", build);
        } else {
            Map<String, Object> map = new HashMap();
            Utils.addBuildNumberToUpdate(map);
            if (map.containsKey("build")) {
                this.capabilities.setCapability("build", map.get("build"));
            }
        }

    }

    public void setTunnelId(String tunnelId) {
        if (!StringUtils.equalsIgnoreCase(tunnelId, "none")) {
            this.capabilities.setCapability("tunnel-identifier", tunnelId);
        }

    }

    public void setMaxDuration(String maxDuration) {
        if (!StringUtils.equalsIgnoreCase(maxDuration, "none")) {
            this.capabilities.setCapability("maxDuration", Integer.parseInt(maxDuration));
        }

    }

    public void setProxy(String proxyHost) {
        if (!StringUtils.equalsIgnoreCase(proxyHost, "none")) {
            this.proxy = new Proxy();
            this.proxy.setFtpProxy(proxyHost).setHttpProxy(proxyHost).setSslProxy(proxyHost);
            this.capabilities.setCapability("proxy", this.proxy);
        }

    }

    public void setSslProxy(String proxyHost) {
        if (!StringUtils.equalsIgnoreCase(proxyHost, "none")) {
            this.proxy = new Proxy();
            this.proxy.setSslProxy(proxyHost);
            this.capabilities.setCapability("proxy", this.proxy);
        }

    }

    public void setFtpProxy(String proxyHost) {
        if (!StringUtils.equalsIgnoreCase(proxyHost, "none")) {
            this.proxy = new Proxy();
            this.proxy.setFtpProxy(proxyHost);
            this.capabilities.setCapability("proxy", this.proxy);
        }

    }

    public void setHttpProxy(String proxyHost) {
        if (!StringUtils.equalsIgnoreCase(proxyHost, "none")) {
            this.proxy = new Proxy();
            this.proxy.setHttpProxy(proxyHost);
            this.capabilities.setCapability("proxy", this.proxy);
        }

    }

    public void setCapabilities(String properties) throws JSONException {
        if (!StringUtils.equalsIgnoreCase(properties, "none")) {
            JSONObject obj = new JSONObject(properties);
            Iterator itr = obj.keys();

            while(itr.hasNext()) {
                String key = (String)itr.next();
                this.capabilities.setCapability(key, obj.getString(key));
            }
        }

    }

    public void afterPropertiesSet() throws Exception {
        if (MapUtils.isNotEmpty(this.mobileEmulation)) {
            this.chromeOptions.put("mobileEmulation", this.mobileEmulation);
        }

        if (MapUtils.isNotEmpty(this.chromeOptions)) {
            this.capabilities.setCapability("chromeOptions", this.chromeOptions);
        }

        if (this.logPrefs != null) {
            this.capabilities.setCapability("loggingPrefs", this.logPrefs);
        }

    }
}
