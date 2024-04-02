package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PositionService_UnitTest {
    public DatabaseConnectionManager dcm;
    public Connection c;
    public QuoteDao quoteDao;
    public PositionDao positionDao;
    public QuoteDao mockQuoteDao;
    public QuoteHTTPHelper mockQhh;
    public Quote msftQuote = new Quote();
    public Position msftPosition = new Position();
    public PositionService ps = new PositionService();
    public QuoteService mockQS = new QuoteService();
    public PositionService mockPS = new PositionService();
    public String msftSymbol = "MSFT";
    public String fakeSymbol = "MSFTX";
    public int shares = 2;
    public double price = 422.86;
    public int volume = 1806045;
    public double oldVal = 427.41;
    public double expected = 1273.13; //(numShares*price)+oldValue;


    @Before
    public void init() throws SQLException {
        dcm = new DatabaseConnectionManager();
        c = dcm.getConnection();

        positionDao = new PositionDao(c);
        positionDao.deleteAll();

        quoteDao = new QuoteDao(c);
        quoteDao.deleteAll();

        msftQuote.setSymbol(msftSymbol);
        msftQuote.setOpen("425.24");
        msftQuote.setHigh("427.41");
        msftQuote.setLow("421.61");
        msftQuote.setPrice(String.valueOf(price));
        msftQuote.setVolume(String.valueOf(volume));
        msftQuote.setLatestTradingDay("2024-03-25");
        msftQuote.setPreviousClose("428.74");
        msftQuote.setChange("-5.88");
        msftQuote.setChangePercent("-1.3715%");
        quoteDao.save(msftQuote);

        msftPosition.setTicker(msftSymbol);
        msftPosition.setNumOfShares(1);
        msftPosition.setValuePaid(oldVal);
        positionDao.save(msftPosition);

        mockQuoteDao = mock(QuoteDao.class);
        mockQhh = mock(QuoteHTTPHelper.class);
        mockQS = mock(QuoteService.class);
        mockPS = mock(PositionService.class);
    }

    @Test
    public void test_calculateValue() {
        assertEquals(ps.updateValue(shares, price, oldVal), expected, 0.0001);
    }

    @Test
    public void test_isValid() {
        assertTrue(ps.isValidShares(volume, shares));
        assertFalse(ps.isValidShares(volume, volume+1));
    }

    @Test
    public void test_doesExist() {
        assertTrue(ps.doesExist(positionDao, msftSymbol));
        assertFalse(ps.doesExist(positionDao, fakeSymbol));
    }

    @Test
    public void test_sell() {
        when(mockPS.doesExist(positionDao, msftSymbol)).thenReturn(true);
        ps.sell(msftSymbol);
        assertFalse(ps.doesExist(positionDao, msftSymbol));
    }

    @Test
    public void test_buy() {
        when(mockQS.fetchQuoteDataFromAPI(msftSymbol)).thenReturn(Optional.ofNullable(msftQuote));
        when(mockPS.isValidShares(volume, shares)).thenReturn(true);
        when(mockPS.doesExist(positionDao, msftSymbol)).thenReturn(true);
        Position testPos1 = ps.buy(msftSymbol, shares, price);

        assertTrue(mockPS.doesExist(positionDao, msftSymbol));
        assertEquals(testPos1.getTicker(), msftPosition.getTicker());
        assertEquals(testPos1.getNumOfShares(), msftPosition.getNumOfShares()+shares);
        assertEquals(testPos1.getValuePaid(), expected, 0.0001);

    }

}
