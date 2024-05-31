package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.dao.PositionDao;
import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.dto.Quote;
import ca.jrvs.apps.jdbc.util.DatabaseConnectionManager;
import ca.jrvs.apps.jdbc.util.QuoteHTTPHelper;
import ca.jrvs.apps.jdbc.util.QuoteService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class QuoteService_IntTest {
    public static DatabaseConnectionManager dcm;
    public static Connection c;
    public static QuoteDao quoteDao;
    public static PositionDao positionDao;
    public Quote msftQuote;
    public static Quote aaplQuote;
    public static QuoteService qs;

    @BeforeAll
    public static void init() throws SQLException {
        dcm = new DatabaseConnectionManager();
        c = dcm.getConnection();

        positionDao = new PositionDao(c);
        quoteDao = new QuoteDao(c);

        qs = new QuoteService();

        QuoteHTTPHelper qhh = new QuoteHTTPHelper();
        aaplQuote = qhh.fetchQuoteInfo("AAPL");

    }

    @BeforeEach
    public void setup() {
        msftQuote = new Quote();

        positionDao.deleteAll();
        quoteDao.deleteAll();

        msftQuote.setSymbol("MSFT");
        msftQuote.setOpen("425.24");
        msftQuote.setHigh("427.41");
        msftQuote.setLow("421.61");
        msftQuote.setPrice("422.86");
        msftQuote.setVolume("1806045");
        msftQuote.setLatestTradingDay("2024-03-25");
        msftQuote.setPreviousClose("428.74");
        msftQuote.setChange("-5.88");
        msftQuote.setChangePercent("-1.3715%");
        quoteDao.save(msftQuote);

    }

    @Test
    public void test_fetchApi_create() {
        Optional<Quote> testQuote1 = qs.fetchQuoteDataFromAPI("AAPL");
        assertFalse(testQuote1.isEmpty());
        assertEquals(testQuote1.get().getSymbol(), aaplQuote.getSymbol());
        assertEquals(testQuote1.get().getOpen(), aaplQuote.getOpen());
        assertEquals(testQuote1.get().getHigh(), aaplQuote.getHigh());
        assertEquals(testQuote1.get().getLow(), aaplQuote.getLow());
        assertEquals(testQuote1.get().getPrice(), aaplQuote.getPrice());
        assertEquals(testQuote1.get().getLatestTradingDay(), aaplQuote.getLatestTradingDay());
        assertEquals(testQuote1.get().getPreviousClose(), aaplQuote.getPreviousClose());
        assertEquals(testQuote1.get().getChange(), aaplQuote.getChange());
        assertEquals(testQuote1.get().getChangePercent(), aaplQuote.getChangePercent());
    }

    @Test
    public void test_fetchApi_update() {
        Optional<Quote> testQuote2 = qs.fetchQuoteDataFromAPI("MSFT");
        assertFalse(testQuote2.isEmpty());
    }

    @Test
    public void test_fetchApi_empty() {
        Optional<Quote> testQuote3 = qs.fetchQuoteDataFromAPI("MSFTX");
        assertTrue(testQuote3.isEmpty());
    }
}