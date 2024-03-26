package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PositionDao_Test {
    public String databaseName = "stock_quote";
    public DatabaseConnectionManager dcm;
    public PositionDao positionDao;
    public Connection c;
    public Position samplePosition1 = new Position();
    public Position samplePosition2 = new Position();

    @Before
    public void init() throws SQLException {
        dcm = new DatabaseConnectionManager(databaseName);
        c = dcm.getConnection();
        positionDao = new PositionDao(c);
        positionDao.deleteAll();

        samplePosition1.setTicker("AAPL");
        samplePosition1.setNumOfShares(1);
        samplePosition1.setValuePaid(170.85);


        samplePosition2.setTicker("MSFT");
        samplePosition2.setNumOfShares(2);
        samplePosition2.setValuePaid(845.72);
        positionDao.save(samplePosition2);
    }

    @Test
    public void test_create() throws JsonProcessingException {
        assertNull(positionDao.save(samplePosition1));
    }

    @Test
    public void test_read_all() throws JsonProcessingException {
        List<Position> expected = new ArrayList<Position>();
        expected.add(samplePosition2);
        expected.add(samplePosition1);

        positionDao.save(samplePosition1);
        Iterable<Position> actual = positionDao.findAll();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void test_read_one_false() {
        Optional<Position> actual = positionDao.findById("MSFTX");
        assertNull(actual.get().getTicker());
    }

    @Test
    public void test_delete_one() {
        positionDao.deleteById("MSFT");
        Optional<Position> actual = positionDao.findById("MSFT");
        assertNull(actual.get().getTicker());
    }
}
