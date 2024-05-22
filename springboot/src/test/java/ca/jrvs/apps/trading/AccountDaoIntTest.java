package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.entity.Account;
import ca.jrvs.apps.trading.entity.Trader;
import ca.jrvs.apps.trading.util.TradingAppTools;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
public class AccountDaoIntTest {
    @Autowired
    private AccountDao accountDao;
    private Account savedAccount;
    @Autowired
    private TraderDao traderDao;
    private Trader savedTrader;

    @Before
    public void init() throws ParseException {
        accountDao.deleteAll();
        traderDao.deleteAll();

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

    }

    @Test
    public void testSave() {
        Account account1 = new Account();
        account1.setTraderId(savedTrader.getId());
        account1.setAmount(200.0);
        accountDao.save(account1);

        assertNotNull(account1);
        assertEquals(savedTrader.getId(), account1.getTraderId());
        assertEquals(200.0, account1.getAmount(), 0.0001);
    }

    @Test
    public void testFindById() {
        assertTrue(accountDao.findById(savedAccount.getId()).isPresent());
    }

    @Test
    public void testFindAll() throws ParseException {
        Trader trader2 = new Trader();
        trader2.setFirst_name("Alex");
        trader2.setLast_name("Dough");
        trader2.setCountry("UK");
        trader2.setDob(TradingAppTools.convertStringtoDate("01-03-2000"));
        trader2.setEmail("jane.playdough@example.com");
        traderDao.save(trader2);

        Account account2 = new Account();
        account2.setTraderId(trader2.getId());
        account2.setAmount(300.50);
        accountDao.save(account2);


        List<Account> foundAccounts = accountDao.findAll();
        assertEquals(2, foundAccounts.size());
    }

    @Test
    public void testDeleteById() {
        accountDao.deleteById(savedAccount.getId());
        assertTrue(accountDao.findById(savedTrader.getId()).isEmpty());
    }

    @After
    public void clear(){
        accountDao.deleteAll();
        traderDao.deleteAll();
    }
}