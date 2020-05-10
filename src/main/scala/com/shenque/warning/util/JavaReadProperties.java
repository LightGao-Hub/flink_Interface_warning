package com.shenque.warning.util;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

public class JavaReadProperties implements Serializable {

    public Properties read() {
        try {
            Properties properties = new Properties();
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config.properties");
            properties.load(inputStream);
            //根据config.properties 选择不同的properties
            String choice = properties.getProperty("properties.choice");
            InputStream path = this.getClass().getClassLoader().getResourceAsStream(choice);
            properties.load(path);
            return properties;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
