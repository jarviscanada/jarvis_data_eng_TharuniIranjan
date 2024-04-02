package ca.jrvs.apps.jdbc;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        StockQuoteController sqc = new StockQuoteController();
        sqc.initClient();
    }
}
