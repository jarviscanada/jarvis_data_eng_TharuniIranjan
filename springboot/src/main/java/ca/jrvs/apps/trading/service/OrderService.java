package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.entity.SecurityOrder;
import ca.jrvs.apps.trading.entity.Account;
import ca.jrvs.apps.trading.entity.Position;
import ca.jrvs.apps.trading.model.Quote;
import ca.jrvs.apps.trading.model.MarketOrder;
import ca.jrvs.apps.trading.util.TradingAppTools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class OrderService {

    private final AccountDao accountDao;
    private final SecurityOrderDao securityOrderDao;
    private final QuoteDao quoteDao;
    private final PositionDao positionDao;

    @Autowired
    public OrderService(AccountDao accountDao, SecurityOrderDao securityOrderDao, QuoteDao quoteDao, PositionDao positionDao) {
        this.accountDao = accountDao;
        this.securityOrderDao = securityOrderDao;
        this.quoteDao = quoteDao;
        this.positionDao = positionDao;
    }

    /**
     * Execute a market order
     * - validate the order (e.g. size and ticker)
     * - create a securityOrder
     * - handle buy or sell orders
     * 	- buy order : check account balance
     * 	- sell order : check position for the ticker/symbol
     * 	- do not forget to update the securityOrder.status
     * - save and return securityOrder
     *
     * NOTE: you are encouraged to make some helper methods (protected or private)
     *
     * @param orderData market order
     * @return SecurityOrder from security_order table
     * @throws DataAccessException if unable to get data from DAO
     * @throws IllegalArgumentException for invalid inputs
     */
    public SecurityOrder executeMarketOrder(MarketOrder orderData) throws ParseException {
        // Validate the market order
        if (orderData == null || orderData.getSize() == 0 || !TradingAppTools.validTicker(orderData.getTicker())) {
            throw new IllegalArgumentException("Invalid market order data");
        }

        // Fetch account information
        Account account = accountDao.findById(orderData.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        // Fetch quote information
        Quote quote = quoteDao.findById(orderData.getTicker())
                .orElseThrow(() -> new IllegalArgumentException("Ticker not found"));


        // Create a SecurityOrder
        SecurityOrder securityOrder = new SecurityOrder();
        securityOrder.setAccountId(orderData.getAccountId());
        securityOrder.setStatus("PENDING");
        securityOrder.setTicker(orderData.getTicker());
        securityOrder.setSize(orderData.getSize());
        securityOrder.setPrice(quote.getLastPrice());

        // Handle buy or sell orders
        if (orderData.getSize() > 0) {
            handleBuyMarketOrder(orderData, securityOrder, account);
        } else {
            handleSellMarketOrder(orderData, securityOrder, account);
        }

        // Save and return the securityOrder
        return securityOrderDao.save(securityOrder);
    }

    /**
     * Helper method to execute a buy order
     *
     * @param marketOrderDto user order
     * @param securityOrder to be saved in database
     * @param account account
     */
    protected void handleBuyMarketOrder(MarketOrder orderData, SecurityOrder securityOrder, Account account) {
        double totalPrice = securityOrder.getPrice() * securityOrder.getSize();
        if (account.getAmount() < totalPrice) {
            securityOrder.setStatus("CANCELLED");
            securityOrder.setNotes("Insufficient funds");
        } else {
            account.setAmount(account.getAmount() - totalPrice);
            accountDao.save(account);
            securityOrder.setStatus("FILLED");
        }
        securityOrderDao.save(securityOrder);
    }


    /**
     * Helper method to execute a sell order
     *
     * @param marketOrderDto user order
     * @param securityOrder to be saved in database
     * @param account account
     */
    protected void handleSellMarketOrder(MarketOrder orderData, SecurityOrder securityOrder, Account account) throws ParseException {
        Position position = positionDao.findByAccountIdAndTicker(account.getId(), securityOrder.getTicker())
                .orElseThrow(() -> new IllegalArgumentException("Position not found for account and ticker"));

        // convert localdate to date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String today = dtf.format(now);
        position.setDate_of_position(TradingAppTools.convertStringtoDate(today));
        positionDao.save(position);

        double totalPrice = securityOrder.getPrice() * Math.abs(securityOrder.getSize());
        account.setAmount(account.getAmount() + totalPrice);
        accountDao.save(account);

        securityOrder.setStatus("FILLED");
        securityOrderDao.save(securityOrder);
    }
}

