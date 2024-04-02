package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuoteDao_Test {
    public DatabaseConnectionManager dcm;
    public PositionDao positionDao;
    public QuoteDao quoteDao;
    public Connection c;
    public Quote sampleQuote1 = new Quote();
    public Quote sampleQuote2 = new Quote();
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

        sampleQuote1.setSymbol("AAPL");
        sampleQuote1.setOpen("170.39");
        sampleQuote1.setHigh("171.94");
        sampleQuote1.setLow("169.45");
        sampleQuote1.setPrice("170.85");
        sampleQuote1.setVolume("53900471");
        sampleQuote1.setLatestTradingDay("2024-03-25");
        sampleQuote1.setPreviousClose("172.28");
        sampleQuote1.setChange("-1.43");
        sampleQuote1.setChangePercent("-0.8300%");

        sampleQuote2.setSymbol(msftSymbol);
        sampleQuote2.setOpen("425.24");
        sampleQuote2.setHigh("427.41");
        sampleQuote2.setLow("421.61");
        sampleQuote2.setPrice("422.86");
        sampleQuote2.setVolume("1806045");
        sampleQuote2.setLatestTradingDay("2024-03-25");
        sampleQuote2.setPreviousClose("428.74");
        sampleQuote2.setChange("-5.88");
        sampleQuote2.setChangePercent("-1.3715%");
        quoteDao.save(sampleQuote2);

    }

    @Test
    public void test_create() {
        assertNull(quoteDao.save(sampleQuote1));
        Quote findQuote = quoteDao.findById("AAPL").get();
        assertEquals(findQuote.getSymbol(), sampleQuote1.getSymbol());
        assertEquals(findQuote.getOpen(), sampleQuote1.getOpen());
        assertEquals(findQuote.getHigh(), sampleQuote1.getHigh());
        assertEquals(findQuote.getLow(), sampleQuote1.getLow());
        assertEquals(findQuote.getPrice(), sampleQuote1.getPrice());
        assertEquals(findQuote.getVolume(), sampleQuote1.getVolume());
        assertEquals(findQuote.getLatestTradingDay(), sampleQuote1.getLatestTradingDay());
        assertEquals(findQuote.getPreviousClose(), sampleQuote1.getPreviousClose());
        assertEquals(findQuote.getChange(), sampleQuote1.getChange());
        assertEquals(findQuote.getChangePercent(), sampleQuote1.getChangePercent());
    }

    @Test
    public void test_update() {
        Quote sampleQuote3 = new Quote();
        sampleQuote3.setSymbol(msftSymbol);
        sampleQuote3.setOpen("425.25");
        sampleQuote3.setHigh("427.42");
        sampleQuote3.setLow("421.62");
        sampleQuote3.setPrice("422.87");
        sampleQuote3.setVolume("1806046");
        sampleQuote3.setLatestTradingDay("2024-03-25");
        sampleQuote3.setPreviousClose("428.75");
        sampleQuote3.setChange("-5.89");
        sampleQuote3.setChangePercent("-1.3716%");

        assertNotNull(quoteDao.update(sampleQuote3));
        Quote findQuote2 = quoteDao.findById(msftSymbol).get();
        assertEquals(findQuote2.getSymbol(), sampleQuote3.getSymbol());
        assertEquals(findQuote2.getOpen(), sampleQuote3.getOpen());
        assertEquals(findQuote2.getHigh(), sampleQuote3.getHigh());
        assertEquals(findQuote2.getLow(), sampleQuote3.getLow());
        assertEquals(findQuote2.getPrice(), sampleQuote3.getPrice());
        assertEquals(findQuote2.getVolume(), sampleQuote3.getVolume());
        assertEquals(findQuote2.getLatestTradingDay(), sampleQuote3.getLatestTradingDay());
        assertEquals(findQuote2.getPreviousClose(), sampleQuote3.getPreviousClose());
        assertEquals(findQuote2.getChange(), sampleQuote3.getChange());
        assertEquals(findQuote2.getChangePercent(), sampleQuote3.getChangePercent());
    }

    @Test
    public void test_read_all() {
        List<Quote> expected = new ArrayList<>();
        expected.add(sampleQuote2);
        expected.add(sampleQuote1);

        quoteDao.save(sampleQuote1);
        Iterable<Quote> actual = quoteDao.findAll();
        assertEquals(actual.toString(), expected.toString());
    }

    @Test
    public void test_read_one_true() {
        Optional<Quote> expected = Optional.of(sampleQuote2);
        Optional<Quote> actual = quoteDao.findById(msftSymbol);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void test_read_one_false() {
        // Optional<Quote> expected = Optional.of(sampleQuote2);
        Optional<Quote> actual = quoteDao.findById(fakeSymbol);
        assertNull(actual.get().getSymbol());
    }

    @Test
    public void test_delete_one() {
        quoteDao.deleteById(msftSymbol);
        Optional<Quote> actual = quoteDao.findById(msftSymbol);
        assertNull(actual.get().getSymbol());
    }

    @Test
    public void test_delete_all() {
        List<Quote> expected = Collections.<Quote>emptyList();
        quoteDao.deleteAll();
        Iterable<Quote> actual = quoteDao.findAll();
        assertEquals(expected.toString(), actual.toString());
    }
}
