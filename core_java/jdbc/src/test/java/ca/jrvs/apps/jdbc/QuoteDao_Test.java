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
    public String databaseName = "stock_quote";
    public DatabaseConnectionManager dcm;
    public QuoteDao mockQuoteDao;
    public QuoteDao quoteDao;
    public Connection c;
    public QuoteHTTPHelper mockQhh;
    public Quote sampleQuote1 = new Quote();
    public Quote sampleQuote2 = new Quote();
    @Before
    public void init() throws SQLException {
        dcm = new DatabaseConnectionManager(databaseName);
        c = dcm.getConnection();
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

        sampleQuote2.setSymbol("MSFT");
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

//        mockQuoteDao = mock(QuoteDao.class);
//        mockQhh = mock(QuoteHTTPHelper.class);
//        when(jdbc.doesExist(mockQuoteDao, symbol)).thenReturn(false);
//        when(mockQhh.fetchQuoteInfo(symbol)).thenReturn(sampleQuote1);
//        when(mockQuoteDao.save(sampleQuote1)).thenReturn(null);

    }

    @Test
    public void test_create() throws JsonProcessingException {
        assertNull(quoteDao.save(sampleQuote1));
    }

    @Test
    public void test_read_all() throws JsonProcessingException {
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
        Optional<Quote> actual = quoteDao.findById("MSFT");
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void test_read_one_false() {
        // Optional<Quote> expected = Optional.of(sampleQuote2);
        Optional<Quote> actual = quoteDao.findById("MSFTX");
        assertNull(actual.get().getSymbol());
    }

    @Test
    public void test_delete_one() {
        quoteDao.deleteById("MSFT");
        Optional<Quote> actual = quoteDao.findById("MSFT");
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
