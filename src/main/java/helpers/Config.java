package helpers;

import common.PROPERTY;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Logger;

public class Config {
    private static Properties properties = new Properties();
    private static Logger LOGGER = Logger.getLogger(Config.class.getName());

//    public static final String RESOURCES_PATH = "/var/task/";
//    public static final String TEMP = "/tmp/";
    public static final String RESOURCES_PATH_L = "local" + File.separator;
//    public static final String TEMP_L = "C:\\Users\\Valerii_Sushkov\\test\\sFrame\\tmp\\";
//    public static final String DEFAULT_RUN_PROPERTIES_PATH = RESOURCES_PATH + "local/config.properties";
    public static final String DEFAULT_RUN_PROPERTIES_PATH_L = RESOURCES_PATH_L + "config.properties";
//    public static final String DRIVER_NAME = "phantomjs";
//    public static final String SCREENSHOT_PATH = "/tmp/";


    public static void readProperties() {
        try {
            properties.load(new FileInputStream(
                    new File(DEFAULT_RUN_PROPERTIES_PATH_L).getAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException("Unable to read properties", e);
        }
        Arrays.stream(PROPERTY.values())
                .forEach(key -> LOGGER.info("Read property " +
                                key.name() + ":'" + key.getValueName() + "' -> '" + getProperty(key) + "'"));
    }

    public static void readPropertiesFromMap(final Map<String, String> input) {
        input.entrySet().stream().forEach(entry -> properties.setProperty(entry.getKey(), entry.getValue()));
    }

    public static String getProperty(final PROPERTY propName) {
        return Optional.ofNullable(properties.getProperty(propName.getValueName())).orElse(StringUtils.EMPTY);
    }

    public static void setProperty(final PROPERTY propName, final String value) {
        properties.setProperty(propName.getValueName(), value);
    }
}