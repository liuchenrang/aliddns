package org.xinghuo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author xinghuo
 */
class PropertiesUtils {
    private static Properties prop = new Properties();
    static {
        InputStream in = ClassLoader.getSystemResourceAsStream("ddns.properties");
        try {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static String get(String key) {
        return prop.getProperty(key);
    }
}
