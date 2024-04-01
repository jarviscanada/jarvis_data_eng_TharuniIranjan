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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class PositionService_IntTest {
    public String databaseName = "stock_quote";
    public DatabaseConnectionManager dcm;
    public Connection c;

    public QuoteDao quoteDao;
    public QuoteDao mockQuoteDao;
    public QuoteHTTPHelper qhh;
    public QuoteService mockQS = new QuoteService();
    public QuoteService qs = new QuoteService();
    public Quote msftQuote = new Quote();
    public Quote emptyQuote = new Quote();


    public PositionDao positionDao;
    public PositionService ps = new PositionService();
    public PositionService mockPS = new PositionService();

    public Position msftPosition = new Position();
    public String msftSymbol = "MSFT";
    public int msftShares = 2;
    public double msftPrice = 422.86;
    public int msftVolume = 1806045;
    public double msftOldPrice = 427.41;
    public double msftExpectedPrice = (msftShares*msftPrice)+msftOldPrice;

    public String aaplSymbol = "AAPL";
    public int aaplShares = 5;
    public double aaplPrice = 100;
    public double aaplExpectedPrice = 500;

    public Position emptyPosition = new Position();
    public String newSymbol = "GOOG";
    public String fakeSymbol = "MSFTX";
    public int defaultShares = 1;
    public int largeShares = 1000000000;
    public String date;


    @Before
    public void init() throws SQLException {
        dcm = new DatabaseConnectionManager(databaseName);
        c = dcm.getConnection();

        positionDao = new PositionDao(c);
        positionDao.deleteAll();

        quoteDao = new QuoteDao(c);
        quoteDao.deleteAll();

        msftQuote.setSymbol(msftSymbol);
        msftQuote.setOpen("425.24");
        msftQuote.setHigh("427.41");
        msftQuote.setLow("421.61");
        msftQuote.setPrice(String.valueOf(msftPrice));
        msftQuote.setVolume(String.valueOf(msftVolume));
        msftQuote.setLatestTradingDay("2024-03-25");
        msftQuote.setPreviousClose("428.74");
        msftQuote.setChange("-5.88");
        msftQuote.setChangePercent("-1.3715%");
        quoteDao.save(msftQuote);


        msftPosition.setTicker(msftSymbol);
        msftPosition.setNumOfShares(defaultShares);
        msftPosition.setValuePaid(msftOldPrice);
        positionDao.save(msftPosition);


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        LocalDate yesterdayDate = now.minusDays(1);
        date = dtf.format(yesterdayDate);
    }

    @Test
    public void test_buy_create_success() {
        // successful buy on a new stock
        Position aaplPos1 = ps.buy(aaplSymbol, aaplShares, aaplPrice);
        assertEquals(aaplPos1.getTicker(), aaplSymbol);
        assertEquals(aaplPos1.getNumOfShares(), aaplShares);
        assertEquals(aaplPos1.getValuePaid(), aaplExpectedPrice, 0.0001);
    }

    @Test
    public void test_buy_create_fakeSymbol() {
        // symbol does not exist
        Position fakePos = ps.buy(fakeSymbol, defaultShares, aaplPrice);
        assertEquals(fakePos.getTicker(), emptyPosition.getTicker());
        assertEquals(fakePos.getNumOfShares(), emptyPosition.getNumOfShares());
        assertEquals(fakePos.getValuePaid(), emptyPosition.getValuePaid(), 0.0001);
    }

    @Test
    public void test_buy_create_invalidShares() {
        // inputted share amount is larger than total shares
        Position fakePos = ps.buy(newSymbol, largeShares, aaplPrice);
        assertEquals(fakePos.getTicker(), emptyPosition.getTicker());
        assertEquals(fakePos.getNumOfShares(), emptyPosition.getNumOfShares());
        assertEquals(fakePos.getValuePaid(), emptyPosition.getValuePaid(), 0.0001);
    }

    @Test
    public void test_buy_update_success() {
        // successful buy on a stock previously purchased
        Position msftPos1 = ps.buy(msftSymbol, msftShares, msftPrice);
        assertEquals(msftPos1.getTicker(), msftPosition.getTicker());
        assertEquals(msftPos1.getNumOfShares(), msftPosition.getNumOfShares()+msftShares);
        assertEquals(msftPos1.getValuePaid(), msftExpectedPrice, 0.0001);
    }

    @Test
    public void test_buy_update_invalidShares() {
        // successful buy on a stock previously purchased
        Position msftPos2 = ps.buy(msftSymbol, largeShares, msftPrice);
        assertEquals(msftPos2.getTicker(), emptyPosition.getTicker());
        assertEquals(msftPos2.getNumOfShares(), emptyPosition.getNumOfShares());
        assertEquals(msftPos2.getValuePaid(), emptyPosition.getValuePaid(), 0.0001);
    }

}
