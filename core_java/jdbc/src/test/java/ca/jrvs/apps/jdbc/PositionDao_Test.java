package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.dao.PositionDao;
import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.dto.Position;
import ca.jrvs.apps.jdbc.dto.Quote;
import ca.jrvs.apps.jdbc.util.DatabaseConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class PositionDao_Test {
    public DatabaseConnectionManager dcm;
    public PositionDao positionDao;
    public Connection c;
    public Position msftPosition = new Position();
    public Position aaplPosition = new Position();

    public QuoteDao quoteDao;
    public Quote msftQuote = new Quote();
    public Quote aaplQuote = new Quote();
    public String msftSymbol = "MSFT";
    public String fakeSymbol = "MSFTX";
    public int msftShares = 2;
    public double msftPrice = 422.86;
    public int msftVolume = 1806045;

    @BeforeEach
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
        msftQuote.setPrice(String.valueOf(msftPrice));
        msftQuote.setVolume(String.valueOf(msftVolume));
        msftQuote.setLatestTradingDay("2024-03-25");
        msftQuote.setPreviousClose("428.74");
        msftQuote.setChange("-5.88");
        msftQuote.setChangePercent("-1.3715%");
        quoteDao.save(msftQuote);

        aaplQuote.setSymbol("AAPL");
        aaplQuote.setOpen("170.39");
        aaplQuote.setHigh("171.94");
        aaplQuote.setLow("169.45");
        aaplQuote.setPrice("170.85");
        aaplQuote.setVolume("53900471");
        aaplQuote.setLatestTradingDay("2024-03-25");
        aaplQuote.setPreviousClose("172.28");
        aaplQuote.setChange("-1.43");
        aaplQuote.setChangePercent("-0.8300%");
        quoteDao.save(aaplQuote);

        msftPosition.setTicker(msftSymbol);
        msftPosition.setNumOfShares(2);
        msftPosition.setValuePaid(845.72);
        positionDao.save(msftPosition);

        aaplPosition.setTicker("AAPL");
        aaplPosition.setNumOfShares(1);
        aaplPosition.setValuePaid(170.85);
    }

    @Test
    public void test_create() {
        assertNull(positionDao.save(aaplPosition));
        Position findPosition1 = positionDao.findById("AAPL").get();
        assertEquals(findPosition1.getTicker(), aaplPosition.getTicker());
        assertEquals(findPosition1.getNumOfShares(), aaplPosition.getNumOfShares());
        assertEquals(findPosition1.getValuePaid(), aaplPosition.getValuePaid(), 0.0001);
    }

    @Test
    public void test_update() {
        Position newPosition = new Position();
        newPosition.setTicker(msftSymbol);
        newPosition.setNumOfShares(msftShares);
        newPosition.setValuePaid(msftPrice);

        assertNotNull(positionDao.update(newPosition));
    }

    @Test
    public void test_read_all() {
        List<Position> expected = new ArrayList<Position>();
        expected.add(msftPosition);
        expected.add(aaplPosition);
        positionDao.save(aaplPosition);

        Iterable<Position> actual = positionDao.findAll();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void test_read_one_false() {
        Optional<Position> actual = positionDao.findById(fakeSymbol);
        assertNull(actual.get().getTicker());
    }

    @Test
    public void test_delete_one() {
        positionDao.deleteById(msftSymbol);
        Optional<Position> actual = positionDao.findById(msftSymbol);
        assertNull(actual.get().getTicker());
    }
}
