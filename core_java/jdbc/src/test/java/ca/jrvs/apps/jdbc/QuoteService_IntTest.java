package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class QuoteService_IntTest {
    public String databaseName = "stock_quote";
    public DatabaseConnectionManager dcm;
    public Connection c;
    public QuoteDao quoteDao;
    public PositionDao positionDao;
    public Quote msftQuote = new Quote();
    public Quote emptyQuote = new Quote();
    public Quote aaplQuote = new Quote();
    public QuoteService qs = new QuoteService();
    public String msftSymbol = "MSFT";
    public String fakeSymbol = "MSFTX";
    public String aaplSymbol = "AAPL";
    public String date;

    @Before
    public void init() throws SQLException {
        dcm = new DatabaseConnectionManager(databaseName);
        c = dcm.getConnection();

        positionDao = new PositionDao(c);
        positionDao.deleteAll();

        quoteDao = new QuoteDao(c);
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

        emptyQuote.setSymbol("null");
        emptyQuote.setOpen("null");
        emptyQuote.setHigh("null");
        emptyQuote.setLow("null");
        emptyQuote.setPrice("null");
        emptyQuote.setVolume("null");
        emptyQuote.setLatestTradingDay("null");
        emptyQuote.setPreviousClose("null");
        emptyQuote.setChange("null");
        emptyQuote.setChangePercent("null");

        QuoteHTTPHelper qhh = new QuoteHTTPHelper();
        aaplQuote = qhh.fetchQuoteInfo(aaplSymbol);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        LocalDate yesterdayDate = now.minusDays(1);
        date = dtf.format(yesterdayDate);
    }

    @Test
    public void test_fetchApi_create() throws JsonProcessingException {
        Optional<Quote> testQuote1 = qs.fetchQuoteDataFromAPI(aaplSymbol);
        assertFalse(testQuote1.isEmpty());
        assertEquals(testQuote1.get().getSymbol(), aaplQuote.getSymbol());
        assertEquals(testQuote1.get().getOpen(), aaplQuote.getOpen());
        assertEquals(testQuote1.get().getHigh(), aaplQuote.getHigh());
        assertEquals(testQuote1.get().getLow(), aaplQuote.getLow());
        assertEquals(testQuote1.get().getPrice(), aaplQuote.getPrice());
        assertEquals(testQuote1.get().getLatestTradingDay(), aaplQuote.getLatestTradingDay());
        assertEquals(testQuote1.get().getLatestTradingDay(), date);
        assertEquals(testQuote1.get().getPreviousClose(), aaplQuote.getPreviousClose());
        assertEquals(testQuote1.get().getChange(), aaplQuote.getChange());
        assertEquals(testQuote1.get().getChangePercent(), aaplQuote.getChangePercent());
    }

    @Test
    public void test_fetchApi_update() throws JsonProcessingException {
        Optional<Quote> testQuote2 = qs.fetchQuoteDataFromAPI(msftSymbol);
        assertFalse(testQuote2.isEmpty());
        assertEquals(testQuote2.get().getSymbol(), msftQuote.getSymbol());

        assertNotEquals(testQuote2.get().getOpen(), msftQuote.getOpen());
        assertNotEquals(testQuote2.get().getHigh(), msftQuote.getHigh());
        assertNotEquals(testQuote2.get().getLow(), msftQuote.getLow());
        assertNotEquals(testQuote2.get().getPrice(), msftQuote.getPrice());
        assertNotEquals(testQuote2.get().getPreviousClose(), msftQuote.getPreviousClose());
        assertNotEquals(testQuote2.get().getChange(), msftQuote.getChange());
        assertNotEquals(testQuote2.get().getChangePercent(), msftQuote.getChangePercent());

        assertNotEquals(testQuote2.get().getLatestTradingDay(), msftQuote.getLatestTradingDay());
        assertEquals(testQuote2.get().getLatestTradingDay(), date);
    }

    @Test
    public void test_fetchApi_empty() throws JsonProcessingException {
        Optional<Quote> testQuote3 = qs.fetchQuoteDataFromAPI(fakeSymbol);
        assertTrue(testQuote3.isEmpty());
    }
}