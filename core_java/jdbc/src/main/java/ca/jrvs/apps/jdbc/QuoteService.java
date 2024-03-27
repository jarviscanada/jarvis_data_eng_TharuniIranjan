package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class QuoteService {
    private QuoteDao dao;
    private QuoteHTTPHelper httpHelper;
    public Connection connection;
    public String databaseName = "stock_quote";

    public boolean doesExist(QuoteDao qd, String s) {
        Optional<Quote> quoteO = qd.findById(s);
        return quoteO.get().getSymbol() != null;
    }

    /**
     * Fetches latest quote data from endpoint
     * @param ticker
     * @return Latest quote information or empty optional if ticker symbol not found
     */
    public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(databaseName);

        try {
            connection = dcm.getConnection();
            QuoteDao quoteDAO = new QuoteDao(connection);
            QuoteHTTPHelper qhh = new QuoteHTTPHelper();
            Quote quote = qhh.fetchQuoteInfo(ticker);
            // if the symbol is null it does not exist
            if (quote.getSymbol() != null) {
                // if the symbol is already in the quote db, make an update, else, create a new value
                if (doesExist(quoteDAO, ticker)) {
                    quoteDAO.update(quote);
                } else {
                    quoteDAO.save(quote);
                }
                return Optional.of(quote);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        // return an empty quote if there's a failure
        return Optional.empty();
    }

}
