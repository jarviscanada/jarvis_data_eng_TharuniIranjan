package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.dao.PositionDao;
import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.dto.Position;
import ca.jrvs.apps.jdbc.dto.Quote;
import ca.jrvs.apps.jdbc.util.DatabaseConnectionManager;
import ca.jrvs.apps.jdbc.util.PositionService;
import ca.jrvs.apps.jdbc.util.QuoteHTTPHelper;
import ca.jrvs.apps.jdbc.util.QuoteService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PositionService_UnitTest {
    public static DatabaseConnectionManager dcm;
    public static Connection c;
    public static QuoteDao quoteDao;
    public static PositionDao positionDao;
    public static QuoteDao mockQuoteDao;
    public static QuoteHTTPHelper mockQhh;
    public static Quote msftQuote;
    public static Position msftPosition;
    public static PositionService ps;
    public static QuoteService mockQS;
    public static PositionService mockPS;


    @BeforeAll
    public static void init() throws SQLException {
        dcm = new DatabaseConnectionManager();
        c = dcm.getConnection();

        positionDao = new PositionDao(c);
        quoteDao = new QuoteDao(c);

        msftQuote = new Quote();
        msftPosition = new Position();
        ps = new PositionService();

        mockQuoteDao = mock(QuoteDao.class);
        mockQhh = mock(QuoteHTTPHelper.class);
        mockQS = mock(QuoteService.class);
        mockPS = mock(PositionService.class);
    }


    @BeforeEach
    public void setup() {
        positionDao.deleteAll();
        quoteDao.deleteAll();

        msftQuote.setSymbol("MSFT");
        msftQuote.setOpen("425.24");
        msftQuote.setHigh("427.41");
        msftQuote.setLow("421.61");
        msftQuote.setPrice(String.valueOf(422.86));
        msftQuote.setVolume(String.valueOf(1806045));
        msftQuote.setLatestTradingDay("2024-03-25");
        msftQuote.setPreviousClose("428.74");
        msftQuote.setChange("-5.88");
        msftQuote.setChangePercent("-1.3715%");
        quoteDao.save(msftQuote);

        msftPosition.setTicker("MSFT");
        msftPosition.setNumOfShares(1);
        msftPosition.setValuePaid(427.41);
        positionDao.save(msftPosition);
    }

    @Test
    public void test_calculateValue() {
        assertEquals(ps.updateValue(2, 422.86, 427.41), 1273.13, 0.0001);
    }

    @Test
    public void test_isValid() {
        assertTrue(ps.isValidShares(1806045, 2));
        assertFalse(ps.isValidShares(1806045, 1806045+1));
    }

    @Test
    public void test_doesExist() {
        assertTrue(ps.doesExist(positionDao, "MSFT"));
        assertFalse(ps.doesExist(positionDao, "MSFTX"));
    }

    @Test
    public void test_sell() {
        when(mockPS.doesExist(positionDao, "MSFT")).thenReturn(true);
        ps.sell("MSFT");
        assertFalse(ps.doesExist(positionDao, "MSFT"));
    }

    @Test
    public void test_buy() {
        when(mockQS.fetchQuoteDataFromAPI("MSFT")).thenReturn(Optional.of(msftQuote));
        when(mockPS.isValidShares(1806045, 2)).thenReturn(true);
        when(mockPS.doesExist(positionDao, "MSFT")).thenReturn(true);
        Position testPos1 = ps.buy("MSFT", 2, 422.86);

        assertTrue(mockPS.doesExist(positionDao, "MSFT"));
        assertEquals("MSFT", testPos1.getTicker());
        assertEquals(testPos1.getNumOfShares(), 3);
        assertEquals(testPos1.getValuePaid(), 1273.13, 0.0001);

    }

}
