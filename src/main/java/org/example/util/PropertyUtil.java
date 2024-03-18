package org.example.util;

import org.apache.commons.lang3.StringUtils;

public class PropertyUtil {

    @SuppressWarnings("SameParameterValue")
    public static String getProperty(String propertyName) {
        String sysProp = System.getProperty(propertyName);
        return StringUtils.isBlank(sysProp) ? System.getenv(propertyName) : sysProp;
    }

    public static String getSystemProperty(String key, String defaultValue) {
        String prop = System.getProperty(key);
        return StringUtils.isBlank(prop) ? defaultValue : prop;
    }
}
