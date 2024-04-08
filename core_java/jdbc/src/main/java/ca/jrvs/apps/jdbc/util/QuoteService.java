package ca.jrvs.apps.jdbc.util;

import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.dto.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class QuoteService {

    public Connection connection;
    private static Logger logger = LoggerFactory.getLogger(QuoteService.class);

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
        DatabaseConnectionManager dcm = new DatabaseConnectionManager();

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
            logger.error("Failed to write to Quote table for symbol " + ticker + " in QuoteService. " +
                    "SQL error in quoteDAO-> " + e);
        }
        // return an empty quote if there's a failure
        logger.warn("Could not find symbol " + ticker + ". fetchQuoteInfo returned empty Quote.");
        return Optional.empty();
    }

}
