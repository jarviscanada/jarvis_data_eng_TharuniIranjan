package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.entity.Account;
import ca.jrvs.apps.trading.entity.SecurityOrder;
import ca.jrvs.apps.trading.entity.Trader;
import ca.jrvs.apps.trading.model.Quote;
import ca.jrvs.apps.trading.util.TradingAppTools;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
public class SecurityOrderDaoIntTest {
    @Autowired
    private SecurityOrderDao securityOrderDao;
    private SecurityOrder savedOrder;
    @Autowired
    private AccountDao accountDao;
    private Account savedAccount;
    @Autowired
    private TraderDao traderDao;
    private Trader savedTrader;
    @Autowired
    private QuoteDao quoteDao;
    private Quote savedQuote;

    @Before
    public void init() throws ParseException {
        securityOrderDao.deleteAll();
        accountDao.deleteAll();
        traderDao.deleteAll();
        quoteDao.deleteAll();

        savedTrader = new Trader();
        savedTrader.setFirst_name("John");
        savedTrader.setLast_name("Doe");
        savedTrader.setDob(TradingAppTools.convertStringtoDate("01-01-2000"));
        savedTrader.setCountry("USA");
        savedTrader.setEmail("john.doe@example.com");
        traderDao.save(savedTrader);

        savedAccount = new Account();
        savedAccount.setTraderId(savedTrader.getId());
        savedAccount.setAmount(100.01);
        accountDao.save(savedAccount);

        savedQuote = new Quote(); // Initialize savedQuote here
        savedQuote.setAskPrice(10d);
        savedQuote.setAskSize(10);
        savedQuote.setBidPrice(10.2d);
        savedQuote.setBidSize(10);
        savedQuote.setLastPrice(10.1d);
        savedQuote.setId("AAPL");
        quoteDao.save(savedQuote);

        savedOrder = new SecurityOrder();
        savedOrder.setAccount_id(savedAccount.getId());
        savedOrder.setTicker(savedQuote.getId());
        savedOrder.setStatus("PLACED");
        savedOrder.setSize(1);
        savedOrder.setPrice(100.0);
        savedOrder.setNotes("n/a");
        securityOrderDao.save(savedOrder);
    }

    @Test
    public void testSave() {
        assertNotNull(savedOrder);
        assertEquals("PLACED", savedOrder.getStatus());
        assertEquals(100.0, savedOrder.getPrice(), 0.0001);
        assertEquals(1, savedOrder.getSize());
        assertEquals("n/a", savedOrder.getNotes());
    }

    @Test
    public void testFindById() {
        assertTrue(securityOrderDao.findById(savedOrder.getId()).isPresent());
    }

    @Test
    public void testDeleteById() {
        securityOrderDao.deleteById(savedOrder.getId());
        assertTrue(securityOrderDao.findById(savedOrder.getId()).isEmpty());
    }
}
