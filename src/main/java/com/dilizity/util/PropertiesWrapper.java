package com.dilizity.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesWrapper {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesWrapper.class);

    private static PropertiesWrapper instance;
    private Properties properties;
    private String fileName;

    private PropertiesWrapper(String fileName) {
        try (FnTraceWrap trace = new FnTraceWrap(fileName)) {
            this.fileName = fileName;
            properties = new Properties();
            loadProperties();
        }
    }

    private PropertiesWrapper() {
        try (FnTraceWrap trace = new FnTraceWrap()) {
            this.fileName = "config.properties";
            properties = new Properties();
            loadProperties();
        }
    }

    public static PropertiesWrapper getInstance(String fileName) {
        try (FnTraceWrap trace = new FnTraceWrap(fileName)) {
            if (instance == null) {
                synchronized (PropertiesWrapper.class) {
                    if (instance == null) {
                        instance = new PropertiesWrapper(fileName);
                    }
                }
            }
            return instance;
        }
    }

    public static PropertiesWrapper getInstance() {
        try (FnTraceWrap trace = new FnTraceWrap()) {
            if (instance == null) {
                synchronized (PropertiesWrapper.class) {
                    if (instance == null) {
                        instance = new PropertiesWrapper();
                    }
                }
            }
            return instance;
        }
    }

    private void loadProperties() {
        try (FnTraceWrap trace = new FnTraceWrap()) {
            Path jarPath = Paths.get(PropertiesWrapper.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
            // Load properties file from the same directory
            logger.info("jarPath=>{}, filename=>{}" , jarPath.toString(), fileName);
            File propertiesFile = new File(jarPath.toFile(), fileName);
            try (FileInputStream input = new FileInputStream(propertiesFile)) {
                properties.load(input);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //throw new RuntimeException(e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
