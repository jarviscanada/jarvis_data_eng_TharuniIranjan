package ca.jrvs.apps.trading.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TradingAppTools {

    public static boolean validTicker(String ticker) {
        if (ticker == null || ticker.isEmpty()) {
            return false;
        }
        if (ticker.length() > 5) {
            return false;
        }
        return true;
    }

    public static Date convertStringtoDate(String input) throws ParseException {
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.parse(input);
    }

}