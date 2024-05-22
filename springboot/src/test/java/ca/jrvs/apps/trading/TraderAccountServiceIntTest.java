package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.entity.Account;
import ca.jrvs.apps.trading.entity.Trader;
import ca.jrvs.apps.trading.jpa.AccountJpaRepository;
import ca.jrvs.apps.trading.model.TraderAccountView;
import ca.jrvs.apps.trading.service.AccountService;
import ca.jrvs.apps.trading.service.QuoteService;
import ca.jrvs.apps.trading.service.TraderAccountService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class, TraderAccountService.class})
public class TraderAccountServiceIntTest {
    private TraderAccountView savedView;
    @Autowired
    private TraderAccountService traderAccountService;
    @Autowired
    private TraderDao traderDao;
    private Trader savedTrader;

    @Autowired
    private AccountDao accountDao;

    @Before
    public void setup() {
//        accountDao.deleteAll();
//        traderDao.deleteAll();

        savedTrader = new Trader();
        savedTrader.setFirst_name("John");
        savedTrader.setLast_name("Doe");
        savedTrader.setCountry("USA");
        savedTrader.setDob(new Date(2000, 1, 1));
        savedTrader.setEmail("john.doe@example.com");
        savedTrader = traderDao.save(savedTrader);

        savedView = traderAccountService.createTraderAndAccount(savedTrader);
    }

    @Test
    public void createTraderAndAccount() {
        assertNotNull(savedView);
        assertNotNull(savedView.getAccount());
        assertNotNull(savedView.getTrader());
    }

    @Test
    public void deposit() {
        savedView = traderAccountService.createTraderAndAccount(savedTrader);
        Integer traderId = savedView.getTrader().getId();
        Account account = savedView.getAccount();
        double currentAmount = account.getAmount();

        double depositAmount = 1000.0;
        Account updatedAccount = traderAccountService.deposit(traderId, depositAmount);

        assertNotNull(updatedAccount);
        assertEquals(currentAmount + depositAmount, updatedAccount.getAmount(), 0.001);
    }

    @Test
    public void withdraw() {
        savedView = traderAccountService.createTraderAndAccount(savedTrader);
        Integer traderId = savedView.getTrader().getId();
        Account account = savedView.getAccount();
        double currentAmount = account.getAmount();

        // Deposit some amount first
        double depositAmount = 1000.0;
        traderAccountService.deposit(traderId, depositAmount);

        // Now withdraw a portion of it
        double withdrawAmount = 500.0;
        Account updatedAccount = traderAccountService.withdraw(traderId, withdrawAmount);

        assertNotNull(updatedAccount);
        assertEquals(currentAmount + depositAmount - withdrawAmount, updatedAccount.getAmount(), 0.001);
    }
    @Test
    public void deleteTraderById() {
        savedView = traderAccountService.createTraderAndAccount(savedTrader);
        Integer traderId = savedView.getTrader().getId();
        traderAccountService.deleteTraderById(traderId);
    }

    @After
    public void clear(){
        accountDao.deleteAll();
        traderDao.deleteAll();
    }
}
