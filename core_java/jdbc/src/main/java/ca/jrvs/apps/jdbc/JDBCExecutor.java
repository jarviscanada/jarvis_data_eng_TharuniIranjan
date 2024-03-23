package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static ca.jrvs.apps.jdbc.DatabaseConnectionManager.getProperty;

public class JDBCExecutor {
    private static final String configFile = "update.properties";

    public static void main(String[] args){

        String databaseName = "stock_quote";
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(databaseName);
        String index1 = "AMZN";
        String index2 = "";

        try {
            Connection connection = dcm.getConnection();
            QuoteDao quoteDAO = new QuoteDao(connection);
//            delete(quoteDAO, index1);
//            read(quoteDAO, index2);
//            create(quoteDAO);
            read(quoteDAO, index2);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void create(QuoteDao qd){
        // Create
        Quote quote = new Quote();
        quote.setSymbol(getProperty("quote.symbol.new", configFile));
        quote.setOpen(getProperty("quote.open.new", configFile));
        quote.setHigh(getProperty("quote.high.new",configFile));
        quote.setLow(getProperty("quote.low.new", configFile));
        quote.setPrice(getProperty("quote.price.new", configFile));
        quote.setVolume(getProperty("quote.volume.new", configFile));
        quote.setLatestTradingDay(getProperty("quote.lasttradedate.new", configFile));
        quote.setPreviousClose(getProperty("quote.lastclose.new", configFile));
        quote.setChange(getProperty("quote.change.new", configFile));
        quote.setChangePercent(getProperty("quote.changepercent.new", configFile));
        qd.save(quote);
    }
    public static void read(QuoteDao qd, String s){
        if (s.isEmpty()) {
            // Find all
            Iterable<Quote> quoteI = qd.findAll();
            for (Quote q : quoteI) {
                System.out.println(q.toString());
            }
        } else {
            // Find by ID
            Optional<Quote> quoteO = qd.findById(s);

            if (quoteO.isEmpty()) {
                System.out.println("Symbol not found");
            } else {
                JDBCExecutor.printResultsShort(quoteO.get());
            }
        }
    }
    public static void delete(QuoteDao qd, String s){
        if (s.isEmpty()) {
            // Delete all
            qd.deleteAll();

        } else {
            // Delete by ID
            qd.deleteById(s);
        }

    }

    public static void printResultsShort(Quote q) {
        System.out.print("symbol: " + q.getSymbol());
        System.out.print(", price: " +q.getPrice() + "\n");
    }
}
