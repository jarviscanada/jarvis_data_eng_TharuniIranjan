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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuoteService_UnitTest  {
    public static DatabaseConnectionManager dcm;
    public static Connection c;
    public static QuoteDao mockQuoteDao;
    public static QuoteDao quoteDao;
    public static PositionDao positionDao;
    public static QuoteHTTPHelper mockQhh;
    public static Quote msftQuote;
    public static Quote emptyQuote;
    public static QuoteService qs;
    public static QuoteService mockQS;

    @BeforeAll
    public static void init() throws SQLException {
        dcm = new DatabaseConnectionManager();
        c = dcm.getConnection();

        positionDao = new PositionDao(c);
        quoteDao = new QuoteDao(c);

        msftQuote = new Quote();
        emptyQuote = new Quote();
        qs = new QuoteService();

        mockQuoteDao = mock(QuoteDao.class);
        mockQhh = mock(QuoteHTTPHelper.class);
        mockQS = mock(QuoteService.class);
    }

    @BeforeEach
    public void setup() {
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
    public void test_doesExist() {
        assertTrue(qs.doesExist(quoteDao, "MSFT"));
        assertFalse(qs.doesExist(quoteDao, "MSFTX"));
    }

    @Test
    public void test_fetchApi() {
        when(mockQS.doesExist(quoteDao, "MSFT")).thenReturn(true);
        when(mockQhh.fetchQuoteInfo("MSFT")).thenReturn(msftQuote);
        assertFalse(qs.fetchQuoteDataFromAPI("MSFT").isEmpty());

        when(mockQS.doesExist(quoteDao, "MSFTX")).thenReturn(false);
        when(mockQhh.fetchQuoteInfo("MSFTX")).thenReturn(emptyQuote);
        assertTrue(qs.fetchQuoteDataFromAPI("MSFTX").isEmpty());
    }
}
