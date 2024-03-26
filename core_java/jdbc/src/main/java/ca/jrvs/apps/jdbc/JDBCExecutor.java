package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class JDBCExecutor {
    public Connection connection;

    public JDBCExecutor(String databaseName) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(databaseName);

        try {
            connection = dcm.getConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) throws JsonProcessingException {
//        JDBCExecutor jdbc = new JDBCExecutor("stock_quote");
//        jdbc.readQuote("all");
//        jdbc.createQuote("AAPL");
//        jdbc.createQuote("MSFT");
//        jdbc.readQuote("all");
//        jdbc.deleteQuote("AAPL");
//        jdbc.readQuote("all");
//    }

    public boolean doesExist(QuoteDao qd, String s) {
        Optional<Quote> quoteO = qd.findById(s);
        return quoteO.get().getSymbol() != null;
    }

    public void createQuote (String s) throws JsonProcessingException {
        // Create
        QuoteDao quoteDAO = new QuoteDao(connection);

        if (doesExist(quoteDAO, s)) {
            System.out.println("Symbol already added");
        } else {
            QuoteHTTPHelper qhh = new QuoteHTTPHelper();
            Quote quote = qhh.fetchQuoteInfo(s);
            if (quote.getSymbol() != null) {
                quoteDAO.save(quote);
            } else {
                System.out.println("Symbol does not exist");
            }
        }
    }
    public void readQuote(String s){
        QuoteDao quoteDAO = new QuoteDao(connection);
        if (s.equals("all")) {
            // Find all
            Iterable<Quote> quoteI = quoteDAO.findAll();
            for (Quote q : quoteI) {
                System.out.println(q.toString());
            }
        } else {
            // Find by ID
            if (!doesExist(quoteDAO, s)) {
                System.out.println("Symbol not found");
            } else {
                Optional<Quote> quoteO = quoteDAO.findById(s);
                System.out.println(quoteO.toString());
            }
        }
    }
    public void deleteQuote(String s){
        QuoteDao quoteDAO = new QuoteDao(connection);
        if (s.equals("all")) {
            // Delete all
            quoteDAO.deleteAll();

        } else {
            // Delete by ID
            if (!doesExist(quoteDAO, s)) {
                System.out.println("Symbol not found");
            } else {
                quoteDAO.deleteById(s);
            }
        }

    }
}
