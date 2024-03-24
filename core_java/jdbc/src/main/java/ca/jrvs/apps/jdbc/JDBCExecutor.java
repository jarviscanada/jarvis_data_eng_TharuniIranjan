package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class JDBCExecutor {

    public JDBCExecutor(String tableName, String requestType, String symbolName) {
        String databaseName = "stock_quote";
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(databaseName);


        try {
            Connection connection = dcm.getConnection();
            JDBCExecutor jdbc = new JDBCExecutor(tableName, requestType, symbolName);
            if (tableName.equals("quote")) {
                QuoteDao quoteDAO = new QuoteDao(connection);
                if (requestType.equals("create")) {
                    jdbc.createQuote(quoteDAO, symbolName);
                } else if (requestType.equals("select")) {
                    jdbc.readQuote(quoteDAO, symbolName);
                } else if (requestType.equals("delete")) {
                    jdbc.deleteQuote(quoteDAO, symbolName);
                }
            } 

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

    public void createQuote (QuoteDao qd, String s) throws JsonProcessingException {
        // Create

        QuoteHTTPHelper qhh = new QuoteHTTPHelper();
        Quote quote = qhh.fetchQuoteInfo(s);
        qd.save(quote);
    }
    public void readQuote(QuoteDao qd, String s){
        if (s.equals("all")) {
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
                System.out.println(quoteO.toString());
            }
        }
    }
    public void deleteQuote(QuoteDao qd, String s){
        if (s.equals("all")) {
            // Delete all
            qd.deleteAll();

        } else {
            // Delete by ID
            qd.deleteById(s);
        }

    }
}
