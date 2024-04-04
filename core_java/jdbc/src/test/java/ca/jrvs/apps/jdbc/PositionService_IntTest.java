package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.dao.PositionDao;
import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.dto.Position;
import ca.jrvs.apps.jdbc.dto.Quote;
import ca.jrvs.apps.jdbc.util.DatabaseConnectionManager;
import ca.jrvs.apps.jdbc.util.PositionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PositionService_IntTest {
    public static DatabaseConnectionManager dcm;
    public static Connection c;
    public static QuoteDao quoteDao;
    public static Quote msftQuote;

    public static PositionDao positionDao;
    public static PositionService ps;
    public static Position msftPosition;
    public static Position emptyPosition;

    @BeforeAll
    public static void init() throws SQLException {
        dcm = new DatabaseConnectionManager();
        c = dcm.getConnection();

        positionDao = new PositionDao(c);
        quoteDao = new QuoteDao(c);

        msftQuote = new Quote();
        msftPosition = new Position();
        emptyPosition = new Position();
        ps = new PositionService();
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
    public void test_buy_create_success() {
        // successful buy on a new stock
        Position aaplPos1 = ps.buy("AAPL", 5, 100);
        assertEquals(aaplPos1.getTicker(), "AAPL");
        assertEquals(aaplPos1.getNumOfShares(), 5);
        assertEquals(aaplPos1.getValuePaid(), 500, 0.0001);
    }

    @Test
    public void test_buy_create_fakeSymbol() {
        // symbol does not exist
        Position fakePos1 = ps.buy("MSFTX", 1, 100);
        assertEquals(fakePos1.getTicker(), emptyPosition.getTicker());
        assertEquals(fakePos1.getNumOfShares(), emptyPosition.getNumOfShares());
        assertEquals(fakePos1.getValuePaid(), emptyPosition.getValuePaid(), 0.0001);
    }

//    @Test
//    public void test_buy_create_invalidShares() {
//        // inputted share amount is larger than total shares
//        Position fakePos2 = ps.buy("MSFT", 1000000000, 100);
//        assertEquals(fakePos2.getTicker(), emptyPosition.getTicker());
//        assertEquals(fakePos2.getNumOfShares(), emptyPosition.getNumOfShares());
//        assertEquals(fakePos2.getValuePaid(), emptyPosition.getValuePaid(), 0.0001);
//    }

    @Test
    public void test_buy_update_success() {
        // successful buy on a stock previously purchased
        Position msftPos1 = ps.buy("MSFT", 2, 422.86);
        assertEquals(msftPos1.getTicker(), msftPosition.getTicker());
        assertEquals(msftPos1.getNumOfShares(), msftPosition.getNumOfShares()+2);
        assertEquals(msftPos1.getValuePaid(), 1273.13, 0.0001);
    }


    @Test
    public void test_sell() {
        ps.sell("MSFT");
        assertFalse(ps.doesExist(positionDao, "MSFT"));

        ps.sell("MSFTX");
        assertFalse(ps.doesExist(positionDao, "MSFTX"));
    }

}
