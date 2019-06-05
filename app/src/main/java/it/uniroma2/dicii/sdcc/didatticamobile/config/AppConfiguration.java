package it.uniroma2.dicii.sdcc.didatticamobile.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/*
 * An AppConfiguration object encapsulates the configuration parameters of the app. Those parameters
 * are read from a .properties file and stored in a Property field of the object. The class is
 * implemented as a singleton.
 * */
public class AppConfiguration {

    private static AppConfiguration instance = null;
    private static Properties properties;
    private static final String CONFIGURATION_FILE_PATH = "assets/application.properties";

    private AppConfiguration() {}

    public static synchronized AppConfiguration getInstance() throws IOException {
        if (instance == null) {
            instance = new AppConfiguration();
            properties = new Properties();
            InputStream inputStream = instance.getClass().getClassLoader().getResourceAsStream(CONFIGURATION_FILE_PATH);
            properties.load(inputStream);
            inputStream.close();
        }
        return instance;
    }

    public String readProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}
