package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.entity.Account;
import ca.jrvs.apps.trading.entity.Trader;
import ca.jrvs.apps.trading.model.TraderAccountView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraderAccountService {
    private final TraderDao traderDao;
    private final AccountDao accountDao;
    private final PositionDao positionDao;
    private final SecurityOrderDao securityOrderDao;

    @Autowired
    public TraderAccountService(TraderDao traderDao, AccountDao accountDao, PositionDao positionDao, SecurityOrderDao securityOrderDao) {
        this.traderDao = traderDao;
        this.accountDao = accountDao;
        this.positionDao = positionDao;
        this.securityOrderDao = securityOrderDao;
    }

    /**
     * Create a new trader and initialize a new account with 0 amount
     * - validate user input (all fields must be non empty)
     * - create a trader
     * - create an account
     * - create, setup, and return a new traderAccountView
     *
     * Assumption: to simplify the logic, each trader has only one account where traderId == accountId
     *
     * @param trader cannot be null. All fields cannot be null except for id (auto-generated by db)
     * @return traderAccountView
     * @throws IllegalArgumentException if a trader has null fields or id is not null
     */
    public TraderAccountView createTraderAndAccount(Trader trader) {
        if (trader == null || trader.getId() <= 0 ||
                trader.getFirst_name() == null || trader.getLast_name() == null ||
                trader.getCountry() == null || trader.getDob() == null || trader.getEmail() == null) {
            throw new IllegalArgumentException("Trader must have non-null fields and null id");
        }

        Trader savedTrader = traderDao.save(trader);
        Account account = new Account();
        account.setId(savedTrader.getId());
        account.setTraderId(savedTrader.getId());
        account.setAmount(0.0);
        Account savedAccount = accountDao.save(account);

        return new TraderAccountView(savedTrader, savedAccount);
    }

    /**
     * A trader can be deleted if and only if it has no open position and 0 cash balance
     * - validate traderId
     * - get trader account by traderId and check account balance
     * - get positions by accountId and check positions
     * - delete all securityOrders, account, trader (in this order)
     *
     * @param traderId must not be null
     * @throws IllegalArgumentException if traderId is null or not found or unable to delete
     */
    public void deleteTraderById(Integer traderId) {
        if (traderId == null) {
            throw new IllegalArgumentException("TraderId must not be null");
        }

        // Check if the trader exists
        Trader trader = traderDao.findById(traderId).orElseThrow(() -> new IllegalArgumentException("Trader not found"));

        // Check if the trader has any open positions
        if (positionDao.findByAccountId(traderId) >= 0) {
            throw new IllegalArgumentException("Cannot delete trader with open positions");
        }

        // Check if the trader has any cash balance
        Account account = accountDao.findById(traderId).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        if (account.getAmount() != 0) {
            throw new IllegalArgumentException("Cannot delete trader with non-zero account balance");
        }

        // Delete all security orders associated with the trader
        securityOrderDao.deleteAllByAccountId(traderId);
        // Delete the account
        accountDao.deleteById(traderId);
        // Delete the trader
        traderDao.deleteById(traderId);
    }

    /**
     * Deposit a fund to an account by traderId
     * - validate user input
     * - find account by trader id
     * - update the amount accordingly
     *
     * @param traderId must not be null
     * @param fund     must be greater than 0
     * @return updated Account
     * @throws IllegalArgumentException if traderId is null or not found,
     *                                  and fund is less than or equal to 0
     */
    public Account deposit(Integer traderId, Double fund) {
        if (traderId == null || fund == null || fund <= 0) {
            throw new IllegalArgumentException("TraderId must not be null, and fund must be greater than 0");
        }

        Account account = accountDao.findById(traderId).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setAmount(account.getAmount() + fund);
        return accountDao.save(account);
    }

    /**
     * Withdraw a fund to an account by traderId
     * - validate user input
     * - find account by trader id
     * - update the amount accordingly
     *
     * @param traderId must not be null
     * @param fund     must be greater than 0
     * @return updated Account
     * @throws IllegalArgumentException if traderId is null or not found,
     *                                  and fund is less than or equal to 0
     */
    public Account withdraw(Integer traderId, Double fund) {
        if (traderId == null || fund == null || fund <= 0) {
            throw new IllegalArgumentException("TraderId must not be null, and fund must be greater than 0");
        }

        Account account = accountDao.findById(traderId).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        if (account.getAmount() < fund) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        account.setAmount(account.getAmount() - fund);
        return accountDao.save(account);
    }
}