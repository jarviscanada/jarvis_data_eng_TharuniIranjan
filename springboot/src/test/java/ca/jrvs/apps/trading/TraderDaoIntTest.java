package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.util.TradingAppTools;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.entity.Trader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
public class TraderDaoIntTest {
    @Autowired
    private TraderDao traderDao;
    private Trader savedTrader;

    @Before
    public void init() throws ParseException {
        savedTrader = new Trader();
        savedTrader.setFirst_name("John");
        savedTrader.setLast_name("Doe");
        savedTrader.setDob(TradingAppTools.convertStringtoDate("01-01-2000"));
        savedTrader.setCountry("USA");
        savedTrader.setEmail("john.doe@example.com");
        traderDao.save(savedTrader);
    }

    @Test
    public void testSave() throws ParseException {
        Trader trader = new Trader();
        trader.setFirst_name("Jane");
        trader.setLast_name("Doe");
        trader.setCountry("USA");
        trader.setDob(TradingAppTools.convertStringtoDate("01-02-2000"));
        trader.setEmail("jane.doe@example.com");

        Trader savedTrader2 = traderDao.save(trader);

        assertNotNull(savedTrader2);
        assertEquals("Jane", savedTrader2.getFirst_name());
        assertEquals("Doe", savedTrader2.getLast_name());
        assertEquals("USA", savedTrader2.getCountry());
        assertEquals("jane.doe@example.com", savedTrader2.getEmail());
    }

    @Test
    public void testFindById() {
        assertTrue(traderDao.findById(savedTrader.getId()).isPresent());
    }

    @Test
    public void testFindAll() throws ParseException {
        Trader trader2 = new Trader();
        trader2.setFirst_name("Alex");
        trader2.setLast_name("Dough");
        trader2.setCountry("UK");
        trader2.setDob(TradingAppTools.convertStringtoDate("01-03-2000"));
        trader2.setEmail("jane.playdough@example.com");

        Trader trader3 = new Trader();
        trader3.setFirst_name("Mike");
        trader3.setLast_name("Dode");
        trader3.setCountry("Canada");
        trader3.setDob(TradingAppTools.convertStringtoDate("01-04-2000"));
        trader3.setEmail("mike.dode@example.com");

        List<Trader> traders = Arrays.asList(trader2, trader3);
        traderDao.saveAll(traders);

        List<Trader> foundTraders = traderDao.findAll();
        assertEquals(traders.size()+1, foundTraders.size());
    }

    @Test
    public void testDeleteById() {
        traderDao.deleteById(savedTrader.getId());
        assertTrue(traderDao.findById(savedTrader.getId()).isEmpty());
    }

    @After
    public void clear(){
        traderDao.deleteAll();
    }
}
