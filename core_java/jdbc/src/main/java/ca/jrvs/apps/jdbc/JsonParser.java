package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class JsonParser {
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

    /***
     * Convert string to a specified type, whether its int, bool, date
     * @param value the string we want to change
     * @return the converted string
     */

    public static double convertStringToDouble(String value) {
        BigDecimal bd = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int convertStringToInt(String value) {
        return Integer.parseInt(value);
    }

    public static Date convertStringToDate(String value) throws ParseException {
        return Date.valueOf(value);
    }

    /***
     * Checks if an inputted value is of type integer
     * @param s the input
     * @return true if the string can be parsed to an integer and false otherwise
     */
    public static boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
