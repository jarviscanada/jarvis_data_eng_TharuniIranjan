package ca.jrvs.apps.trading.util;

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

}
