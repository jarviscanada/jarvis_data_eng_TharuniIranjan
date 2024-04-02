package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuoteService_UnitTest  {
    public DatabaseConnectionManager dcm;
    public Connection c;
    public QuoteDao mockQuoteDao;
    public QuoteDao quoteDao;
    public PositionDao positionDao;
    public QuoteHTTPHelper mockQhh;
    public Quote msftQuote = new Quote();
    public Quote emptyQuote = new Quote();
    public QuoteService qs = new QuoteService();
    public QuoteService mockQS = new QuoteService();
    public String msftSymbol = "MSFT";
    public String fakeSymbol = "MSFTX";

    @Before
    public void init() throws SQLException {
        dcm = new DatabaseConnectionManager();
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

        mockQuoteDao = mock(QuoteDao.class);
        mockQhh = mock(QuoteHTTPHelper.class);
        mockQS = mock(QuoteService.class);
    }

    @Test
    public void test_doesExist() {
        assertTrue(qs.doesExist(quoteDao, msftSymbol));
        assertFalse(qs.doesExist(quoteDao, fakeSymbol));
    }

    @Test
    public void test_fetchApi() {
        when(mockQS.doesExist(quoteDao, msftSymbol)).thenReturn(true);
        when(mockQhh.fetchQuoteInfo(msftSymbol)).thenReturn(msftQuote);
        assertFalse(qs.fetchQuoteDataFromAPI(msftSymbol).isEmpty());

        when(mockQS.doesExist(quoteDao, fakeSymbol)).thenReturn(false);
        when(mockQhh.fetchQuoteInfo(fakeSymbol)).thenReturn(emptyQuote);
        assertTrue(qs.fetchQuoteDataFromAPI(fakeSymbol).isEmpty());
    }
}
