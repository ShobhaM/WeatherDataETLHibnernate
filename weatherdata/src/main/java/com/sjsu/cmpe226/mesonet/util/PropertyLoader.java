package com.sjsu.cmpe226.mesonet.util;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

import com.sjsu.cmpe226.mesonet.constants.WeatherDataConstants;

/**
 * 
 * @author <a href="Bhargav@leantaas.com">Bhargav</a>
 */
public class PropertyLoader {
    private static PropertyLoader propertyLoaderObj = null;

    private static Logger         logger            = Logger.getLogger(PropertyLoader.class.getName());

    private PropertyLoader() {

    }

    public static synchronized Properties createConfigFileHook() {
        Properties prop = null;
        URL url = null;
        try {
            prop = new Properties();
            url = ClassLoader.getSystemResource(WeatherDataConstants.PROPERTY_FILE);
            prop.load(url.openStream());
        } catch (IOException e) {
            logger.severe("Property file was not loaded " + e.getMessage());
            e.printStackTrace();
        }
        return prop;
    }

    public synchronized PropertyLoader createPropertyLoaderObj() {

        if (propertyLoaderObj == null)
            propertyLoaderObj = new PropertyLoader();

        return propertyLoaderObj;
    }
}
