package ca.jrvs.apps.trading.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TradingAppTools {
    private static final String CONFIGFILE = "config.properties";

    /***
     * Convert a Java object to JSON string
     * @param object input object
     * @param prettyJson is it a pretty object
     * @param includeNullValues does it include Null values
     * @return JSON string
     * @throws JsonProcessingException when json processing fails
     */
    public static String toJson(Object object, boolean prettyJson, boolean includeNullValues) throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();

        if (!includeNullValues) {
            m.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }

        if (prettyJson) {
            m.enable(SerializationFeature.INDENT_OUTPUT);
        }

        return m.writeValueAsString(object);
    }


    /***
     * Parse JSON string to an object
     * @param json JSON str
     * @param clazz object class
     * @return object
     * @param <T> type
     * @throws IOException when it fails to read
     */
    public static <T> T toObjectFromJson(String json, Class clazz) throws IOException {
        ObjectMapper m = new ObjectMapper();
        return (T) m.readValue(json, clazz);
    }


    /**
     * Extracts data from the config file
     * @param property what type of value we are looking for
     * @return the value found in the config file
     */
    public static String getProperty(String property) {
        Properties properties = new Properties();
        try (InputStream inputStream = TradingAppTools.class.getClassLoader().getResourceAsStream(CONFIGFILE)) {
            properties.load(inputStream);
            return properties.getProperty(property);
        } catch (IOException e) {
            //e.printStackTrace();
            // logger.error("Failed to retrieve config property " + property + " in TradingAppTools-> " + e);
            return null;
        }
    }

    public String setURL(String tickers){
        return "";
    }

}
