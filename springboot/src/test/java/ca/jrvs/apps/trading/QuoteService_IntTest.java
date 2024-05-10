package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.entity.IexQuote;
import ca.jrvs.apps.trading.model.Quote;
import ca.jrvs.apps.trading.service.QuoteService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest(classes = {TestConfig.class, QuoteService.class})
@Sql({"classpath:schema.sql"})
public class QuoteService_IntTest {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private QuoteDao quoteDao;

    @Before
    public void setup() {
        quoteDao.deleteAll();
    }

    @Test
    public void findIexQuoteByTicker() {
        // check if data is retuned for valid ticker
        String ticker = "AAPL";
        IexQuote iexQuote = quoteService.findIexQuoteByTicker(ticker);
        assertNotNull(iexQuote);
        assertEquals(ticker, iexQuote.getSymbol());

        // check null is returned if invalid ticker
        String ticker2 = "INVALID_TICKER";
        IexQuote iexQuote2 = quoteService.findIexQuoteByTicker(ticker2);
        assertNull(iexQuote2);
    }

    @Test
    public void updateMarketData() {
        // add some quotes to the database and update MarketData
        List<String> tickers = Arrays.asList("AAPL", "GOOGL", "MSFT");
        quoteService.saveQuotes(tickers);
        quoteService.updateMarketData();

        // check if all the quotes were updated by checking if values are empty or not
        List<Quote> quotes = quoteService.findAllQuotes();
        assertNotNull(quotes);
        assertFalse(quotes.isEmpty());
        for (Quote quote : quotes) {
            assertNotNull(quote.getLastPrice());
            assertNotNull(quote.getBidPrice());
            assertNotNull(quote.getAskPrice());
            assertNotNull(quote.getBidSize());
            assertNotNull(quote.getAskSize());
        }
    }

    @Test
    public void saveQuote() {
        // add a new valid quote
        String ticker3 = "AAPL";
        Quote quote1 = quoteService.saveQuote(ticker3);
        // check if the returned quote is not null and its id matches the given ticker
        assertNotNull(quote1);
        assertEquals(ticker3, quote1.getId());

        // try to add a new invalid quote
        String ticker4 = "INVALID_TICKER1";
        Quote quote2 = quoteService.saveQuote(ticker4);
        // check if the returned quote is null
        assertNull(quote2);
    }

    @Test
    public void saveQuotes() {
        // create a list of valid tickers and save them
        List<String> tickers2 = Arrays.asList("AAPL", "GOOGL", "MSFT");
        List<Quote> savedQuotes = quoteService.saveQuotes(tickers2);

        // make sure the list of saved quotes returned are not empty and match the size of the given list
        assertNotNull(savedQuotes);
        assertFalse(savedQuotes.isEmpty());
        assertEquals(tickers2.size(), savedQuotes.size());
    }

    @Test
    public void findAllQuotes() {
        // Test logic for findAllQuotes method
        List<Quote> quotes = quoteService.findAllQuotes();

        assertNotNull(quotes);
        assertTrue(quotes.isEmpty());
    }

}
