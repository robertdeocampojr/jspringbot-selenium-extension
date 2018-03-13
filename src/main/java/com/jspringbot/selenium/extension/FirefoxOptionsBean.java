package com.jspringbot.selenium.extension;


import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jspringbot.keyword.selenium.OsCheck;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.rauschig.jarchivelib.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FirefoxOptionsBean implements InitializingBean, DisposableBean {
    private static final Logger LOGGER = Logger.getLogger(FirefoxOptionsBean.class);

    private FirefoxOptions options;
    private File baseDir;
    private String geckoDriverVersion;
    private File tempDir;
    private File geckoDriverFile;

    public void destroy() throws Exception {
        if (this.geckoDriverFile != null && this.geckoDriverFile.isFile() && this.geckoDriverFile.isFile()) {
            this.geckoDriverFile.delete();
        }

        if (this.tempDir != null && this.tempDir.isDirectory()) {
            this.tempDir.delete();
        }

    }

    public void afterPropertiesSet() throws Exception {
    }

    public FirefoxOptionsBean(FirefoxOptions options) {
        this.options = options;
    }

    public void setFirefoxProfile(FirefoxProfile profile) {
        options.setProfile(profile);
    }

    public void setIsHeadless(boolean isHeadless) {
        options.setHeadless(isHeadless);
    }

    public void setGeckoDriverVersion(String geckoDriverVersion) {
        this.geckoDriverVersion = geckoDriverVersion;
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

    public void setBaseDir(String baseStrDir) {
        if (StringUtils.isNotBlank(baseStrDir) && !StringUtils.equalsIgnoreCase(baseStrDir, "none")) {
            this.baseDir = new File(baseStrDir);
            if (!this.baseDir.isDirectory()) {
                this.baseDir.mkdirs();
            }
        }

    }

    public void setGeckoDrivers(Map<OsCheck.OSType, Resource> geckoDrivers) throws IOException {
        System.out.println("test");
        OsCheck.OSType osType = OsCheck.getOperatingSystemType();
        Resource geckoDriver = (Resource)geckoDrivers.get(osType);
        if (geckoDriver == null) {
            throw new IllegalArgumentException("Unsupported OS " + osType.name());
        } else {
            File driverDir = this.createDriverDir();
            File downloadedFile = new File(driverDir, geckoDriver.getFilename());

            if (!downloadedFile.isFile()) {
                LOGGER.info("Gecko driver version: " + this.geckoDriverVersion);
                System.out.println("Downloading driver: " + geckoDriver.getURL());
                IOUtils.copy(geckoDriver.getInputStream(), new FileOutputStream(downloadedFile));
            }

            LOGGER.info("Gecko driver file: " + downloadedFile.getAbsolutePath());
            System.out.println("Gecko Raw File: " + downloadedFile.getAbsolutePath());
            this.tempDir = Files.createTempDir();
            System.out.println("Temp Folder: " + tempDir);


            File archive = new File(downloadedFile.getAbsolutePath());
            File destination = new File(tempDir + "/");


            this.geckoDriverFile = new File(destination.getAbsolutePath() + "/geckodriver");
            System.out.println(geckoDriverFile.getAbsolutePath());

            Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");
            archiver.extract(archive, destination);


            System.setProperty("webdriver.gecko.driver", geckoDriverFile.getAbsolutePath());
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
                entryFile = new File(dir, entry.getName());

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

}
